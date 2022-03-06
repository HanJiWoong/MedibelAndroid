package com.exs.medivelskinmeasure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.exs.medivelskinmeasure.UI.Member.LoginActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val intent = Intent(this@StartActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_do_not_move)


    }
}