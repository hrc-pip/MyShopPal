package com.example.myshoppal.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.Product
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_my_product_details.*
import java.io.IOException

class MyProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private var mSelectedImageUri: Uri? = null
    private var mProductDetailImageURL: String = ""
    private lateinit var mProduct:Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        getProductDetails()

        iv_my_product_detail_image.setOnClickListener(this)
        btn_edit_my_product.setOnClickListener(this)
    }

    private fun setupActionBar() {

        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_my_product_details_activity)

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

    private fun getProductDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getProductDetails(this@MyProductDetailsActivity, mProductId)
    }

    fun productDetailsSuccess(product: Product) {

        // Hide Progress dialog.
        hideProgressDialog()

        mProduct = product

        // Populate the product details in the UI.
        GlideLoader(this@MyProductDetailsActivity).loadProductPicture(
            product.image,
            iv_my_product_detail_image
        )

        et_my_product_details_title.setText(product.title)
        et_my_product_details_price.setText(product.price)
        et_my_product_details_description.setText(product.description)
        et_my_product_details_stock_quantity.setText(product.stock_quantity)

        iv_my_product_detail_image.setOnClickListener(this@MyProductDetailsActivity)
        btn_edit_my_product.setOnClickListener(this@MyProductDetailsActivity)
    }

    override fun onClick(v: View?) {
        if ( v != null ) {
            when (v.id) {
                R.id.iv_my_product_detail_image -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooser(this@MyProductDetailsActivity)
                    } else {

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )

                    }

                }

                R.id.btn_edit_my_product -> {

                    if (validateProductDetails()) {

                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageUri != null){
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageUri, Constants.PRODUCT_IMAGE)
                        } else {
                            updateProductDetails()
                        }
                    }
                }
            }
        }
    }


    private fun validateProductDetails(): Boolean{
        return when {

            TextUtils.isEmpty(et_my_product_details_title.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }

            TextUtils.isEmpty(et_my_product_details_price.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }

            TextUtils.isEmpty(et_my_product_details_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_description),
                    true
                )
                false
            }

            TextUtils.isEmpty(et_my_product_details_stock_quantity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_product_quantity),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {

        mProductDetailImageURL = imageURL
        updateProductDetails()
    }

    private fun updateProductDetails(){
        val productHashMap = HashMap<String, Any>()

        val title = et_my_product_details_title.text.toString().trim { it <= ' ' }
        if(title != mProduct.title) {
            productHashMap[Constants.TITLE] = title
        }

        val description = et_my_product_details_description.text.toString().trim { it <= ' ' }
        if(description != mProduct.description) {
            productHashMap[Constants.DESCRIPTION] = description
        }

        val price = et_my_product_details_price.text.toString().trim { it <= ' ' }
        if(price != mProduct.price) {
            productHashMap[Constants.PRICE] = price
        }

        val stock = et_my_product_details_stock_quantity.text.toString().trim { it <= ' ' }
        if(stock != mProduct.stock_quantity) {
            productHashMap[Constants.STOCK_QUANTITY] = stock
        }

        if(mProductDetailImageURL.isNotEmpty()) {
            productHashMap[Constants.IMAGE] = mProductDetailImageURL
        }




        FirestoreClass().updateProductDetails(this@MyProductDetailsActivity, mProductId, productHashMap)
    }

    fun productDetailUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@MyProductDetailsActivity,
            resources.getString(R.string.msg_product_update_success),
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this@MyProductDetailsActivity)

            } else {
                //Display another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageUri = data.data!!

                        GlideLoader(this).loadUserPicture(mSelectedImageUri!!, iv_my_product_detail_image)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@MyProductDetailsActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {

            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }


}