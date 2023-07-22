package com.example.plant.main_fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.example.plant.NaverGeocode.GeocodeDTO
import com.example.plant.NaverGeocode.GeocodeInterface
import com.example.plant.NaverSearch.LocationDTO
import com.example.plant.NaverSearch.LocationSearchInterface
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.example.plant.pathfinder.NaverAPI
import com.example.plant.pathfinder.ResultPath
import com.naver.maps.map.util.MarkerIcons
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
    // 마커 찍기
    private val marker = Marker()

    // Geocode
    private val GEOCODE_CLIENT_ID = "u04wstprb6"
    private val GEOCODE_SECRET_KEY = "UTtsqS8xv7TxQzZcE9offwjuXfQ9LKUqJm9CZ7UW"
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


        var searchLocationIntent = Intent()
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

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // uiSettings
        val uiSettings = naverMap.uiSettings
        naverMap.extent = LatLngBounds(LatLng(31.43, 122.37), LatLng(44.35, 132.0)) // 국내에서만 탐색
        naverMap.minZoom = 6.0
        naverMap.maxZoom = 18.0 // 최소 최대 zoom 설정
        // 현재 위치 및 위치 추적
        naverMap.locationSource = locationSource
        uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.onSymbolClickListener
        // 심볼클릭
        naverMap.setOnMapClickListener { point, coord ->
            Toast.makeText(mainActivity, "${coord.latitude}, ${coord.longitude}",
                Toast.LENGTH_SHORT).show()
        }

    // visible 설정
    fun hideMapFinder(state: Boolean) {
        if (state) binding.frameLayoutMapFinder.visibility =
            View.GONE else binding.frameLayoutMapFinder.visibility = View.VISIBLE
    }

        naverMap.setOnSymbolClickListener { symbol ->
            if (symbol.caption == "롯데리아") {
                Toast.makeText(mainActivity, "${symbol.caption}", Toast.LENGTH_SHORT).show()
                // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
                true
            } else {
                // 이벤트 전파, OnMapClick 이벤트가 발생함
                false
            }
        }

    }

    lateinit var text: String
    private fun locationTextWatcher() {
        binding.edtSearchLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    //tvParent.text = data -> 바꿔야함
    override fun onReceivedData(data: String) {
//tvParent.text = data
    }
}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = binding.edtSearchLocation.text.toString()
                clearRecycler()
                connectNaverSearch()
            }



            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearRecycler()
                }




        locationSearchInterface.getLocationByName(SEARCH_CLIENT_ID, SEARCH_SECRET_KEY, text, 5)
            .enqueue(object : Callback<LocationDTO> {
                override fun onResponse(
                    call: Call<LocationDTO>,
                    response: Response<LocationDTO>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    Log.d(TAG,"RAW: ${response.raw()}")
                    Log.d(TAG,"BODY: ${response.body()}")
                    response.body()?.locations?.forEach {
                        addRecycler(
                            Html.fromHtml(it.title).toString(),
                            it.category,
                            it.description,
                            it.roadAddress,
                            it.mapx,
                            it.mapy,
                            it.address
                        )
                    }
                    recyclerViewAdapter.datas = datas
                }

                override fun onFailure(call: Call<LocationDTO>, t: Throwable) {
                    Log.d(TAG, "Connection ERROR")
                }
            })
    }

    private fun clearRecycler() {
        datas.clear()
    }

    private fun addRecycler(
        title: String,
        category: String,
        description: String,
        roadAddress: String,
        mapx: String,
        mapy: String,
        address: String
    ) {
        recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
        binding.rvItems.adapter = recyclerViewAdapter
        datas.apply {
            add(RecyclerViewData(title, category, description, roadAddress, mapx, mapy, address))
        }
        // RecyclerclickEvent
        recyclerViewAdapter.setOnItemClickListener(object :
            RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: RecyclerViewData, pos: Int) {
                binding.edtSearchLocation.setText("")
                binding.edtSearchLocation.setHint(data.title)
                Geocode(data.roadAddress)
                Toast.makeText(mainActivity,"${data.roadAddress}",Toast.LENGTH_SHORT).show()
                clearRecycler()
            }

        })
    }

    private fun Geocode(address: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val GeocodeInterface = retrofit.create(GeocodeInterface::class.java)
        val call = GeocodeInterface.getLocationByGeocode(GEOCODE_CLIENT_ID, GEOCODE_SECRET_KEY, address)

        call.enqueue(object : Callback<GeocodeDTO> {
                override fun onResponse(
                    call: Call<GeocodeDTO>,
                    response: Response<GeocodeDTO>
                ) {
                    Log.d("Test", "Raw: ${response.raw()}")
                    Log.d("Test", "Body: ${response.body()}")
                    response.body()?.addresses?.forEach{
                        val x = it.x
                        val y = it.y
                        moveToSearchedLocation(x.toDouble(),y.toDouble())
                    }
                }

                override fun onFailure(call: Call<GeocodeDTO>, t: Throwable) {
                    Log.d(TAG, "Connection ERROR")
                }
            })
    }
    private fun moveToSearchedLocation(x: Double, y: Double) {
        val coord = LatLng(y, x)
        Toast.makeText(mainActivity,"x:${x} and y:${y}",Toast.LENGTH_SHORT).show()
        val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing, 2000)
        naverMap.moveCamera(cameraUpdate)

        // 잠깐 마커 테스트
        marker.position = coord
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED // 현재위치 마커 빨간색으로
        marker.captionText = "여기"
    }
    // 크롤링으로 데이터 가져올 예정



