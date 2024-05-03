package com.polinema.sewakamera.View.Activity

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
import com.polinema.sewakamera.databinding.ActivityPesananBinding

class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val tb = findViewById<TabLayout>(R.id.tabLayout)

        val fragmentAdapter = PesananAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(BookingFragment(),"Booking")
        fragmentAdapter.addFragment(TerverifikasiFragment(),"Terverifikasi")
        fragmentAdapter.addFragment(DibayarFragment(),"Lunas")
        fragmentAdapter.addFragment(DipinjamFragment(),"Disewa")
        fragmentAdapter.addFragment(SelesaiFragment(),"Selesai")
        viewPager.adapter = fragmentAdapter
        tb.setupWithViewPager(viewPager)
    }
}
