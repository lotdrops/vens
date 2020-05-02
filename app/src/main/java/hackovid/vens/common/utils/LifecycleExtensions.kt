package hackovid.vens.common.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Utils to observe liveData in a cleaner way from an Activity.
 */
inline fun <T> AppCompatActivity.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    liveData.observe(this, Observer { observer(it) })
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner way from an Activity.
 */
inline fun <T> AppCompatActivity.observeOnce(
    liveData: LiveData<T>,
    crossinline observer: (T) -> Unit
) {
    liveData.observe(this, object : Observer<T> {
        override fun onChanged(t: T) {
            liveData.removeObserver(this)
            observer(t)
        }
    })
}

/**
 * Utils to observe liveData in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { observer(it) })
}

/**
 * Utils to observe a liveData FOR ONE VALUE in a cleaner and safe way from a Fragment.
 *
 * It enforces observing viewLifecycleOwner to avoid bugs due to Fragments' view lifecycle not
 * being tied to the enclosing Fragment's lifecycle
 */
inline fun <T> Fragment.observeOnce(liveData: LiveData<T>, crossinline observer: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            liveData.removeObserver(this)
            observer(t)
        }
    })
}
