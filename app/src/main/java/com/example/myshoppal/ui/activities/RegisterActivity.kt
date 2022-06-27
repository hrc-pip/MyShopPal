package com.example.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupActionBar()

        tv_login.setOnClickListener {
            // Do some work here
           onBackPressed()
        }

        btn_register.setOnClickListener(){
            registerUser()
        }

    }

    private fun setupActionBar() {

        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_register_activity)

        toolbar.title = ""

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }


    private fun validateRegisterDetails(): Boolean {
        val firstName = findViewById<EditText>(R.id.et_first_name)
        val lastName = findViewById<EditText>(R.id.et_last_name)
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val confirmPassword = findViewById<EditText>(R.id.et_confirm_password)
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
                true
            }
        }
    }



    private fun registerUser() {

        if(validateRegisterDetails()) {

            // show the progressBar
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = et_email.text.toString().trim { it <= ' '}
            val password: String = et_password.text.toString().trim { it <= ' '}

            //Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            //  Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim { it <= ' '},
                                et_last_name.text.toString().trim { it <= ' '},
                                email
                                )

                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            /*FirebaseAuth.getInstance().signOut();
                            */


                        } else {

                            hideProgressDialog()

                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}