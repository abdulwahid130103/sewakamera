package com.polinema.sewakamera.View.Activity

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
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.Model.Connection
import com.polinema.sewakamera.Model.Produk
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.databinding.ActivityUpdateProfileBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var b : ActivityUpdateProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var session : SessionSewa

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
        b = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(b.root)

        session = SessionSewa(this)
        getProfile()

        progressDialog = ProgressDialog(this)
        helper = DataHelper(this)
        mediaHelper = MediaHelper(this)

        try {
            val m = StrictMode::class.java
                .getMethod("disableDeathOnFileUriExposure")
            m.invoke(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        b.tglUpdateProfile.setOnClickListener {
            showDatePickerDialog()
        }

        b.btnFileUpdateProfile.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHelper.getRcProfil())

        }

        b.btnFileKtpUpdateProfile.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,mediaHelper.getRcKtp())
        }

        b.btnUpdateProfile.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Peringatan!")
                .setIcon(R.drawable.warning)
                .setMessage("Apakah Anda ingin update profile?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this,url_k.toString(),Toast.LENGTH_LONG).show()
                    if(cekDataKosong()){
                        performUpdateProfile()
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
            b.namaUpdateProfile.text.toString() == "" ||
            b.emailUpdateProfile.text.toString() == "" ||
            b.nikUpdateProfile.text.toString() == "" ||
            b.noHpUpdateProfile.text.toString() == "" ||
            b.tempatUpdateProfile.text.toString() == "" ||
            b.tglUpdateProfile.text.toString() == "" ||
            b.alamatUpdateProfile.text.toString() == ""
        ){
            return false
        }

        return true

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHelper.getRcProfil()){
                url_p = mediaHelper.getBitmapToStringGall(data!!.data!!, b.imgFotoProfilUpdateProfile)
            }
            else if(requestCode == mediaHelper.getRcKtp()){
                url_k = mediaHelper.getBitmapToStringGall(data!!.data!!, b.imgFotoKtpUpdateProfile)
            }
        }
    }

    private fun resetData(){
        url_p = ""
        url_k = ""
        namafile_k = ""
        namafile_p = ""
        b.namaUpdateProfile.setText("")
        b.emailUpdateProfile.setText("")
        b.nikUpdateProfile.setText("")
        b.noHpUpdateProfile.setText("")
        b.tempatUpdateProfile.setText("")
        b.tglUpdateProfile.setText("")
        b.alamatUpdateProfile.setText("")
        Glide.with(this)
            .load(R.drawable.image)
            .into(b.imgFotoProfilUpdateProfile)

        Glide.with(this)
            .load(R.drawable.image)
            .into(b.imgFotoKtpUpdateProfile)
    }

    private fun performUpdateProfile() {

        val data = HashMap<String, String>()
        if(namafile_p == null || namafile_p == ""){
            val nmFile =
                "FP"+ SimpleDateFormat("yyyyMMddHHmmss",
                    Locale.getDefault()).
                format(Date())+".jpg"
            namafile_p = nmFile
            data["image"] =url_p
            data["file_image"] =namafile_p
        }
        if(namafile_k == null || namafile_p == ""){
            val nmFile2 =
                "FKTP"+ SimpleDateFormat("yyyyMMddHHmmss",
                    Locale.getDefault()).
                format(Date())+".jpg"
            namafile_k = nmFile2

            data["ktp_image"] = url_k
            data["file_ktp_image"] = namafile_k
        }
        data["id"] = session.getUserId().toString()
        data["name"] = b.namaUpdateProfile.text.toString()
        data["username"] = b.namaUpdateProfile.text.toString()
        data["email"] =  b.emailUpdateProfile.text.toString()
        data["ktp"] =  b.nikUpdateProfile.text.toString()
        data["phone_number"] = b.noHpUpdateProfile.text.toString()
        data["tempat_lahir"] = b.tempatUpdateProfile.text.toString()
        data["tanggal_lahir"] = b.tglUpdateProfile.text.toString()
        data["alamat"] = b.alamatUpdateProfile.text.toString()
        updateUser(data)
    }

    private fun updateUser(datas: HashMap<String, String>) {

        val url = Connection()
        val request = object : StringRequest(
            Method.POST,url.update_profile, Response.Listener { response -> //HERE
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                b.tglUpdateProfile.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    private fun getProfile(){
        val url = Connection()
        val url_fix = "${url.get_profile}/${session.getUserId()} "

        val stringRequest = object : StringRequest(
            Method.GET, url_fix,
            Response.Listener { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        if(jsonObject.getString("name")?.toString() != "null" && jsonObject.getString("name")?.toString() != null){
                            b.namaUpdateProfile.setText(jsonObject.getString("name"))
                        }
                        if(jsonObject.getString("email")?.toString() != "null" && jsonObject.getString("email")?.toString() != null){
                            b.emailUpdateProfile.setText(jsonObject.getString("email"))
                        }
                        if(jsonObject.getString("tempat_lahir")?.toString() != "null" && jsonObject.getString("tempat_lahir")?.toString() != null){
                            b.tempatUpdateProfile.setText(jsonObject.getString("tempat_lahir"))
                        }
                        if(jsonObject.getString("ktp")?.toString() != "null" && jsonObject.getString("ktp")?.toString() != null){
                            b.nikUpdateProfile.setText(jsonObject.getString("ktp"))
                        }
                        if(jsonObject.getString("tanggal_lahir")?.toString() != "null" && jsonObject.getString("tanggal_lahir")?.toString() != null){
                            b.tglUpdateProfile.setText(jsonObject.getString("tanggal_lahir"))
                        }
                        if(jsonObject.getString("phone_number")?.toString() != "null" && jsonObject.getString("phone_number")?.toString() != null){
                            b.noHpUpdateProfile.setText(jsonObject.getString("phone_number"))
                        }
                        if(jsonObject.getString("alamat")?.toString() != "null" && jsonObject.getString("alamat")?.toString() != null){
                            b.alamatUpdateProfile.setText(jsonObject.getString("alamat"))
                        }

                        if(
                            (jsonObject.getString("image_url")?.toString() != "null" && jsonObject.getString("image_url")?.toString() != null) &&
                            (jsonObject.getString("image")?.toString() != "null" && jsonObject.getString("image")?.toString() != null)
                        ){
                            url_p = jsonObject.getString("image_url")
                            namafile_p = jsonObject.getString("image")
                            Picasso.get().load(jsonObject.getString("image_url")).into(b.imgFotoProfilUpdateProfile)
                        } else {
                            url_p = ""
                        }

                        if(
                            (jsonObject.getString("ktp_image_url")?.toString() != "null" && jsonObject.getString("ktp_image_url")?.toString() != null) &&
                            (jsonObject.getString("ktp_image")?.toString() != "null" && jsonObject.getString("ktp_image")?.toString() != null)
                        ){
                            url_k = jsonObject.getString("ktp_image_url")
                            namafile_k = jsonObject.getString("ktp_image")
                            Picasso.get().load(jsonObject.getString("ktp_image_url")).into(b.imgFotoKtpUpdateProfile)
                        } else {
                            url_k = ""
                        }

                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, getString(R.string.error_message_data), Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    this, getString(R.string.error_message_server), Toast.LENGTH_LONG
                ).show()
            }) {}

        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }
}