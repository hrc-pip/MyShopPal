package com.example.myshoppal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatCheckBox

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tvLogin = findViewById<TextView>(R.id.tv_login)
        val register = findViewById<Button>(R.id.btn_register)


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

        register.setOnClickListener(){
            validateRegisterDetails()
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


    private fun validateRegisterDetails(): Boolean {
        val firstName = findViewById<TextView>(R.id.et_first_name)
        val lastName = findViewById<TextView>(R.id.et_last_name)
        val email = findViewById<TextView>(R.id.et_email)
        val password = findViewById<TextView>(R.id.et_password)
        val confirmPassword = findViewById<TextView>(R.id.et_confirm_password)
        val terms = findViewById<AppCompatCheckBox>(R.id.cb_terms_and_condition)


        return when {
            TextUtils.isEmpty(firstName.text.toString().trim { it <= ' '}) || firstName.length() < 3 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(lastName.text.toString().trim { it <= ' '}) || lastName.length() <= 3 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) || password.length() < 8 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(confirmPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            password.text.toString().trim { it <= ' '} != confirmPassword.text.toString().trim { it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            !terms.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }

            else -> {
                showErrorSnackBar(  "Thanks for registering!", false)
                true
            }
        }
    }

}