package com.example.plant.main_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.BuildConfig
import com.example.plant.MainActivity
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.databinding.FragmentMapFinderBinding

class MapFinderFragment : Fragment(), FragmentListener {
    var mainActivity: MainActivity = MainActivity()
    private var _binding: FragmentMapFinderBinding? = null
    private val binding get() = _binding!!
    private lateinit var mFragmentListener: FragmentListener

    //출발지, 목적지 editText, rv
    lateinit var edtStartPoint: EditText
    lateinit var rvstart: RecyclerView
    lateinit var edtGoalPoint: EditText
    lateinit var rvgoal: RecyclerView

    //리사이클러뷰
    private val SEARCH_CLIENT_ID = "c8hh8dsrqnsuh3wDLvzi"
    private val SEARCH_SECRET_KEY = BuildConfig.SEARCH_SECRET_KEY
    var recyclerViewAdapter: RecyclerViewAdapter = RecyclerViewAdapter(mainActivity)
    val mdatas = mutableListOf<RecyclerViewData>()
    lateinit var text: String


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapFinderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    val mdates = mutableListOf<RecyclerViewData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
//            mFragmentListener.onReceivedData("Child -> Parent")
            (parentFragment as HomeFragment).hideMapFinder(true)
        }
        binding.btnMapFinder.setOnClickListener {
            (parentFragment as HomeFragment).pathFinder(
                mainActivity.startCoord,
                mainActivity.goalCoord
            )
            (parentFragment as HomeFragment).hideMapFinder(true)
            (parentFragment as HomeFragment).hideCameraAct(false)
        }

        //출발지, 도착지 setOnFocuseChangeListener로 포커스 될때 리사이클러뷰 나오게 설정 완료
        binding.edtStartPoint.setOnFocusChangeListener(object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    mainActivity.hideRecyclerView(binding.rvStart, false)
                    recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
                    binding.rvStart.adapter = recyclerViewAdapter
                    mainActivity.targetRecyclerView = binding.rvStart
                    mainActivity.targetActivity = mainActivity
                    mainActivity.locationTextWatcher(binding.edtStartPoint)

                    mainActivity.editText = binding.edtStartPoint
                    edtStartPoint = binding.edtStartPoint
                    rvstart = binding.rvStart

                } else {
                    mainActivity.hideRecyclerView(binding.rvStart, true)
                }
            }
        })

        binding.edtGoalPoint.setOnFocusChangeListener(object : OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    mainActivity.hideRecyclerView(binding.rvGoal, false)
                    recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
                    binding.rvGoal.adapter = recyclerViewAdapter
                    mainActivity.targetRecyclerView = binding.rvGoal
                    mainActivity.locationTextWatcher(binding.edtGoalPoint)

                    mainActivity.editText = binding.edtGoalPoint
                    edtGoalPoint = binding.edtGoalPoint
                    rvgoal = binding.rvGoal
                } else {
                    mainActivity.hideRecyclerView(binding.rvGoal, true)
                }
            }
        })


    }


    override fun onReceivedData(data: String) {
//        tvm.text = data

    }


}

