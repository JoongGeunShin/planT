package com.example.plant.main_fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.plant.MainActivity
import com.example.plant.NaverSearch.LocationSearchInterface
import com.example.plant.NaverSearch.LocationDTO
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//, PermissionListener
class HomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var mainActivity: MainActivity
    // naver map
    private lateinit var naverMap : NaverMap
    private lateinit var mapFragment: MapFragment
    // 현재 위치 반환값
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource

    // binding
    private var _binding: FragmentBottomnviHomeBinding? = null
    private val binding get() = _binding!!

    // recycler view
    private val SEARCH_CLIENT_ID = "c8hh8dsrqnsuh3wDLvzi"
    private val SEARCH_SECRET_KEY = "VMmDTZuvfv"
    lateinit var recyclerViewAdapter : RecyclerViewAdapter
    val datas = mutableListOf<RecyclerViewData>()

    override fun onAttach(context:Context){
        super.onAttach(context)
        mainActivity = context as MainActivity

    }

    // resource initiaion, 나중에 조건 넣을때 사용할 예정
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = childFragmentManager

        // 지도 객체 선언
        mapFragment = fm.findFragmentById(com.example.plant.R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(com.example.plant.R.id.map_fragment, it).commit()
            }
        // onMapReady 호출
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
//        binding.tvLocation.text = "안녕"

        locationTextWatcher()
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

    public lateinit var text : String
    private fun locationTextWatcher(){
        binding.edtSearchLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = binding.edtSearchLocation.text.toString()
//                Toast.makeText(mainActivity, text,Toast.LENGTH_SHORT).show()
                clearRecycler()
                binding.btnFindWay.setOnClickListener {
                    connectNaverSearch()
                }

//                connectNaverSearch()
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()){
                    clearRecycler()
                }

            }
        })
    }

//    lateinit var searchAdapter: SearchAdapter
//    val datas = mutableListOf<SearchData>()
    // 위 검색 토대로 recyclerview 생성
    private fun connectNaverSearch() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val locationSearchInterface = retrofit.create(LocationSearchInterface::class.java)

        locationSearchInterface.getLocationByName(SEARCH_CLIENT_ID,SEARCH_SECRET_KEY,text,5)
            .enqueue(object : Callback<LocationDTO> {
                override fun onResponse(
                    call: Call<LocationDTO>,
                    response: Response<LocationDTO>
                ) {
                    if(response.isSuccessful.not()){
                        return
                    }
                    response.body()?.locations?.forEach{
                        addRecycler(Html.fromHtml(it.title).toString(),it.category,it.description,it.roadAddress)
                    }
                    recyclerViewAdapter.datas = datas
                }
                override fun onFailure(call: Call<LocationDTO>, t: Throwable) {
                    Log.d(TAG, "Connection ERROR")
                }
            })
    }
    private fun clearRecycler(){
        datas.clear()
    }
    private fun addRecycler(title : String, category: String, description: String, roadAddress: String){
        recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
        binding.rvItems.adapter = recyclerViewAdapter
        datas.apply {
            add(RecyclerViewData(title, category, description, roadAddress))
        }
        // RecyclerclickEvent
        recyclerViewAdapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: RecyclerViewData, pos : Int) {
                Toast.makeText(mainActivity,"${data.title}",Toast.LENGTH_SHORT).show()

            }

        })
    }


}

