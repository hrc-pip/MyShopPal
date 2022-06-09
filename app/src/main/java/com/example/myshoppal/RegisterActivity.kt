package com.example.myshoppal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toolbar

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tvLogin = findViewById<TextView>(R.id.tv_login)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        tvLogin.setOnClickListener {
            // Do some work here


            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)

            startActivity(intent)

            finish()
        }

    }

    private fun setupActionBar() {

        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register_activity)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}