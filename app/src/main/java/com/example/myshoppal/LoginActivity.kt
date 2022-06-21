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
import com.example.myshoppal.utils.Constants

import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


       tv_register.setOnClickListener(){
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener(){
            logInRegisteredUser()
        }

        tv_forgot_password.setOnClickListener(){
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }


    fun userLoggedInSuccess(user: User) {

        // hide the progress dialog
        hideProgressDialog()
        

        //TODO - Mudar a opção do userProfile para um hamburger menu, pois desse jeito todo user novo vai direto para o Profile
        // Start
        if (user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
        // End
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

        if(validateLoginDetails()) {

            // show progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

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