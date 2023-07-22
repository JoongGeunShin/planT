package com.example.plant


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.plant.NaverGeocode.GeocodeDTO
import com.example.plant.NaverGeocode.GeocodeInterface
import com.example.plant.NaverSearch.LocationDTO
import com.example.plant.NaverSearch.LocationSearchInterface
import com.example.plant.NaverSearch.RecyclerViewAdapter
import com.example.plant.NaverSearch.RecyclerViewData
import com.example.plant.main_fragment.CalendarFragment
import com.example.plant.main_fragment.HomeFragment
import com.example.plant.main_fragment.SettingsFragment
import com.example.plant.main_fragment.UserinfoFragment
import com.example.plant.pathfinder.NaverAPI
import com.example.plant.pathfinder.ResultPath
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


open class MainActivity : AppCompatActivity() {

    // naver map
    private lateinit var naverMap: NaverMap

    // recycler view
    val SEARCH_CLIENT_ID = "c8hh8dsrqnsuh3wDLvzi"
    val SEARCH_SECRET_KEY = "VMmDTZuvfv"
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var targetRecyclerView: RecyclerView
    lateinit var targetActivity: Activity
    var datas = mutableListOf<RecyclerViewData>()
    
    //수정필요
    var homeFragment: HomeFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer) as HomeFragment


    private val fl: FrameLayout by lazy {
        findViewById(R.id.mainFragmentContainer)
    }

    //pathFinder
    val PATHFINDER_CLIENT_ID = "u04wstprb6"
    val PATHFINDER_SECRET_KEY = "UTtsqS8xv7TxQzZcE9offwjuXfQ9LKUqJm9CZ7UW"

    // 마커 찍기
    private val marker = Marker()

    // Geocode
    val GEOCODE_CLIENT_ID = "u04wstprb6"
    val GEOCODE_SECRET_KEY = "UTtsqS8xv7TxQzZcE9offwjuXfQ9LKUqJm9CZ7UW"

    lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tet

        val bnv_main = findViewById<BottomNavigationView>(R.id.bnv_main)
        bnv_main.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.menu_home -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        HomeFragment()
                        // Respond to navigation item 1 click
                    }

                    R.id.menu_calendar -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        CalendarFragment()
                        // Respond to navigation item 2 click
                    }

                    R.id.menu_userinfo -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        UserinfoFragment()
                        // Respond to navigation item 3 click
                    }

                    else -> {
                        bnv_main.itemIconTintList =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        bnv_main.itemTextColor =
                            ContextCompat.getColorStateList(this, R.color.color_bnv)
                        SettingsFragment()
                    }
                }
            )
            true
        }
        bnv_main.selectedItemId = R.id.menu_home
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }

    lateinit var text: String
    fun locationTextWatcher(editText: EditText) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = editText.text.toString()
//                Toast.makeText(mainActivity, text,Toast.LENGTH_SHORT).show()
                clearRecycler()
//                binding.btnFindWay.setOnClickListener {
//                    connectNaverSearch()
//                }

                connectNaverSearch(targetActivity, targetRecyclerView)
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearRecycler()
                }

            }
        })
    }

    //    lateinit var searchAdapter: SearchAdapter
//    val datas = mutableListOf<SearchData>()
    // 위 검색 토대로 recyclerview 생성
    fun connectNaverSearch(targetActivity: Activity, targetRecyclerView: RecyclerView) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val locationSearchInterface = retrofit.create(LocationSearchInterface::class.java)

        locationSearchInterface.getLocationByName(SEARCH_CLIENT_ID, SEARCH_SECRET_KEY, text, 5)
            .enqueue(object : Callback<LocationDTO> {
                override fun onResponse(
                    call: Call<LocationDTO>,
                    response: Response<LocationDTO>
                ) {
                    if (response.isSuccessful.not()) {
                        return
                    }
                    recyclerViewAdapter = RecyclerViewAdapter(targetActivity)
                    targetRecyclerView.adapter = recyclerViewAdapter
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
                    recyclerViewAdapter.datas = datas.toMutableList()
                }

                override fun onFailure(call: Call<LocationDTO>, t: Throwable) {
                    Log.d(ContentValues.TAG, "Connection ERROR")
                }
            })
    }

    fun clearRecycler() {
        this.datas.clear()
    }

    fun addRecycler(
        title: String,
        category: String,
        description: String,
        roadAddress: String,
        mapx: String,
        mapy: String,
        address: String
    ) {
        datas.apply {
            add(RecyclerViewData(title, category, description, roadAddress, mapx, mapy, address))
        }
        // RecyclerclickEvent
        recyclerViewAdapter.setOnItemClickListener(object :
            RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: RecyclerViewData, pos: Int) {
                //수정필요
                editText.setText("")
                editText.setHint(data.title)
                homeFragment.Geocode(data.roadAddress)
                Toast.makeText(this@MainActivity,"${data.roadAddress}",Toast.LENGTH_SHORT).show()
                clearRecycler()
            }

        })
    }

    fun pathFinder() {
        val retrofit =
            Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = retrofit.create(NaverAPI::class.java) // 여기까지 API 연결 세팅(Direction5)

        //근처에서 길찾기
        // 여기서 연결
        val callgetPath =
            api.getPath(
                PATHFINDER_CLIENT_ID,
                PATHFINDER_SECRET_KEY,
                "129.089441, 35.231100",
                "129.084454, 35.228982"
            ).also {

                it.enqueue(/* callback = */ object : Callback<ResultPath> {
                    override fun onResponse(
                        call: Call<ResultPath>,
                        response: Response<ResultPath>
                    ) {
                        var path_cords_list = response.body()?.route?.traoptimal
                        //경로 그리기 응답바디가 List<List<Double>> 이라서 2중 for문 썼음
                        val path = PathOverlay()
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
                            val cameraUpdate = CameraUpdate.scrollTo(path.coords[0]!!)
                                .animate(CameraAnimation.Fly, 3000)
                            naverMap!!.moveCamera(cameraUpdate)
                            Log.d(ContentValues.TAG, "path size is ${path_size}")

                            Toast.makeText(this@MainActivity, "경로 안내가 시작됩니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<ResultPath>, t: Throwable) {
                        Log.d(ContentValues.TAG, "ErrorPathFinder")
                    }

                })
            }
    }


    fun hideRecyclerView(recyclerView: RecyclerView, state: Boolean) {
        if (state) recyclerView.visibility = View.GONE else recyclerView.visibility = View.VISIBLE
    }

}