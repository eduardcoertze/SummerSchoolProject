package com.example.summerschool

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import retrofit.*
import java.util.*
import com.example.summerschool.models.NasaResponse
import com.example.summerschool.network.NasaService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_apod.*

class Apod : AppCompatActivity() {

    private var monthFinal = 0
    private var monthFinal2 = ""
    private var dateFinal = ""
    private var selectedDate: String = ""

    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod)

        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        monthFinal = month + 1
        if (monthFinal<10){
            monthFinal.toString()
            monthFinal2 = "0$monthFinal"
        }

        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        dateFinal = "$year-${monthFinal2}-$day"

        getAPODDetails(dateFinal)


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
        Picasso.with(this).load(imageUrl).into(iv_main)
        tv_main.text = nasaList.title
        tv_date.text = "Date of APOD: ${nasaList.date}"

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