package com.polinema.sewakamera.View.Auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.polinema.sewakamera.Helper.LoadingDialog
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var loadingDialog: LoadingDialog
    private lateinit var Session: SessionSewa

    private lateinit var b : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)



        loadingDialog = LoadingDialog(this)
        Session = SessionSewa(this)
        checkLogin()
        b.btnLogin.setOnClickListener(this)
        b.btnRegister.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnLogin -> {
                authentication()
            }
            R.id.btnRegister -> {
                intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkLogin(){
        if (Session.getLogin()){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun authentication() {
        val url = Connection()

        val email = b.inputEmail.text.toString()
        val password = b.inputPassword.text.toString()

        val stringRequest = object : StringRequest(
            Method.POST, url.url_login,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val is_valid = jsonObject.optString("success")
                    val pesan = jsonObject.optString("message")


                    if (is_valid.toBoolean()) {
                        val data = jsonObject.getJSONObject("data")
                        val id = data.getInt("id")
                        val name = data.getString("name")
                        val image = data.getString("image")
                        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
                        Session.setLogin(true)
                        Session.setUsername(name.toString())
                        Session.setUserId(id.toString())
                        Session.setImage(image.toString())

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this,  pesan.toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this, "Tidak dapat terhubung ke server", Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}
