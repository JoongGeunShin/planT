package com.example.plant.main_fragment

import android.R

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import com.example.plant.MainActivity
import com.gun0912.tedpermission.*
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import okhttp3.internal.Util


//, PermissionListener
class HomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var mainActivity: MainActivity
    // naver map
    private lateinit var naverMap : NaverMap
    private lateinit var mapFragment: MapFragment
    // 현재 위치 반환값
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var locationSource: FusedLocationSource

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
        return inflater.inflate(com.example.plant.R.layout.fragment_bottomnvi_home, container, false)


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

    }

//    // <-- using ted permission, location
//    private fun requestPermission() {
//        TedPermission.create()
//            .setPermissionListener(this)
//            .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
//            .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
//            .setDeniedCloseButtonText("닫기")
//            .setGotoSettingButtonText("설정")
//            .setRationaleTitle("위치 권한 설정")
//            .setPermissions(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            .check()
//    }
//    override fun onPermissionGranted() {
//    }
//    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
//        Toast.makeText(mainActivity,"위치 정보 제공이 거부되었습니다.",Toast.LENGTH_SHORT).show()
//    }
//    // ted permission -->

    override fun onMapReady(@NonNull naverMap: NaverMap) {
        Toast.makeText(mainActivity,"onMapReadey 호출 완료",Toast.LENGTH_SHORT).show()
        this.naverMap = naverMap

        // uiSettings
        val uiSettings = naverMap.uiSettings

        // 현재 위치 관련 에러 계속 발생
        naverMap.locationSource = locationSource
        uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        }


}

