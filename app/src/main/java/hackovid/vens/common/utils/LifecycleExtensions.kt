package hackovid.vens.common.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Utils to observe liveData in a cleaner way from an Activity.
 */
fun <T> AppCompatActivity.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    observeLifeCycle(liveData, observer)
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner way from an Activity.
 */
fun <T> AppCompatActivity.observeOnce(liveData: LiveData<T>, observer: (T) -> Unit) {
    observeLifeCycleOnce(liveData, observer)
}

/**
 * Utils to observe liveData in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
fun <T> Fragment.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    viewLifecycleOwner.observeLifeCycle(liveData, observer)
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
fun <T> Fragment.observeOnce(liveData: LiveData<T>, observer: (T) -> Unit) {
    viewLifecycleOwner.observeLifeCycleOnce(liveData, observer)
}

/**
 * Utils to observe liveData in a cleaner way from any LifecycleOwner.
 *
 * This should not be used with Fragment as lifecycleOwner
 */
fun <T> LifecycleOwner.observeLifeCycle(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this, Observer { observer(it) })
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner way from any LifecycleOwner.
 *
 * This should not be used with Fragment as lifecycleOwner
 */
fun <T> LifecycleOwner.observeLifeCycleOnce(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this, object : Observer<T> {
        override fun onChanged(t: T) {
            observer(t)
            liveData.removeObserver(this)
        }
    })
}
