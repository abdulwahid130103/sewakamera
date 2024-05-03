package com.polinema.sewakamera.View.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.polinema.sewakamera.databinding.ActivityAfterPaymentBinding

class AfterPaymentActivity : AppCompatActivity() {
    lateinit var b : ActivityAfterPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityAfterPaymentBinding.inflate(layoutInflater)
        setContentView(b.root)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, PesananActivity::class.java))
            finish()
        }, 4000)

        b.buttonDone.setOnClickListener{
            val intent = Intent(this, PesananActivity::class.java)
            startActivity(intent)
        }
    }
}