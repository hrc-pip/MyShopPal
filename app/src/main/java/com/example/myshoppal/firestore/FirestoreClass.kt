package com.example.myshoppal.firestore

import android.app.Activity
import android.util.Log
import com.example.myshoppal.LoginActivity
import com.example.myshoppal.RegisterActivity
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" collection name. If the collection is already created the then it will not create the same one again
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the Users ID.
            .document(userInfo.id)
            //Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of baseActivity for transferring the result to it
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserID if it is not null or else it will be blank
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data
        mFireStore.collection(Constants.USERS)
            // THe document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have the received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                when(activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                //Hide the progress dialog if there is any error. And print the error in log.
                when(activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user data",
                    e
                )
            }
    }
}