package com.polinema.sewakamera.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polinema.sewakamera.View.Activity.HomeActivity
import com.polinema.sewakamera.databinding.FragmentMitraBinding

class MitraFragment : Fragment() {

    private lateinit var b : FragmentMitraBinding
    private lateinit var thisParent : HomeActivity
    private lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as HomeActivity
        b = FragmentMitraBinding.inflate(layoutInflater)
        v = b.root

        return v
    }
}