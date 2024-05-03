package com.polinema.sewakamera.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polinema.sewakamera.View.Activity.PesananActivity
import com.polinema.sewakamera.databinding.FragmentDipinjamBinding

class DipinjamFragment : Fragment() {

    private lateinit var b : FragmentDipinjamBinding
    private lateinit var thisParent : PesananActivity
    private lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as PesananActivity
        b = FragmentDipinjamBinding.inflate(layoutInflater)
        v = b.root

        return v
    }

}