package com.night.smallimageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import com.night.image.SmallImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<SmallImageView>(R.id.iv_1).setRadius(dpToPx(30))
        findViewById<SmallImageView>(R.id.iv_2).setRadius(dpToPx(30),0F,0F,dpToPx(30))
        findViewById<SmallImageView>(R.id.iv_3).setRadius(0F,dpToPx(30),dpToPx(30),0F)
        findViewById<SmallImageView>(R.id.iv_4).setRadius(dpToPx(7),dpToPx(14),dpToPx(21),dpToPx(28))
    }


    private fun dpToPx(dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
    }
}