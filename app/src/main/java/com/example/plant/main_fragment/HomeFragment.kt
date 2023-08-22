package com.example.plant.main_fragment

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.HOTPLACE.HOTPLACEDTO
import com.example.plant.HOTPLACE.HOTPLACESearchInterface

import com.example.plant.MainActivity
import com.example.plant.NaverGeocode.GeocodeDTO
import com.example.plant.NaverGeocode.GeocodeInterface
import com.example.plant.NaverRevesreGeocode.ReverseGeocodeDTO
import com.example.plant.NaverRevesreGeocode.ReverseGeocodeInterface
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.R
import com.example.plant.databinding.FragmentBottomnviHomeBinding
import com.example.plant.pathfinder.NaverAPI
import com.example.plant.pathfinder.ResultPath
import com.naver.maps.geometry.Coord
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.CircleOverlay

import com.naver.maps.geometry.LatLngBounds

import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//, PermissionListener
class HomeFragment : Fragment(), FragmentListener, OnMapReadyCallback {

    lateinit var mainActivity: MainActivity

    // 마커 찍기
    private val marker = Marker()

    // Geocode
    val GEOCODE_CLIENT_ID = "u04wstprb6"
    val GEOCODE_SECRET_KEY = "UTtsqS8xv7TxQzZcE9offwjuXfQ9LKUqJm9CZ7UW"


    // naver map
    private lateinit var naverMap: NaverMap
    private lateinit var mapFragment: MapFragment
    private val circleOverlay = CircleOverlay() // circle overlay

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

    //editText
    lateinit var edtSearchLocation: EditText
    lateinit var rvItems: RecyclerView

    // 현재위치 기반 맛집찾기
    var addressByReverseGeocode = ""
    var HOTPLACEMarker = Marker()
    var HOTPLACEList : MutableList<Array<String>> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomnviHomeBinding.inflate(inflater, container, false)
        val view = binding.root

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
        mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        // onMapReady 호출
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        binding.edtSearchLocation.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    mainActivity.hideRecyclerView(binding.rvItems, false)
                    recyclerViewAdapter = RecyclerViewAdapter(mainActivity)
                    binding.rvItems.adapter = recyclerViewAdapter
                    mainActivity.targetRecyclerView = binding.rvItems
                    mainActivity.targetActivity = mainActivity
                    mainActivity.locationTextWatcher(binding.edtSearchLocation)

