package hackovid.vens.common.data.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigFirebaseImpl(private val firebase: FirebaseRemoteConfig) : RemoteConfig {
    override val hoursIntervalRefreshStores: Long
        get() = firebase.getLong(RefreshStoresIntervalKey)
    override val registerNewUserEnabled: Boolean
        get() = firebase.getBoolean(RegisterNewUserEnabled)
}
private const val RefreshStoresIntervalKey = "hours_interval_refresh_stores"
private const val RegisterNewUserEnabled = "register_new_user_enabled"
