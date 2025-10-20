package com.example.legaapp.legaapp.legaapp.activities

class MapActivity : AppCompatActivity() {
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Configuration.getInstance().load(applicationContext, androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext))
        map = findViewById(R.id.mapView)
        map.setMultiTouchControls(true)

        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lon = intent.getDoubleExtra("LONGITUDE", 0.0)
        val title = intent.getStringExtra("TITLE") ?: "Ubicación"

        val point = GeoPoint(lat, lon)
        map.controller.setZoom(15.0)
        map.controller.setCenter(point)

        val marker = Marker(map)
        marker.position = point
        marker.title = title
        map.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
