package com.camo.kripto.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Modern Animation Utilities
 * Provides smooth Material Design animations
 */
object AnimationUtils {

    /**
     * Fade in animation
     */
    fun fadeIn(view: View, duration: Long = 300) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(FastOutSlowInInterpolator())
            .start()
    }

    /**
     * Fade out animation
     */
    fun fadeOut(view: View, duration: Long = 300, onEnd: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(FastOutSlowInInterpolator())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    /**
     * Scale up animation (pop in effect)
     */
    fun scaleIn(view: View, duration: Long = 300) {
        view.scaleX = 0f
        view.scaleY = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(duration)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    /**
     * Slide up animation
     */
    fun slideUp(view: View, duration: Long = 300) {
        view.translationY = view.height.toFloat()
        view.visibility = View.VISIBLE
        view.animate()
            .translationY(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    /**
     * Slide down animation
     */
    fun slideDown(view: View, duration: Long = 300, onEnd: (() -> Unit)? = null) {
        view.animate()
            .translationY(view.height.toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                view.visibility = View.GONE
                onEnd?.invoke()
            }
            .start()
    }

    /**
     * Pulse animation for highlighting
     */
    fun pulse(view: View, scale: Float = 1.1f, duration: Long = 200) {
        view.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(duration)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(duration)
                    .start()
            }
            .start()
    }

    /**
     * Rotate animation
     */
    fun rotate(view: View, angle: Float, duration: Long = 300) {
        ObjectAnimator.ofFloat(view, "rotation", angle).apply {
            this.duration = duration
            interpolator = FastOutSlowInInterpolator()
            start()
        }
    }

    /**
     * Shake animation for errors
     */
    fun shake(view: View) {
        val translateX = 20f
        view.animate()
            .translationX(translateX)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .translationX(-translateX)
                    .setDuration(100)
                    .withEndAction {
                        view.animate()
                            .translationX(translateX / 2)
                            .setDuration(100)
                            .withEndAction {
                                view.animate()
                                    .translationX(0f)
                                    .setDuration(100)
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }

    /**
     * Elevation animation for Material Design
     */
    fun elevate(view: View, elevation: Float, duration: Long = 200) {
        ViewCompat.animate(view)
            .translationZ(elevation)
            .setDuration(duration)
            .setInterpolator(FastOutSlowInInterpolator())
            .start()
    }
}
