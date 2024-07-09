package com.polinema.sewakamera.View.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.polinema.sewakamera.R
import com.polinema.sewakamera.View.Fragment.BookingFragment
import com.polinema.sewakamera.View.Fragment.DibayarFragment
import com.polinema.sewakamera.View.Fragment.DipinjamFragment
import com.polinema.sewakamera.View.Fragment.SelesaiFragment
import com.polinema.sewakamera.View.Fragment.TerverifikasiFragment
import com.polinema.sewakamera.Adapter.PesananAdapter
import com.polinema.sewakamera.Helper.DataHelper
import com.polinema.sewakamera.Helper.SessionSewa
import com.polinema.sewakamera.View.Auth.LoginActivity
import com.polinema.sewakamera.View.Fragment.ExpiredFragment
import com.polinema.sewakamera.View.Fragment.LunasFragment
import com.polinema.sewakamera.View.Fragment.TolakFragment
import com.polinema.sewakamera.databinding.ActivityPesananBinding

class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    private lateinit var Session: SessionSewa
    lateinit var dataHelper : DataHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Session = SessionSewa(this)
        dataHelper = DataHelper(this)
        cekLogin()
    }

    private fun cekLogin(){
        if (!Session.getLogin()) {
            dataHelper.toast_custom_alert(this, "notifikasi","Anda harus login untuk melihat detail transaksi")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{

            val viewPager = findViewById<ViewPager>(R.id.viewPager)
            val tb = findViewById<TabLayout>(R.id.tabLayout)
            val fragmentAdapter = PesananAdapter(supportFragmentManager)
            fragmentAdapter.addFragment(BookingFragment(),"Booking")
            fragmentAdapter.addFragment(TerverifikasiFragment(),"Terverifikasi")
            fragmentAdapter.addFragment(DibayarFragment(),"Bayar")
            fragmentAdapter.addFragment(LunasFragment(),"Lunas")
            fragmentAdapter.addFragment(DipinjamFragment(),"Disewa")
            fragmentAdapter.addFragment(SelesaiFragment(),"Selesai")
            fragmentAdapter.addFragment(ExpiredFragment(),"Kadaluarsa")
            fragmentAdapter.addFragment(TolakFragment(),"Ditolak")
            viewPager.adapter = fragmentAdapter
            tb.setupWithViewPager(viewPager)

            binding.backDetailTransaksiContainer.setOnClickListener{
                finish()
            }
        }
    }
}
