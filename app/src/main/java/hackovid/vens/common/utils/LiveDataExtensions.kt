package hackovid.vens.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block.invoke(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block.invoke(this.value, liveData.value)
    }
    return result
}

fun <T> combineLiveDatas(
    vararg liveDatas: LiveData<out Any>,
    block: () -> T
): LiveData<T> {
    val result = MediatorLiveData<T>()
    liveDatas.forEach { source ->
        result.addSource(source) {
            result.value = block.invoke()
        }
    }
    return result
}
