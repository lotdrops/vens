package hackovid.vens.common.data

import hackovid.vens.common.data.config.RemoteConfig

class RemoteConfigMock(
    override val hoursIntervalRefreshStores: Long = 6,
    override val registerNewUserEnabled: Boolean = true
) : RemoteConfig
