package com.polinema.sewakamera.View.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.databinding.ActivityUpdatePasswordBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpdatePasswordActivity : AppCompatActivity() {

    lateinit var b : ActivityUpdatePasswordBinding
    lateinit var helper : DataHelper
    private lateinit var Session: SessionSewa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(b.root)
        helper = DataHelper(this)
        Session = SessionSewa(this)


        b.btnUpdatePassword.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peringatan!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin menganti password?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    if(cekDataKosong()){
                        if (
                            (b.passwordKonfirmasi.text.toString() == b.password.text.toString()) &&
                            (b.passwordKonfirmasi.text.toString() != "") &&
                            (b.password.text.toString() != "")
                        ) {
                            performRegistration()
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("Peringatan!")
                                .setIcon(R.drawable.warning)
                                .setMessage("Tolong masukkan password dengan benar?")
                                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                    null
                                })
                                .show()
                        }
                    }else{
                        AlertDialog.Builder(this)
                            .setTitle("Peringatan!")
                            .setIcon(R.drawable.warning)
                            .setMessage("Data tidak boleh kosong?")
                            .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                                null
                            })
                            .show()
                    }
                })
                .show()
        }
    }

    private fun cekDataKosong() : Boolean{
        if(
            b.password.text.toString() == "" ||
            b.passwordKonfirmasi.text.toString() == ""
        ){
            return false
        }

        return true

    }

    private fun resetData(){
        b.password.setText("")
        b.passwordKonfirmasi.setText("")
    }
    private fun performRegistration() {
        val data = HashMap<String, String>()
        data["id"] = Session.getUserId().toString()
        data["password"] = b.password.text.toString()
        registerUser(data)
    }

    private fun registerUser(datas: HashMap<String, String>) {

        val url = Connection()
        val request = object : StringRequest(
            Method.POST,url.url_update_password, Response.Listener { response -> //HERE
                val jsonObject = JSONObject(response)
                val status = jsonObject.getInt("status")
                val message = jsonObject.getString("message")

                if(status == 1){
                    helper.toast_custom_alert(this, "success",message)
                    resetData()
                }else{
                    helper.toast_custom_alert(this, "error",message)
                }

            },
            Response.ErrorListener { error -> //HERE
                Toast.makeText(this,"Tidak dapat terhubung ke server",
                    Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                return datas
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}