package com.night.smallimageview

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.night.image.SmallImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<SmallImageView>(R.id.iv_1).setRadius(dpToPx(30))
        findViewById<SmallImageView>(R.id.iv_2).setRadius(dpToPx(30), 0F, 0F, dpToPx(30))
        findViewById<SmallImageView>(R.id.iv_3).setRadius(0F, dpToPx(30), dpToPx(30), 0F)
        findViewById<SmallImageView>(R.id.iv_4).setRadius(
            dpToPx(7),
            dpToPx(14),
            dpToPx(21),
            dpToPx(28)
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_list){
            startActivity(Intent(this,ListActivity::class.java))
        }else if(item.itemId == R.id.menu_single){
            startActivity(Intent(this,SingleActivity::class.java))
        }else if(item.itemId == R.id.menu_test){
            startActivity(Intent(this,TestActivity::class.java))
        }
        return true
    }


    private fun dpToPx(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }
}