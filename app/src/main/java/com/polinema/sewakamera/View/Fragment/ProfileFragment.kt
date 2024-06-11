package com.polinema.sewakamera.View.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.View.Activity.UlasanActivity
import com.polinema.sewakamera.View.Activity.UpdateProfileActivity
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.View.Auth.RegisterActivity
import com.polinema.sewakamera.databinding.FragmentMitraBinding
import com.polinema.sewakamera.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var b : FragmentProfileBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View

    private lateinit var Session: SessionSewa
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentProfileBinding.inflate(layoutInflater)
        v = b.root

        Session = SessionSewa(thisParent)

        cekLogin()
        b.pesananMenuProfileContainer.setOnClickListener(this)
        b.btnLoginProfile.setOnClickListener(this)
        b.btnRegisterProfile.setOnClickListener(this)
        b.btnLogoutContainer.setOnClickListener(this)
        b.ulasanProfileContainer.setOnClickListener(this)
        if(Session.getLogin()){
            b.updateProfileContainer.setOnClickListener(this)
        }



        return v
    }

    fun updateProfileSession(){

    }

    fun cekLogin(){
        if(Session.getLogin()){
            b.nameProfileContainer.visibility = View.VISIBLE
            b.buttonLoginRegisterContainer.visibility = View.GONE
            b.profileNameProfileFrag.text = Session.getUsername().toString()

            if(Session.getImage() != null || Session.getImage() != ""){
                Glide.with(this)
                    .load(Session.getImage())
                    .into(b.profileImageProfileFrag)
            }else{
                Glide.with(this)
                    .load(R.drawable.baseline_account_circle_24)
                    .into(b.profileImageProfileFrag)
            }
        }else{
            b.nameProfileContainer.visibility = View.GONE
            b.buttonLoginRegisterContainer.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        cekLogin()
        super.onStart()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.pesananMenuProfileContainer ->{
                val intent = Intent(thisParent, PesananActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_login_profile ->{
                val intent = Intent(thisParent, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_register_profile ->{
                val intent = Intent(thisParent, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_logout_container -> {
                Session.logoutUser()
                val intent = Intent(thisParent, HomeActivity::class.java)
                startActivity(intent)
            }
            R.id.ulasanProfileContainer -> {
                val intent = Intent(thisParent, UlasanActivity::class.java)
                startActivity(intent)
            }
            R.id.updateProfileContainer -> {
                val intent = Intent(thisParent, UpdateProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}