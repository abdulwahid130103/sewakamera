package com.polinema.sewakamera.View.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.polinema.sewakamera.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var b : ActivityRegisterBinding
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        progressDialog = ProgressDialog(this)

        b.signInTvSignUpPage.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        b.signUpBtnSignUpPage.setOnClickListener {
            intent = Intent(this, AfterRegisterActivity::class.java)
            startActivity(intent)
        }
    }



}