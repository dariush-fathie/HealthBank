package pro.ahoora.zhin.healthbank.activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_map.*
import pro.ahoora.zhin.healthbank.R
import pro.ahoora.zhin.healthbank.customClasses.CustomBottomSheetBehavior
import pro.ahoora.zhin.healthbank.interfaces.LoginListener
import pro.ahoora.zhin.healthbank.models.KotlinItemModel
import pro.ahoora.zhin.healthbank.models.SimpleResponseModel
import pro.ahoora.zhin.healthbank.utils.ApiInterface
import pro.ahoora.zhin.healthbank.utils.KotlinApiClient
import pro.ahoora.zhin.healthbank.utils.LoginClass
import pro.ahoora.zhin.healthbank.utils.StaticValues
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private val request_permission = 1052
    private val request_location_setting = 1053

    val realm = Realm.getDefaultInstance()!!
    var id = 0
    var i = 0
    var isSaved = false
    var itemSaved = false

    // if model save or delete form realm -> change = true  else change = false
    var change = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    var mMap: GoogleMap? = null

    // current model
    lateinit var item: KotlinItemModel

    // google location service
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    // send request to location service with @mLocationRequest
    private lateinit var mLocationRequest: LocationRequest
    // @mLocationRequest response
    private lateinit var mLocationCallback: LocationCallback

    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest

    private lateinit var currentPoint: LatLng

    private val updateInterval: Long = 10000
    private val fastestUpdateInterval: Long = updateInterval / 2

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                change = true
                if (isSaved) {
                    btn_save.text = "ذخیره در نشان شده ها"
                    deleteItem(id)
                    itemSaved = false

                } else {
                    btn_save.text = "حذف از نشان شده ها"
                    saveItem(id)
                    itemSaved = true
                }
            }
            R.id.rl_seeOnMap -> {
                openMapSheet()
            }
            R.id.fab_closeMap -> {
                closeMapSheet()
            }
            R.id.btn_savePoint -> {
                savePoint()
            }
            R.id.fab_direction -> directionRequest()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent != null) {
            id = intent.getIntExtra("id", 1)
            isSaved = checkItemIsSaved()

            if (isSaved) {
                btn_save.text = "حذف از نشان شده ها"
            } else {
                btn_save.text = "ذخیره در نشان شده ها"
            }
            if (intent.getIntExtra(StaticValues.MODEL, 0) == 0) {
                i = 0
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 1) {
                i = 1
                btn_save.visibility = View.GONE
            } else if (intent.getIntExtra(StaticValues.MODEL, 0) == 2) {
                i = 2
                btn_save.visibility = View.VISIBLE
            }

            if (i != 2) {
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                item = realm.where(KotlinItemModel::class.java).equalTo("centerId", id).findFirst()!!
                realm.commitTransaction()
            } else {
                item = SearchActivity.tempModel
            }

            loadDetails()
            btn_save.setOnClickListener(this)
            btn_savePoint.setOnClickListener(this)
            fab_closeMap.setOnClickListener(this)
            rl_seeOnMap.setOnClickListener(this)
            fab_direction.setOnClickListener(this)
        }
        initBottomSheet()
        initLists()
    }


    private fun getGeoContext(): GeoApiContext {
        val geoApiContext = GeoApiContext.Builder()
        geoApiContext.apiKey("AIzaSyCwx7842CtkOpGo-5RlUv7c8Ig-y2a0HxI")
        geoApiContext.queryRateLimit(3)
        geoApiContext.connectTimeout(5, TimeUnit.SECONDS)
        geoApiContext.writeTimeout(5, TimeUnit.SECONDS)
        geoApiContext.readTimeout(5, TimeUnit.SECONDS)
        return geoApiContext.build()
    }

    private fun directionRequest() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.progress_dialog)
        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()
        if (this::currentPoint.isInitialized) {
            val itemAddressLatLng = LatLng(item.addressList!![0]?.latitude?.toDouble()!!
                    , item.addressList!![0]?.longitude?.toDouble()!!)
            val origin: com.google.maps.model.LatLng = com.google.maps.model.LatLng(currentPoint.latitude, currentPoint.longitude)
            val destination: com.google.maps.model.LatLng =
                    com.google.maps.model.LatLng(itemAddressLatLng.latitude, itemAddressLatLng.longitude)

            val directionReq: DirectionsApiRequest = DirectionsApi.newRequest(getGeoContext())
            directionReq.destination(origin)
            directionReq.origin(destination)
            directionReq.mode(TravelMode.DRIVING)
            directionReq.alternatives(false)
            directionReq.setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult?) {
                    runOnUiThread({
                        addPolyline(result!!, mMap!!, LatLng(origin.lat, origin.lng), LatLng(destination.lat, destination.lng))
                        alert.dismiss()
                    })
                }

                override fun onFailure(e: Throwable?) {
                    runOnUiThread({
                        alert.dismiss()
                        Toast.makeText(this@DetailActivity, "خطایی رخ داد", Toast.LENGTH_SHORT).show()
                    })
                }
            })
        } else {
            Toast.makeText(this, "مکان فعلی شما مشخص نیست", Toast.LENGTH_SHORT).show()
        }
    }

    lateinit var originMarker: Marker
    lateinit var destinationMarker: Marker

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap, origin: LatLng, destination: LatLng) {
        if (this::originMarker.isInitialized) {
            originMarker.remove()
            destinationMarker.remove()
        }

        originMarker = mMap.addMarker(MarkerOptions().title("مبداء").position(origin))
        destinationMarker = mMap.addMarker(MarkerOptions().title("مقصد").position(destination))

        val builder = LatLngBounds.Builder()
        builder.include(origin).include(destination)

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 150))

        val decodedPath = results.routes[0].overviewPolyline.decodePath()
        val paths = ArrayList<com.google.android.gms.maps.model.LatLng>()
        Log.e("path", "${decodedPath.size}${decodedPath.toString()}")
        decodedPath.forEach { path: com.google.maps.model.LatLng ->
            paths.add(com.google.android.gms.maps.model.LatLng(path.lat, path.lng))
        }
        Log.e("paths", "${paths.size}${paths.toString()}")
        mMap.addPolyline(PolylineOptions().addAll(paths).color(ContextCompat.getColor(this, R.color.green)))

    }

    @SuppressLint("MissingPermission")
    private fun initLocationApi() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSettingsClient = LocationServices.getSettingsClient(this)
        // get last saved location
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
            //mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17f))
            currentPoint = LatLng(location.latitude, location.longitude)
        }
    }

    private fun createLocationRequest() {
        initLocationApi()
        mLocationRequest = LocationRequest.create()
        // update user location each 10000ms = 10s
        mLocationRequest.interval = updateInterval
        mLocationRequest.fastestInterval = fastestUpdateInterval
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        checkLocationSetting()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == request_location_setting) {
            checkLocationSetting()
        }
    }

    //open location setting page to active location toggle buttton
    private fun locationSettingDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("برای استفاده از نقشه باید تنظیمات مکان یابی دستگاه و همچنین gps را فعال کنید ")
        dialog.setPositiveButton("فعال کردن", { _, _ ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(myIntent, request_location_setting)
        })
        dialog.show()
    }

    // check if location setting is enabled?
    private fun checkLocationSetting() {
        mLocationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build()
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    Log.e("Location", "success")
                    // location setting in enabled
                    createLocationCallback()
                }.addOnFailureListener {
                    Log.e("Location", "failure")
                    locationSettingDialog()
                    // location setting not enabled
                }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    private fun createLocationCallback() {
        Log.e("loc", "1")
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.e("loc", "2")
                locationResult ?: return
                val x = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                currentPoint = x
                //mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(x, 18f))

                Log.e("Location Update", "location updated!")
            }
        }
        startLocationUpdate()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        Log.e("loc", "3")
        if (hasLocationPermission()) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }


    // stop location Update in onStop ;
    private fun stopLocationUpdate() {
        if (hasLocationPermission()) {
            if (this::mFusedLocationClient.isInitialized && this::mLocationCallback.isInitialized) {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdate()
    }

    // save map center point in center information
    private fun savePoint() {
        val b = AlertDialog.Builder(this)
        b.setMessage("توجه فرمایید که نقطه مشخص شده در مرکز نقشه در اطلاعات این مرکز ذخیره می شود")
        b.setPositiveButton("متوجه هستم، ادامه بده", { dialog, which ->
            Log.e("autoId", "${item.addressList!![0]?.autoId}")
            val builder = AlertDialog.Builder(this)
            builder.setView(R.layout.progress_dialog)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.show()
            val lat = centerPoint?.latitude
            val lng = centerPoint?.longitude
            val res = KotlinApiClient.client.create(ApiInterface::class.java)
                    .updateGeoLocation(item.addressList!![0]?.autoId!!, lat!!, lng!!)
            res.enqueue(object : retrofit2.Callback<SimpleResponseModel> {
                override fun onResponse(call: Call<SimpleResponseModel>?, response: Response<SimpleResponseModel>?) {
                    val result = response?.body()
                    if (result?.response == "success") {
                        Toast.makeText(this@DetailActivity, "با موفقیت در اطلاعت مرکز ذخیره شد", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@DetailActivity, "خطا در ذخیره اطلاعات در بانک", Toast.LENGTH_LONG).show()
                    }
                    alertDialog.dismiss()
                }

                override fun onFailure(call: Call<SimpleResponseModel>?, t: Throwable?) {
                    alertDialog.dismiss()
                    Toast.makeText(this@DetailActivity, "خطا در ذخیره اطلاعات در بانک", Toast.LENGTH_LONG).show()
                }
            })

        })
        b.setNegativeButton("ادامه نده، برگرد", { dialog, which ->
            dialog.dismiss()
        })
        b.show()
    }


    private fun closeMapSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun openMapSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun checkUserLoggedIn() {
        LoginClass(this, object : LoginListener {
            override fun sessionExist() {
                Log.e("session", "exist")

                tv_point.visibility = View.VISIBLE
                btn_savePoint.visibility = View.VISIBLE
                iv_marker.visibility = View.VISIBLE

                centerPoint = mMap?.projection?.visibleRegion?.latLngBounds?.center
                tv_point.text = formatLatLng(centerPoint!!)
            }

            override fun sessionNotExist() {
                Log.e("session", "not exist")
            }
        })

    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(cl_detailMapSheet)
        if (bottomSheetBehavior is CustomBottomSheetBehavior) {
            (bottomSheetBehavior as CustomBottomSheetBehavior).setAllowUserDragging(false)
        }
        bottomSheetBehavior.setBottomSheetCallback(getBottomSheetCallback())

    }

    override fun onCameraMove() {
        centerPoint = mMap?.projection?.visibleRegion?.latLngBounds?.center
        tv_point.text = formatLatLng(centerPoint!!)
    }

    private var centerPoint: LatLng? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        ll_detailProgress.visibility = View.GONE

        checkLocationPermissions()
        enableMyLocation()
        mMap?.setOnCameraMoveListener(this)
        val p = LatLng(item.addressList!![0]?.latitude?.toDouble()!!, item.addressList!![0]?.longitude?.toDouble()!!)
        mMap?.addMarker(MarkerOptions().title("${item.firstName} ${item.lastName}").position(p))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(p, 16f))
        checkUserLoggedIn()
    }

    private fun formatLatLng(p: LatLng): String {
        return "x : " + p.latitude + "\n" + "y : " + p.longitude
    }

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("slide", "$slideOffset")
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        if (mMap == null) {
                            val mapFragment = supportFragmentManager
                                    .findFragmentById(R.id.detailMap) as SupportMapFragment
                            mapFragment.getMapAsync(this@DetailActivity)
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }
        }
        return bottomSheetCallback
    }

    private fun saveItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = true
        })
    }

    private fun deleteItem(centerId: Int) {
        realm.executeTransaction({ db ->
            val item = db.where(KotlinItemModel::class.java)
                    .equalTo("centerId", centerId)
                    .findFirst()!!
            item.saved = false
        })
    }

    private fun checkItemIsSaved(): Boolean {
        var isSaved = false
        realm.executeTransaction({ db ->
            val model = db.where(KotlinItemModel::class.java)
                    ?.equalTo("centerId", id)
                    ?.equalTo("saved", true)
                    ?.findAll()!!
            isSaved = model.count() > 0
        })
        return isSaved
    }

    private fun initLists() {
        rv_imageListBig.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_imageListBig.adapter = ImageAdapter()
        list_indicator.attachToRecyclerView(rv_imageListBig)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_imageListBig)

        rv_imageListBig.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val p = (recyclerView?.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (p >= 0) {
                    rv_imageThumbnail.smoothScrollToPosition(p)
                    (rv_imageThumbnail.adapter as ThumbnailAdapter).select(p)
                }
            }
        })

        rv_imageThumbnail.layoutManager = LinearLayoutManager(this)
        rv_imageThumbnail.adapter = ThumbnailAdapter()
    }

    private fun loadDetails() {
        tv_dName.text = item.firstName + " " + item.lastName
        tv_dt.text = item.specialityList!![0]?.name
        tv_dAddress.text = item.addressList!![0]?.locTitle
    }

    override fun onBackPressed() {
        if (change) {
            val resultPayload = Intent(this@DetailActivity, OfficeActivity::class.java)
            resultPayload.putExtra("save", itemSaved)
            resultPayload.putExtra("centerId", id)
            setResult(Activity.RESULT_OK, resultPayload)
        }
        super.onBackPressed()
    }

    // show list of slides in real size
    inner class ImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(this@DetailActivity).inflate(R.layout.image_big_item, parent, false)
            return ImageHolder(v)
        }

        override fun getItemCount(): Int {
            return item.slideList?.size!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            try {
                Glide.with(this@DetailActivity)
                        .load(item.slideList!![position]?.fileUrl)
                        .apply(RequestOptions()
                                .fitCenter()
                                .placeholder(R.drawable.ic_jin))
                        .into((holder as ImageHolder)
                                .ivImage)
            } catch (e: Exception) {
                Log.e("glideErr", e.message + " ")
            }
        }

        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageThumbnail.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_bigItem)
            val ivImage = itemView.findViewById<AppCompatImageView>(R.id.iv_imageBig)

            init {
                cvImage.setOnClickListener(this)
            }
        }

    }


    // show list of slides in thumbnail size
    inner class ThumbnailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val v = LayoutInflater.from(this@DetailActivity).inflate(R.layout.image_thumbnail_item, parent, false)
            return ImageHolder(v)
        }

        override fun getItemCount(): Int {
            return item.slideList?.size!!
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            try {
                Glide.with(this@DetailActivity)
                        .load(item.slideList!![position]?.fileUrl)
                        .apply(RequestOptions()
                                .fitCenter()
                                .placeholder(R.drawable.ic_jin))
                        .thumbnail(0.5f)
                        .into((holder as ImageHolder)
                                .ivImage)
            } catch (e: Exception) {
                Log.e("glideErr", e.message + " ")
            }
        }


        internal inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            override fun onClick(v: View?) {
                rv_imageListBig.smoothScrollToPosition(adapterPosition)
            }

            val cvImage = itemView.findViewById<CardView>(R.id.cv_smallItem)
            val ivImage = itemView.findViewById<AppCompatImageView>(R.id.iv_imageThumbnail)

            init {
                cvImage.setOnClickListener(this)
            }
        }

        fun select(position: Int) {
            Log.e("selected", "$position" + "")
        }

    }

    // return true if app has location permission
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // request location permission
    private fun checkLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasLocationPermission()) {
                createLocationRequest()
                // access location
                Toast.makeText(this, "location permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this
                        , arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        , request_permission)
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == request_permission) {
            if (permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("granted", "success")
                    createLocationRequest()
                } else {
                    Log.e("granted", "failed")

                }
            }
        }
    }


}
