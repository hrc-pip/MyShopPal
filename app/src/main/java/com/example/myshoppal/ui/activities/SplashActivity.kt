package com.example.myshoppal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            if(FirestoreClass().getCurrentUserID().isNotEmpty()){
                startActivity(Intent(this,DashboardActivity::class.java))
            }else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            }, 1500)

    }
}