                    mainActivity.editText = binding.edtSearchLocation
                    edtSearchLocation = binding.edtSearchLocation
                    rvItems = binding.rvItems


                }
            }
        })

        //카메라 이동 버튼 setOnClickListener
        binding.btnFirstCamera.setOnClickListener {
            firstCamera()
        }

        binding.btnNextCamera.setOnClickListener {
            stackOfCamera += 1
            if (path.coords.size < stackOfCamera * 60) {
                val cameraUpdate = CameraUpdate.scrollTo(mainActivity.goalCoord as LatLng)
                naverMap.moveCamera(cameraUpdate)
                stackOfCamera = -1
            } else {
                nextCamera()
            }
        }
        binding.btnPrevCamera.setOnClickListener {
            stackOfCamera -= 1
            if (stackOfCamera < 0) {
                stackOfCamera = 0
            }
            prevCamera()
        }

        binding.btnFindWay.setOnClickListener {
            mFragmentListener = MapFinderFragment()
            hideMapFinder(false)
        }

        // 위치에서 탐색하기 클릭시 뜨게 하기
        binding.btnFindMapItems.setOnClickListener {
            Toast.makeText(mainActivity,"위치에서 탐색하기 버튼 click",Toast.LENGTH_SHORT).show()
            // 현재 위치 중심을 토대로 원 생성
            circleOverlay.center = naverMap.cameraPosition.target
            circleOverlay.outlineWidth = 12
            circleOverlay.radius = 1000.0
            circleOverlay.color = 0
            circleOverlay.map = naverMap

            val latlng_string = "%.9f".format(naverMap.cameraPosition.target.longitude) + "," + "%.9f".format(naverMap.cameraPosition.target.latitude)
            HOTPLACEList.clear()
            ReverseGeocode(latlng_string)
            findHOTPLACE(addressByReverseGeocode, "카페")
            // 이름, 카테고리, 설명, 도로명주소, x, y
//            if(!HOTPLACEList[0][4].isEmpty() && !HOTPLACEList[0][5].isEmpty()) {
//                val tempLatLng =
//                    LatLng(HOTPLACEList[0][4].toDouble(), HOTPLACEList[0][5].toDouble())
//                HOTPLACEMarker.position = tempLatLng
//                HOTPLACEMarker.map = naverMap
//            }
//            if(!HOTPLACEList[1][4].isEmpty() && !HOTPLACEList[1][5].isEmpty()) {
//                val tempLatLng1 =
//                    LatLng(HOTPLACEList[1][4].toDouble(), HOTPLACEList[1][5].toDouble())
//                HOTPLACEMarker.position = tempLatLng1
//                HOTPLACEMarker.map = naverMap
//            }
//            if(!HOTPLACEList[2][4].isEmpty() && !HOTPLACEList[2][5].isEmpty()) {
//                val tempLatLng2 =
//                    LatLng(HOTPLACEList[2][4].toDouble(), HOTPLACEList[2][5].toDouble())
//                HOTPLACEMarker.position = tempLatLng2
//                HOTPLACEMarker.map = naverMap
//            }
//            if(!HOTPLACEList[3][4].isEmpty() && !HOTPLACEList[3][5].isEmpty()) {
//                val tempLatLng3 =
//                    LatLng(HOTPLACEList[3][4].toDouble(), HOTPLACEList[3][5].toDouble())
//                HOTPLACEMarker.position = tempLatLng3
//                HOTPLACEMarker.map = naverMap
//            }
//            if(!HOTPLACEList[4][4].isEmpty() && !HOTPLACEList[4][5].isEmpty()) {
//                val tempLatLng4 =
//                    LatLng(HOTPLACEList[4][4].toDouble(), HOTPLACEList[4][5].toDouble())
//                HOTPLACEMarker.position = tempLatLng4
//                HOTPLACEMarker.map = naverMap
//            }



        }
    }

    var tempHOTPLACE_X = ""
    var tempHOTPLACE_Y = ""
    fun findHOTPLACE(placeAddress: String, placetype:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val HOTPLACESearchInterface = retrofit.create(HOTPLACESearchInterface::class.java)
        HOTPLACESearchInterface.getHOTPLACEByLocation(SEARCH_CLIENT_ID, SEARCH_SECRET_KEY, placeAddress + placetype)
            .enqueue(object : Callback<HOTPLACEDTO> {
                override fun onResponse(
                    call: Call<HOTPLACEDTO>,
                    response: Response<HOTPLACEDTO>
                ) {
                    response.body()?.HOTPLACES?.forEach {
                        if(!it.roadAddress.isNullOrEmpty()){
                            HOTPLACEGeocoder(it.roadAddress)
                            HOTPLACEList.add(arrayOf(it.title,it.category,it.description,it.roadAddress,tempHOTPLACE_X,tempHOTPLACE_Y))
                        }
                    }
//                    Log.d(ContentValues.TAG,HOTPLACEList[0][0])

                }
                override fun onFailure(call: Call<HOTPLACEDTO>, t: Throwable) {
                    Log.d(ContentValues.TAG, "Connection ERROR")
                }
            })
    }
    fun HOTPLACEGeocoder(HOTPLACE_RoadAddress: String) {
        val retrofit = Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val GeocodeInterface = retrofit.create(GeocodeInterface::class.java)
        val call =
            GeocodeInterface.getLocationByGeocode(GEOCODE_CLIENT_ID, GEOCODE_SECRET_KEY, HOTPLACE_RoadAddress)

        call.enqueue(object : Callback<GeocodeDTO> {
            override fun onResponse(
                call: Call<GeocodeDTO>, response: Response<GeocodeDTO>
            ) {
                response.body()?.addresses?.forEach {
                    tempHOTPLACE_X = it.x
                    tempHOTPLACE_Y = it.y
                }
            }
            override fun onFailure(call: Call<GeocodeDTO>, t: Throwable) {
                Log.d(ContentValues.TAG, "Connection ERROR")
            }
        })
    }
    fun ReverseGeocode(address_x_y : String) {
        val retrofit = Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()


        val ReverseGeocodeInterface = retrofit.create(ReverseGeocodeInterface::class.java)
        val call =
            ReverseGeocodeInterface.getLocationByReverseGeocode(GEOCODE_CLIENT_ID, GEOCODE_SECRET_KEY,address_x_y)

        call.enqueue(object : Callback<ReverseGeocodeDTO> {
            override fun onResponse(
                call: Call<ReverseGeocodeDTO>, response: Response<ReverseGeocodeDTO>
            ) {
//                Log.d("Test", "Raw: ${response.raw()}")
//                Log.d("Test", "Body: ${response.body()}")
                response.body()?.results?.forEach {
                    if(it.region.area1.name.isNotEmpty()) addressByReverseGeocode = ""
                    addressByReverseGeocode =
                        it.region.area1.name + " " + it.region.area2.name + " " + it.region.area3.name +
                                " "  + it.region.area4.name
                }
                Log.d("Test",addressByReverseGeocode)

            }

            override fun onFailure(call: Call<ReverseGeocodeDTO>, t: Throwable) {
                Log.d(ContentValues.TAG, t.toString())

            }
        })
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
            Toast.makeText(
                mainActivity, "${coord.latitude}, ${coord.longitude}", Toast.LENGTH_SHORT
            ).show()
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

    //tvParent.text = data -> 바꿔야함
    override fun onReceivedData(data: String) {
//tvParent.text = data
    }


    // visible 설정
    fun hideMapFinder(state: Boolean) {
        if (state) binding.frameLayoutMapFinder.visibility =
            View.GONE else binding.frameLayoutMapFinder.visibility = View.VISIBLE
    }

    lateinit var coord: Coord
    fun moveToSearchedLocation(x: Double, y: Double, type: String, boolean: Boolean) {
        coord = LatLng(y, x)
        if (type.contains("search")) {
            Toast.makeText(mainActivity, "냅둬", Toast.LENGTH_SHORT).show()
        } else if (type.contains("start")) {
            mainActivity.startCoord = coord
            Log.d(ContentValues.TAG, mainActivity.startCoord.toString())
        } else if (type.contains("goal")) {
            mainActivity.goalCoord = coord
            Log.d(ContentValues.TAG, mainActivity.goalCoord.toString())
        }
        Toast.makeText(mainActivity, "x:${x} and y:${y}", Toast.LENGTH_SHORT).show()
        if (boolean == true) {
            val cameraUpdate =
                CameraUpdate.scrollTo(coord as LatLng).animate(CameraAnimation.Easing, 2000)
            naverMap.moveCamera(cameraUpdate)
        }

        // 잠깐 마커 테스트
        marker.position = coord as LatLng
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED // 현재위치 마커 빨간색으로
        marker.captionText = "여기"
    }

    fun Geocode(address: String, type: String, boolean: Boolean) {
        val retrofit = Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val GeocodeInterface = retrofit.create(GeocodeInterface::class.java)
        val call =
            GeocodeInterface.getLocationByGeocode(GEOCODE_CLIENT_ID, GEOCODE_SECRET_KEY, address)

        call.enqueue(object : Callback<GeocodeDTO> {
            override fun onResponse(
                call: Call<GeocodeDTO>, response: Response<GeocodeDTO>
            ) {
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}")
                response.body()?.addresses?.forEach {
                    val x = it.x
                    val y = it.y
                    if (boolean == true) {
                        moveToSearchedLocation(x.toDouble(), y.toDouble(), type, true)
                    } else {
                        moveToSearchedLocation(x.toDouble(), y.toDouble(), type, false)
                    }
                }
            }

            override fun onFailure(call: Call<GeocodeDTO>, t: Throwable) {
                Log.d(ContentValues.TAG, "Connection ERROR")
            }
        })
    }

    val path = PathOverlay()
    fun pathFinder(startCoord: Coord, goalCoord: Coord) {
        val retrofit =
            Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(NaverAPI::class.java) // 여기까지 API 연결 세팅(Direction5)

        //근처에서 길찾기
        // 여기서 연결
        var startCoordString = startCoord.toString()
        startCoordString = startCoordString.replace("LatLng{latitude=", "")
        startCoordString = startCoordString.replace("longitude=", "")
        startCoordString = startCoordString.replace("}", "")
        var startCoordArr = startCoordString.split(",") //배열로써 스플릿함
        var startCoordStr = (startCoordArr[1] + ", " + startCoordArr[0])

        var goalCoordString = goalCoord.toString()
        goalCoordString = goalCoordString.replace("LatLng{latitude=", "")
        goalCoordString = goalCoordString.replace("longitude=", "")
        goalCoordString = goalCoordString.replace("}", "")
        var goalCoordArr = goalCoordString.split(",") //배열로써 스플릿함
        var goalCoordStr = (goalCoordArr[1] + ", " + goalCoordArr[0])

        Log.d(ContentValues.TAG, "Start = ${startCoordString} Goal = ${goalCoordString}")

        val callgetPath = api.getPath(
            PATHFINDER_CLIENT_ID, PATHFINDER_SECRET_KEY,
            startCoordStr, goalCoordStr
        )

        callgetPath.enqueue(/* callback = */ object : Callback<ResultPath> {
            override fun onResponse(
                call: Call<ResultPath>, response: Response<ResultPath>
            ) {
                var path_cords_list = response.body()?.route?.traoptimal
                //경로 그리기 응답바디가 List<List<Double>> 이라서 2중 for문 썼음
//                val path = PathOverlay()
                //MutableList에 add 기능 쓰기 위해 더미 원소 하나 넣어둠
                val path_container: MutableList<LatLng>? = mutableListOf(LatLng(0.1, 0.1))
                for (path_cords in path_cords_list!!) {
                    for (path_cords_xy in path_cords?.path!!) {
                        //구한 경로를 하나씩 path_container에 추가해줌
                        path_container?.add(LatLng(path_cords_xy[1], path_cords_xy[0]))
                    }
                }
                //더미원소 드랍후 path.coords에 path들을 넣어줌.
                path.coords = path_container?.drop(1)!!
                path.color = Color.RED
                path.map = naverMap
                var path_size = path.coords.size

                if (path.coords != null) {
                    val cameraUpdate =
                        CameraUpdate.scrollTo(path.coords[0]!!).animate(CameraAnimation.Fly, 3000)
                    naverMap!!.moveCamera(cameraUpdate)
                    Log.d(ContentValues.TAG, "path size is ${path_size}")

                    Toast.makeText(mainActivity, "경로 안내가 시작됩니다.", Toast.LENGTH_SHORT).show()
                }

                Log.d(ContentValues.TAG, path.coords[0].toString())
            }

            override fun onFailure(call: Call<ResultPath>, t: Throwable) {
                Log.d(ContentValues.TAG, "ErrorPathFinder")
            }

        })

    }

    //카메라 이동 (이전, 처음, 다음)
    fun firstCamera() {
        //여기 바꿔야함
        val cameraUpdate = CameraUpdate.scrollTo(mainActivity.startCoord as LatLng)
        //여기에 path_cords_list 소분해서 ㄱㄱ
        var path_size = path.coords.size
        naverMap.moveCamera(cameraUpdate)
        Log.d(ContentValues.TAG, "path size is ${path_size}")
    }

    var stackOfCamera = 0
    fun nextCamera() {
        var nextStep = path.coords[stackOfCamera * 60]
        val cameraUpdate = CameraUpdate.scrollTo(nextStep as LatLng)
        naverMap.moveCamera(cameraUpdate)
    }

    fun prevCamera() {
        var prevStep = path.coords[stackOfCamera * 60]
        val cameraUpdate = CameraUpdate.scrollTo(prevStep as LatLng)
        naverMap.moveCamera(cameraUpdate)
    }


}




