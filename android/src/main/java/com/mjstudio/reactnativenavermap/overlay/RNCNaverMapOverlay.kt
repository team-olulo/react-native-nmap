package com.mjstudio.reactnativenavermap.overlay

import android.content.Context
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.view.ReactViewGroup
import com.mjstudio.reactnativenavermap.event.NaverMapOverlayTapEvent
import com.mjstudio.reactnativenavermap.util.emitEvent
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay

abstract class RNCNaverMapOverlay<T : Overlay>(
  context: Context?,
) : ReactViewGroup(context) {
  abstract val overlay: T
  private var ignoreTouch = false

  abstract fun addToMap(map: NaverMap)

  abstract fun removeFromMap(map: NaverMap)

  abstract fun onDropViewInstance()

  fun setIgnoreTouch(value: Boolean) {
    ignoreTouch = value
    if (ignoreTouch) overlay.onClickListener = null
    else initClickListener(overlay)
  }

  fun initClickListener(o: T) {
    if (o.onClickListener != null) return
    o.setOnClickListener {
      val reactContext = context as? ThemedReactContext
      reactContext?.emitEvent(id) { surfaceId, reactTag ->
        NaverMapOverlayTapEvent(surfaceId, reactTag)
      }
      true
    }
  }
}
