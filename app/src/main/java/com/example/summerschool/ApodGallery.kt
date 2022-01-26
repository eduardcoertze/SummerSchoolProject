package com.example.summerschool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApodGallery : AppCompatActivity() {

     var recycle: RecyclerView? = null

     var s1 = arrayOf<String>("")
     var s2 = arrayOf<String>("")

     var intArray = intArrayOf(R.drawable.tagging_bennu_2020_10_22,R.drawable.ngc_7822_in_cepheus_2022_1_20,
        R.drawable.postcard_from_the_south_pole_2021_12_11,R.drawable.full_moonlight_2021_11_18,
        R.drawable.mare_frigoris_2020_10_08,R.drawable.messier_101_2021_11_27,
        R.drawable.interior_view_2015_1_23,R.drawable.eye_of_moon_2020_12_02,
        R.drawable.colors_of_the_moon_2020_11_11)



//     var images = arrayOf<Int>(R.drawable.tagging_bennu_2020_10_22,R.drawable.ngc_7822_in_cepheus_2022_1_20,
//                                R.drawable.postcard_from_the_south_pole_2021_12_11,R.drawable.full_moonlight_2021_11_18,
//                                 R.drawable.mare_frigoris_2020_10_08,R.drawable.messier_101_2021_11_27,
//                                   R.drawable.interior_view_2015_1_23,R.drawable.eye_of_moon_2020_12_02,
//                                    R.drawable.colors_of_the_moon_2020_11_11)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apod_gallery)

        recycle = findViewById(R.id.recycler_view)

        s1 = resources.getStringArray(R.array.image_titles)
        s2 = resources.getStringArray(R.array.image_date)

        var myAdapter = MyAdapter(this, s1, s2, intArray)

        recycle!!.adapter = myAdapter
        recycle!!.layoutManager = LinearLayoutManager(this)


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