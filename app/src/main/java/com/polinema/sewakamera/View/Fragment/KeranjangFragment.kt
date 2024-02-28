package com.polinema.sewakamera.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentKeranjangBinding

class KeranjangFragment : Fragment() {

    private lateinit var b : FragmentKeranjangBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentKeranjangBinding.inflate(layoutInflater)
        v = b.root

        return v
    }

}