package hackovid.vens.features.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import hackovid.vens.R

class ClusterStoreRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<ClusterStoreItem>
) : DefaultClusterRenderer<ClusterStoreItem>(context, map, clusterManager) {
    private var previousMarker: Marker? = null

    override fun onBeforeClusterItemRendered(
        item: ClusterStoreItem?,
        markerOptions: MarkerOptions?
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        val icon = purpleMarkerBitmap()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    private fun purpleMarkerBitmap(): Bitmap? =
        ContextCompat.getDrawable(context, R.drawable.ic_place_purple)
            ?.getBitmap()

    private fun Drawable?.getBitmap(): Bitmap? {
        if (this !is VectorDrawable) return null
        val bitmap = Bitmap.createBitmap(
            intrinsicWidth,
            intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterStoreItem>): Boolean {
        return cluster.size > 10
    }

    fun onClusterItemSelected(item: ClusterStoreItem?) {
        if (previousMarker.wasRemoved()) {
            previousMarker = null
        } else {
            previousMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(purpleMarkerBitmap()))
        }
        if (item != null) {
            previousMarker = getMarker(item)
            previousMarker?.markNotRemoved()
            previousMarker?.setIcon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }
    }

    /**
     * When a marker is removed its tag is set to null.
     * Adding a non null tag allows us to detect if it has been removed
     */
    private fun Marker.markNotRemoved() {
        tag = Unit
    }

    private fun Marker?.wasRemoved() = this?.tag == null
}
