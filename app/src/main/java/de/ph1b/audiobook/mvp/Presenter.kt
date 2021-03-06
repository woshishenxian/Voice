package de.ph1b.audiobook.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import de.ph1b.audiobook.misc.checkMainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class Presenter<V : Any> {

  val view: V
    get() {
      checkMainThread()
      return internalView!!
    }
  private var internalView: V? = null

  private val compositeDisposable = CompositeDisposable()

  @CallSuper
  open fun onRestore(savedState: Bundle) {
    checkMainThread()
  }

  fun attach(view: V) {
    Timber.i("attach $view")
    checkMainThread()
    check(internalView == null) {
      "$internalView already bound."
    }

    internalView = view
    onAttach(view)
  }

  fun detach() {
    Timber.i("detach $view")
    checkMainThread()
    compositeDisposable.clear()
    internalView = null
  }

  @CallSuper
  open fun onSave(state: Bundle) {
    checkMainThread()
  }

  open fun onAttach(view: V) {}

  fun Disposable.disposeOnDetach() {
    checkMainThread()
    check(internalView != null) { "Must only be called in attached state." }
    compositeDisposable.add(this)
  }
}
