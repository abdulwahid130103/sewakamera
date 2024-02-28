package com.polinema.sewakamera.View.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.polinema.sewakamera.Helper.LoadingDialog
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    lateinit var loadingDialog: LoadingDialog

    private lateinit var b : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        textAutoCheck()

        loadingDialog = LoadingDialog(this)

        b.signUpTv.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        b.loginBtn.setOnClickListener {
//            checkInput()
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
    }

    private fun textAutoCheck() {



        b.emailEt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.emailEt.text.isEmpty()){
                    b.emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (Patterns.EMAIL_ADDRESS.matcher(b.emailEt.text).matches()) {
                    b.emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                    b.emailError.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (Patterns.EMAIL_ADDRESS.matcher(b.emailEt.text).matches()) {
                    b.emailEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                    b.emailError.visibility = View.GONE
                }
            }
        })

        b.PassEt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.PassEt.text.isEmpty()){
                    b.PassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (b.PassEt.text.length > 4){
                    b.PassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.PassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                b.passwordError.visibility = View.GONE
                if (count > 4){
                    b.PassEt.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)

                }
            }
        })



    }

    private fun checkInput() {

        if (b.emailEt.text.isEmpty()){
            b.emailError.visibility = View.VISIBLE
            b.emailError.text = "Email tidak boleh kosong!"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(b.emailEt.text).matches()) {
            b.emailError.visibility = View.VISIBLE
            b.emailError.text = "Email tidak valid!"
            return
        }
        if(b.PassEt.text.isEmpty()){
            b.passwordError.visibility = View.VISIBLE
            b.passwordError.text = "Password tidak boleh kosong !"
            return
        }

        if(b.PassEt.text.length < 8){
            b.passwordError.visibility = View.VISIBLE
            b.passwordError.text = "Minimal 8 digit"
            return
        }

        if ( b.PassEt.text.isNotEmpty() && b.emailEt.text.isNotEmpty()){
            b.emailError.visibility = View.GONE
            b.passwordError.visibility = View.GONE
//            signInUser()
            Toast.makeText(this,"Sukses login",Toast.LENGTH_LONG).show()
        }
    }


//    private fun signInUser() {
//
//
//    }
}
