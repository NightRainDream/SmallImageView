package com.night.smallimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.widget.AppCompatSeekBar
import com.night.image.SmallImageView

class TestActivity : AppCompatActivity() {
    private lateinit var iv_test_t: SmallImageView
    private lateinit var seek_bar: AppCompatSeekBar

    //    private lateinit var rg_test: RadioGroup
//    private lateinit var rg_padding: RadioGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        iv_test_t = findViewById(R.id.iv_test_t)
        seek_bar = findViewById(R.id.seek_bar)

        seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
               if(p2){
                   iv_test_t.setRadius(dpToPx(100 * (p1/100F)))
               }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }


    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }
}