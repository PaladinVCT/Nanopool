package by.lebedev.nanopoolmonitoring.ui.options

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class OptionsController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<OptionsViewState>() {

    var callbacks: OptionsController.Callbacks? = null

    interface Callbacks {
    }

    override fun buildModels(data: OptionsViewState) {
    }
}