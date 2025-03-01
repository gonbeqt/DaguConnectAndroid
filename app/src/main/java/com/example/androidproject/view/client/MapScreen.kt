import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@SuppressLint("ClickableViewAccessibility")
@Composable
fun MapScreen(context: Context, viewModel: MapViewModel = viewModel()) {
    var mapView by remember { mutableStateOf<MapView?>(null) }

    AndroidView(
        factory = {
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                val controller = controller
                controller.setZoom(10.0)
                controller.setCenter(GeoPoint(14.5995, 120.9842)) // Manila, example

                setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val projection = projection
                        val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                        viewModel.setLocation(geoPoint.latitude, geoPoint.longitude)
                        addMarker(this, geoPoint.latitude, geoPoint.longitude)
                    }
                    false
                }

                mapView = this
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { map ->
            viewModel.jobLocations.forEach { job ->
                addMarker(map, job.latitude, job.longitude)
            }
        }
    )
}

fun addMarker(map: MapView, lat: Double, lng: Double) {
    val marker = Marker(map).apply {
        position = GeoPoint(lat, lng)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        title = "Job Location"
    }
    map.overlays.add(marker)
    map.invalidate()
}
