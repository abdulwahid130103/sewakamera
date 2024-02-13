package com.polinema.sewakamera.View.Auth

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var b : ActivityRegisterBinding
    private lateinit var progressDialog: ProgressDialog


    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        progressDialog = ProgressDialog(this)

        textAutoCheck()

        b.signInTvSignUpPage.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        b.signUpBtnSignUpPage.setOnClickListener {
            checkInput()

        }
    }

    private fun textAutoCheck() {

        b.nameEtSignUpPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.nameEtSignUpPage.text.isEmpty()){
                    b.nameEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (b.nameEtSignUpPage.text.length >= 1){
                    b.nameEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                    b.fullnameError.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.nameEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (count >= 4){

                    b.nameEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                    b.fullnameError.visibility = View.GONE
                }
            }
        })

        b.emailEtSignUpPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.emailEtSignUpPage.text.isEmpty()){
                    b.emailEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (b.emailEtSignUpPage.text.matches(emailPattern.toRegex())) {
                    b.emailEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.emailEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (b.emailEtSignUpPage.text.matches(emailPattern.toRegex())) {
                    b.emailError.visibility = View.GONE
                    b.emailEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }
        })

        b.PassEtSignUpPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.PassEtSignUpPage.text.isEmpty()){
                    b.PassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (b.PassEtSignUpPage.text.length >= 8){
                    b.passError.visibility = View.GONE
                    b.PassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.PassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (count > 8){
                    b.passError.visibility = View.GONE
                    b.PassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }
        })

        b.cPassEtSignUpPage.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (b.cPassEtSignUpPage.text.isEmpty()){
                    b.cPassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                }
                else if (b.cPassEtSignUpPage.text.length < 8){
                    b.cPassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                }else if ((b.cPassEtSignUpPage.text.length >= 8) && (b.cPassEtSignUpPage.text.toString().equals(b.PassEtSignUpPage.text.toString()))){
                    b.cpassError.visibility = View.GONE
                    b.cPassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {

                b.cPassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if ((b.cPassEtSignUpPage.text.length >= 8) && (b.cPassEtSignUpPage.text.toString().equals(b.PassEtSignUpPage.text.toString()))){
                    b.cpassError.visibility = View.GONE
                    b.cPassEtSignUpPage.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(applicationContext,
                        R.drawable.ic_check
                    ), null)
                }
            }
        })

    }

    private fun checkInput() {
        if (b.nameEtSignUpPage.text.isEmpty()){
            b.fullnameError.visibility = View.VISIBLE
            b.fullnameError.text = "Nama tidak boleh kosong!"
            return
        }
        if (b.emailEtSignUpPage.text.isEmpty()){
            b.emailError.visibility = View.VISIBLE
            b.emailError.text = "Email tidak boleh kosong!"
            return
        }

        if (!b.emailEtSignUpPage.text.matches(emailPattern.toRegex())) {
            b.emailError.visibility = View.VISIBLE
            b.emailError.text = "Email tidak valid!"
            return
        }
        if(b.PassEtSignUpPage.text.isEmpty()){
            b.passError.visibility = View.VISIBLE
            b.passError.text = "Password tidak boleh kosong!"
            return
        }
        if(b.PassEtSignUpPage.text.length < 8){
            b.passError.visibility = View.VISIBLE
            b.passError.text = "Minimal 8 digit"
            return
        }
        if(b.cPassEtSignUpPage.text.isEmpty()){
            b.cpassError.visibility = View.VISIBLE
            b.cpassError.text = "Password tidak boleh kosong!"
            return
        }
        if(b.cPassEtSignUpPage.text.length < 8){
            b.cpassError.visibility = View.VISIBLE
            b.cpassError.text = "Minimal 8 digit"
            return
        }
        if (b.PassEtSignUpPage.text.toString() != b.cPassEtSignUpPage.text.toString()){
            b.cpassError.visibility = View.VISIBLE
            b.cpassError.text = "Password tidak cocok!"
            return
        }


//        signIn()
        Toast.makeText(this,"Sukses Register", Toast.LENGTH_LONG).show()


    }



}