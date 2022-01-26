package com.example.summerschool

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.summerschool.models.NasaResponse
import com.example.summerschool.network.NasaService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_apod.*
import kotlinx.android.synthetic.main.activity_apos_date_selection.*
import kotlinx.android.synthetic.main.activity_apos_date_selection.iv_mains
import kotlinx.android.synthetic.main.activity_apos_date_selection.tv_dates
import kotlinx.android.synthetic.main.activity_apos_date_selection.tv_mains
import retrofit.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.TextView

import android.widget.EditText
import android.widget.DatePicker


class ApodDateSelection : AppCompatActivity() {

//    private var date: String = ""
//
    private var selectedDates: String = ""
//
//    private var dpd: DatePickerDialog? = null

    var picker: DatePickerDialog? = null
    var eText: EditText? = null
    var btnGet: Button? = null
    var tvw: TextView? = null

    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apos_date_selection)

        tvw = findViewById(R.id.tv_dates)
       // eText = findViewById(R.id.editText1)

        et_date.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog
            picker = DatePickerDialog(this@ApodDateSelection,
                { view, year, monthOfYear, dayOfMonth -> selectedDates = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())

                    et_date.setText(selectedDates)

                },
                year,
                month,
                day
            )
            picker!!.show()
        })

        btn_date_selection.setOnClickListener( View.OnClickListener() {

                getAPODDetails(selectedDates)

                //tv_dates.text = ("Selected Date: $selectedDates")

        })



    }

    private fun getAPODDetails(date: String){
        if (Constants.isNetworkAvailable(this)){
            //Toast.makeText(this,"You have internet",Toast.LENGTH_SHORT).show()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service: NasaService = retrofit.create<NasaService>(NasaService::class.java)

            val listCall: Call<NasaResponse> = service.getData(date,Constants.APP_ID)

            showCustomProgressDialog()

            listCall.enqueue(object : Callback<NasaResponse> {
                override fun onResponse(response: Response<NasaResponse>?, retrofit: Retrofit?) {

                    if (response!!.isSuccess){
                        val nasaList: NasaResponse = response.body()

                        loadImage(nasaList)

                    }else{
                        val rc = response.code()
                        when(rc){
                            400->{
                                Log.e("error 400","error")
                            }else ->{
                            Log.e("error","error")
                        }
                        }
                    }
                }
                override fun onFailure(t: Throwable?) {
                    hideProgressDialog()
                }
            })
        }else{
            Toast.makeText(this,"No internet connection available", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)

        mProgressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    private fun loadImage(nasaList: NasaResponse){
        val imageUrl : String = nasaList.url
        Picasso.with(this).load(imageUrl).into(iv_mains)
        tv_mains.text = nasaList.title
        tv_dates.text = "Date of APOD: ${nasaList.date}"

        hideProgressDialog()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_back->{
                val selectIntent = Intent(this,MainActivity::class.java)
                startActivity(selectIntent)
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }


}