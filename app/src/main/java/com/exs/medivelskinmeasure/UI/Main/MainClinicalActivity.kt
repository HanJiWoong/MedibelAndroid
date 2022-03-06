package com.exs.medivelskinmeasure.UI.Main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.exs.medivelskinmeasure.Constants
import com.exs.medivelskinmeasure.MeasureMode
import com.exs.medivelskinmeasure.R
import com.exs.medivelskinmeasure.UI.Main.Component.CommonMainTop
import com.exs.medivelskinmeasure.UI.Measure.SkinMeasureActivity

class MainClinicalActivity : AppCompatActivity() {
    private lateinit var mViewMainTop: CommonMainTop

    private lateinit var mIBHuman: AppCompatButton
    private lateinit var mIBAnimal: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_clinical)

        initUI()
        setCommonListener()
    }

    override fun onResume() {
        super.onResume()

        Constants.measureMode = MeasureMode.Clinical
    }

    fun initUI() {
        mViewMainTop = findViewById(R.id.ViewMainClinicalTop)

        mIBHuman = findViewById(R.id.BtnMainClinicalHumanMeasureSkin)
        mIBAnimal = findViewById(R.id.BtnMainClinicalAnimalMeasureSkin)
    }

    fun setCommonListener() {
        mIBHuman.setOnClickListener {
            val intent = Intent(this@MainClinicalActivity, MainClinicalHumanActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }

        mIBAnimal.setOnClickListener {
            val intent = Intent(this@MainClinicalActivity, MainClinicalAnimalActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)
        }
    }
}