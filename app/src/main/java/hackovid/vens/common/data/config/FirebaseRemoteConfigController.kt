package hackovid.vens.common.data.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import hackovid.vens.R

class FirebaseRemoteConfigController(private val firebase: FirebaseRemoteConfig) {
    fun setup() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600 * 3)
            .build()
        firebase.setConfigSettingsAsync(configSettings)
        firebase.setDefaultsAsync(R.xml.remote_config_defaults)
    }
    fun fetchAndActivate() {
        firebase.fetchAndActivate()
    }
}
