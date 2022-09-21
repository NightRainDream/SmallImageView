package com.night.smallimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.RadioGroup
import com.night.image.SmallImageView

class TestActivity : AppCompatActivity() {
    private lateinit var iv_test_t: SmallImageView
    private lateinit var rg_test: RadioGroup
    private lateinit var rg_padding: RadioGroup
    private val mHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        iv_test_t = findViewById(R.id.iv_test_t)
        rg_test = findViewById(R.id.rg_test)
        rg_padding = findViewById(R.id.rg_padding)
        rg_test.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_1 ->{
                    iv_test_t.setRadius(dpToPx(15),0F,0F,0F)
                }
                R.id.rb_2 ->{
                    iv_test_t.setRadius(0F,dpToPx(15),0F,0F)
                }
                R.id.rb_3 ->{
                    iv_test_t.setRadius(0F,0F,0F,dpToPx(15))
                }
                R.id.rb_4 ->{
                    iv_test_t.setRadius(0F,0F,dpToPx(15),0F)
                }
            }
        }

        rg_padding.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_padding_1 ->{
                    iv_test_t.setPadding(dpToPx(15).toInt(),0,0,0)
                }
                R.id.rb_padding_2 ->{
                    iv_test_t.setPadding(0,dpToPx(15).toInt(),0,0)
                }
                R.id.rb_padding_3 ->{
                    iv_test_t.setPadding(0,0,0,dpToPx(15).toInt())
                }
                R.id.rb_padding_4 ->{
                    iv_test_t.setPadding(0,0,dpToPx(15).toInt(),0)
                }
            }
        }

    }


    private fun dpToPx(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }
}