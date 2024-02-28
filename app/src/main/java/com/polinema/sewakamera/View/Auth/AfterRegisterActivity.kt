package com.polinema.sewakamera.View.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityAfterRegisterBinding

class AfterRegisterActivity : AppCompatActivity() {

    private lateinit var b : ActivityAfterRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAfterRegisterBinding.inflate(layoutInflater);
        setContentView(b.root)


        b.btnBackHome.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}