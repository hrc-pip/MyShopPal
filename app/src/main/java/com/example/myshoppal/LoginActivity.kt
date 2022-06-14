package com.example.myshoppal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult

import androidx.annotation.NonNull
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User

import com.google.android.gms.tasks.OnCompleteListener




class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        val register = findViewById<TextView>(R.id.tv_register)
        val login = findViewById<Button>(R.id.btn_login)
        val forgot = findViewById<TextView>(R.id.tv_forgot_password)

        register.setOnClickListener(){
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener(){
            logInRegisteredUser()
        }

        forgot.setOnClickListener(){
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }


    fun userLoggedInSuccess(user: User) {

        // hide the progress dialog
        hideProgressDialog()

        //Print the user details in the log as of now
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }


    private fun validateLoginDetails():Boolean {
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)

        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim { it <= ' '}).matches() -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_valid_email), true)
                false
            }



            TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) || password.length() < 8 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }


            password.length() < 8 -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_length), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        val emailT = findViewById<EditText>(R.id.et_email)
        val passwordT = findViewById<EditText>(R.id.et_password)

        if(validateLoginDetails()) {

            // show progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            val email = emailT.text.toString().trim { it <= ' ' }
            val password = passwordT.text.toString().trim { it <= ' ' }

            //Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->


                    if(task.isSuccessful) {

                       FirestoreClass().getUserDetails(this@LoginActivity)

                    } else {

                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)

                    }
                }
        }
    }
}