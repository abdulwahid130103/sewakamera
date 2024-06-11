package com.polinema.sewakamera.Helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale

class MediaHelper(context : Context) {

    val context =context
    var namaFile=""
    var fileUri = Uri.parse("")


    companion object{
        const val REQ_CODE_GALLERY = 100
        const val RC_CAMERA = 200
    }
    fun getMyFileName() : String{
        return this.namaFile
    }

    fun getRcProfil() : Int{
        return RC_CAMERA
    }


    fun getRcKtp() : Int{
        return REQ_CODE_GALLERY
    }

    //2. buat direktori bila belum ada
    fun getOutputMediaFile() : File {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Appx11Laravel")
        if(!mediaStorageDir.exists()) //cek apakah direktori belum ada
            if(!mediaStorageDir.mkdirs()){ //coba buat direktori
                Log.e("mkdir","Gagal membuat direktori")
            }
        val mediaFile = File(mediaStorageDir.path+ File.separator+
                "${this.namaFile}")
        return mediaFile
    }
    //1. siapkan nama file
    fun getOutputMediaFileUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss",
            Locale.getDefault()).format(Date())
        this.namaFile = "DC_${timeStamp}.jpg"
        this.fileUri = Uri.fromFile(getOutputMediaFile())
        Log.i("fileUri",this.fileUri.path.toString())
        return this.fileUri
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun bitmapToString(bmp : Bitmap) : String{
        val outputStream = ByteArrayOutputStream()
        //kompresi gambar dengan menurunkan kualitas gambar menjadi
        //tinggal 60% dari aslinya
        bmp.compress(Bitmap.CompressFormat.JPEG, 60,outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBitmapToStringGall(uri : Uri, imv : ImageView) : String{
        var bmp = MediaStore.Images.Media.getBitmap(this.context.contentResolver,uri)
        var dim = 720
        if(bmp.height > bmp.width){
            bmp = Bitmap.createScaledBitmap(bmp,
                (bmp.width*dim).div(bmp.height),dim,true)
        }else{
            bmp = Bitmap.createScaledBitmap(bmp,
                dim, (bmp.height*dim).div(bmp.width),true)
        }
        imv.setImageBitmap(bmp)
        return bitmapToString(bmp)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBitmapToString(imv : ImageView) : String{
        //var bmp = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
        var bmp = BitmapFactory.decodeFile(this.fileUri.path)
        val dim = 720
        if(bmp.height > bmp.width){
            bmp = Bitmap.createScaledBitmap(bmp,
                (bmp.width*dim).div(bmp.height),dim,true)
        }else{
            bmp = Bitmap.createScaledBitmap(bmp,
                dim, (bmp.height*dim).div(bmp.width),true)
        }
        imv.setImageBitmap(bmp)
        return bitmapToString(bmp)
    }
}