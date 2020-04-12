package hackovid.vens.features.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
    clusterManager:
    ClusterManager<ClusterStoreItem>
) : DefaultClusterRenderer<ClusterStoreItem>(context, map, clusterManager), GoogleMap.OnMarkerClickListener{

    override fun onBeforeClusterItemRendered(
        item: ClusterStoreItem?,
        markerOptions: MarkerOptions?
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        val icon = yellowMarkerBitmap()
        markerOptions?.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    private fun yellowMarkerBitmap(): Bitmap? {
        return BitmapFactory.decodeResource(
            context.resources,
            R.drawable.white_circle
        ).copy(Bitmap.Config.ARGB_8888, true)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterStoreItem>?): Boolean {
        return cluster?.size!! > 10
    }

    private var previousMarker: Marker? = null
    override fun onMarkerClick(marker: Marker?): Boolean {
        Log.d("asdf","asdfasdfasdf")
        previousMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(yellowMarkerBitmap()))
        marker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        previousMarker = marker;
        return true;
    }

}


