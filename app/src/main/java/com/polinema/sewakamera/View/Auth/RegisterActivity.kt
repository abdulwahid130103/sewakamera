package com.polinema.sewakamera.View.Auth

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.MediaHelper
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.R
import com.polinema.sewakamera.databinding.ActivityRegisterBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var b : ActivityRegisterBinding
    private lateinit var progressDialog: ProgressDialog
    lateinit var mediaHelper : MediaHelper

    lateinit var uriP: Uri
    lateinit var uriK: Uri
    lateinit var helper : DataHelper
    var url_p = ""
    var url_k = ""
    var namafile_k = ""
    var namafile_p = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        progressDialog = ProgressDialog(this)
        helper = DataHelper(this)
        mediaHelper = MediaHelper(this)

        b.signInTvSignUpPage.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        try {
            val m = StrictMode::class.java
                .getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.btnFile.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHelper.getRcProfil())

        }

        b.insTgl.setOnClickListener {
            showDatePickerDialog()
        }

        b.btnFileKtp.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHelper.getRcKtp())
        }


        b.signUpBtnSignUpPage.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peringatan!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin membuat akun Baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    if(cekDataKosong()){
                        if (
                            (b.insPasswordConfirm.text.toString() == b.insPassword.text.toString()) &&
                            (b.insPasswordConfirm.text.toString() != "") &&
                            (b.insPassword.text.toString() != "")
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                b.insTgl.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHelper.getRcProfil()){
                url_p = mediaHelper.getBitmapToStringGall(data!!.data!!, b.imgFotoProfil)
            }
            else if(requestCode == mediaHelper.getRcKtp()){
                url_k = mediaHelper.getBitmapToStringGall(data!!.data!!, b.imgFotoKtp)
            }
        }
    }

    private fun cekDataKosong() : Boolean{
        if(
            b.insNama.text.toString() == "" ||
            b.insEmail.text.toString() == "" ||
            b.insNik.text.toString() == "" ||
            b.insNoHpAkun.text.toString() == "" ||
            b.insTempat.text.toString() == "" ||
            b.insTgl.text.toString() == "" ||
            b.insAlamt.text.toString() == "" ||
            b.insPassword.text.toString() == "" ||
            url_p == "" ||
            url_k == ""
        ){
            return false
        }

        return true

    }

    private fun resetData(){
        url_p = ""
        url_k = ""
        namafile_k = ""
        namafile_p = ""
        b.insNama.setText("")
        b.insEmail.setText("")
        b.insNik.setText("")
        b.insNoHpAkun.setText("")
        b.insTempat.setText("")
        b.insTgl.setText("")
        b.insAlamt.setText("")
        b.insPassword.setText("")
        b.insPasswordConfirm.setText("")
        Glide.with(this)
            .load(R.drawable.image)
            .into(b.imgFotoProfil)

        Glide.with(this)
            .load(R.drawable.image)
            .into(b.imgFotoKtp)
    }

    private fun performRegistration() {
        val nmFile =
            "FP"+ SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).
            format(Date())+".jpg"
        namafile_p = nmFile

        val nmFile2 =
            "FKTP"+ SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).
            format(Date())+".jpg"
        namafile_k = nmFile2
        val data = HashMap<String, String>()
        data["name"] = b.insNama.text.toString()
        data["username"] = b.insNama.text.toString()
        data["email"] =  b.insEmail.text.toString()
        data["ktp"] =  b.insNik.text.toString()
        data["phone_number"] = b.insNoHpAkun.text.toString()
        data["tempat_lahir"] = b.insTempat.text.toString()
        data["tanggal_lahir"] = b.insTgl.text.toString()
        data["alamat"] = b.insAlamt.text.toString()
        data["password"] = b.insPassword.text.toString()
        data["image"] =url_p
        data["file_image"] =namafile_p
        data["ktp_image"] = url_k
        data["file_ktp_image"] = namafile_k
        registerUser(data)
    }

    private fun registerUser(datas: HashMap<String, String>) {

        val url = Connection()
        val request = object : StringRequest(
            Method.POST,url.url_register, Response.Listener { response -> //HERE
                val jsonObject = JSONObject(response)
                val status = jsonObject.getInt("status")
                val message = jsonObject.getString("message")

                if(status == 1){
                    helper.toast_custom_alert(this, "success",message)
                    resetData()
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
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