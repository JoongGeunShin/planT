package com.example.plant.main_fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.plant.MainActivity
import com.example.plant.NaverSearch.LocationSearchInterface
import com.example.plant.NaverSearch.LocationDTO
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.example.plant.pathfinder.NaverAPI
import com.example.plant.pathfinder.ResultPath
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//, PermissionListener
open class HomeFragment : Fragment(), FragmentListener, OnMapReadyCallback {

    lateinit var mainActivity: MainActivity

    // naver map
    private lateinit var naverMap: NaverMap
    private lateinit var mapFragment: MapFragment

    // 현재 위치 반환값
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource

    // binding
    private var _binding: FragmentBottomnviHomeBinding? = null
    private val binding get() = _binding!!

    // recycler view
    val SEARCH_CLIENT_ID = "c8hh8dsrqnsuh3wDLvzi"
    val SEARCH_SECRET_KEY = "VMmDTZuvfv"
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    var datas = mutableListOf<RecyclerViewData>()

    //pathFinder
    val PATHFINDER_CLIENT_ID = "u04wstprb6"
    val PATHFINDER_SECRET_KEY = "UTtsqS8xv7TxQzZcE9offwjuXfQ9LKUqJm9CZ7UW"

    //프래그먼트 이동
    private lateinit var mFragmentListener: FragmentListener
    private var fragmentChild = MapFinderFragment()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(com.example.plant.R.layout.fragment_bottomnvi_home, container, false)
        _binding = FragmentBottomnviHomeBinding.inflate(inflater, container, false)
        val view = binding.root
//        edt_searchLocation = view.findViewById(com.example.plant.R.id.edt_searchLocation)
//        rv_items = view.findViewById(com.example.plant.R.id.rv_items)

        //mapFinder 안보이게
        hideMapFinder(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = childFragmentManager
        //길찾기
        fm.beginTransaction().add(R.id.frameLayoutMapFinder, fragmentChild).commit()


        // 지도 객체 선언
        mapFragment = fm.findFragmentById(com.example.plant.R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(com.example.plant.R.id.map_fragment, it).commit()
            }
        // onMapReady 호출
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
//        binding.tvLocation.text = "안녕"


        binding.edtSearchLocation.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    mainActivity.hideRecyclerView(binding.rvItems, false)
                    recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
                    binding.rvItems.adapter = recyclerViewAdapter
                    mainActivity.targetRecyclerView = binding.rvItems
                    mainActivity.targetActivity = mainActivity
                    mainActivity.locationTextWatcher(binding.edtSearchLocation)

                } else {
                    mainActivity.hideRecyclerView(binding.rvItems, true)
                }
            }
        })

        binding.btnFindWay.setOnClickListener {
            mFragmentListener = MapFinderFragment()
            hideMapFinder(false)
//            mFragmentListener.onReceivedData("Parent -> Child")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(@NonNull naverMap: NaverMap) {
//        Toast.makeText(mainActivity, "onMapReadey 호출 완료", Toast.LENGTH_SHORT).show()
        this.naverMap = naverMap

        // uiSettings
        val uiSettings = naverMap.uiSettings

        // 현재 위치 관련 에러 계속 발생
        naverMap.locationSource = locationSource
        uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    // visible 설정
    fun hideMapFinder(state: Boolean) {
        if (state) binding.frameLayoutMapFinder.visibility =
            View.GONE else binding.frameLayoutMapFinder.visibility = View.VISIBLE
    }

    //tvParent.text = data -> 바꿔야함
    override fun onReceivedData(data: String) {
//tvParent.text = data
    }
}












