package pro.ahoora.zhin.healthbank.fragments;

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import org.greenrobot.eventbus.EventBus
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.adapters.HListAdapter
import pro.ahoora.zhin.healthbank.customClasses.CustomBottomSheetBehavior
import java.util.concurrent.TimeUnit

class MapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {
    override fun onClick(v: View?) {
        directionRequest()
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            ivToggle.rotation = 180f
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            ivToggle.rotation = 0f
        }
    }

    lateinit var map: GoogleMap
    lateinit var mapList: RecyclerView
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var bottomSheetLayout: ConstraintLayout
    lateinit var ivToggle: AppCompatImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_map, container, false)
        initMap()
        initViews(v)
        return v
    }


    private fun initViews(v: View) {
        ivToggle = v.findViewById(R.id.iv_toggle)
        ivToggle.setOnClickListener(this)
        mapList = v.findViewById(R.id.rv_mapList)
        bottomSheetLayout = v.findViewById(R.id.cl_mapBottomSheetLayout)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
        mapList.layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, false)
        mapList.adapter = HListAdapter(activity as Context)
        LinearSnapHelper().attachToRecyclerView(mapList)
    }

    private fun initMap() {
        var mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        if (mapFragment == null) {
            val fm = fragmentManager
            val ft: FragmentTransaction
            if (fm != null) {
                ft = fm.beginTransaction()
                mapFragment = SupportMapFragment.newInstance()
                ft.replace(R.id.map, mapFragment).commit()
            }
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            // TODO: 4/29/2018 show error dialog to user
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val sydney = LatLng(35.311339, 46.995957)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19f))
        Handler().postDelayed({ EventBus.getDefault().post("loaded") }, 150)
    }


    private fun getGeoContext(): GeoApiContext {
        val geoApiContext = GeoApiContext.Builder()
        geoApiContext.apiKey("AIzaSyC1vWjq5eQtlB5L5W7mZQKMeeaD7dQYpnE")
        geoApiContext.queryRateLimit(3)
        geoApiContext.connectTimeout(5, TimeUnit.SECONDS)
        geoApiContext.writeTimeout(5, TimeUnit.SECONDS)
        geoApiContext.readTimeout(5, TimeUnit.SECONDS)
        return geoApiContext.build()
    }

    private fun directionRequest() {
        val origin: com.google.maps.model.LatLng = com.google.maps.model.LatLng(35.307400, 46.994016)
        val destination: com.google.maps.model.LatLng = com.google.maps.model.LatLng(35.317059, 46.999075)
        val directionReq: DirectionsApiRequest = DirectionsApi.newRequest(getGeoContext())
        directionReq.destination(origin)
        directionReq.origin(destination)
        directionReq.mode(TravelMode.DRIVING)
        directionReq.alternatives(false)
        try {
            addPolyline(directionReq.await(), map)
        } catch (e: Exception) {
            Log.e("ERR", e.message + " ")
        }
    }

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap) {
        val decodedPath = results.routes[0].overviewPolyline.decodePath()
        val paths = ArrayList<com.google.android.gms.maps.model.LatLng>()
        Log.e("path", "${decodedPath.size}${decodedPath.toString()}")
        decodedPath.forEach { path: com.google.maps.model.LatLng ->
            paths.add(com.google.android.gms.maps.model.LatLng(path.lat, path.lng))
        }
        Log.e("paths", "${paths.size}${paths.toString()}")
        mMap.addPolyline(PolylineOptions().addAll(paths).color(ContextCompat.getColor(activity as Context, R.color.green)))
    }


}
