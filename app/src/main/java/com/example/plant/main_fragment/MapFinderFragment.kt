package com.example.plant.main_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.example.plant.databinding.FragmentMapFinderBinding
import org.jetbrains.anko.find

class MapFinderFragment : Fragment(), FragmentListener {

    private var _binding: FragmentMapFinderBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFragmentListener: FragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapFinderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            mFragmentListener = parentFragment as HomeFragment
//            mFragmentListener.onReceivedData("Child -> Parent")
            (parentFragment as HomeFragment).hideMapFinder(true)
        }
    }
    override fun onReceivedData(data: String) {
//        tvm.text = data
    }
}