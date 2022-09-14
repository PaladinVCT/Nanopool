package by.lebedev.nanopoolmonitoring.utils.epoxymodels

import android.text.method.DigitsKeyListener
import android.text.method.KeyListener

/**
 * Implementation of KeyListener by delegation to DigitsKeyListener
 * The main goal is to implement comparison methods
 */
class DigitsSmartKeyListener private constructor(
    val accepted: String,
    private val digitsKeyListener: DigitsKeyListener
) : KeyListener by digitsKeyListener {

    constructor(accepted: String) : this(accepted, DigitsKeyListener.getInstance(accepted))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DigitsSmartKeyListener

        if (accepted != other.accepted) return false

        return true
    }

    override fun hashCode(): Int {
        return accepted.hashCode()
    }

}