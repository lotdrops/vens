package hackovid.vens.common.data.config

interface RemoteConfig {
    val hoursIntervalRefreshStores: Long
    val registerNewUserEnabled: Boolean
    val minForcedVersion: Long
}
