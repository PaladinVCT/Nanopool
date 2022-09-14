package by.lebedev.nanopoolmonitoring.ui.workers.details

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentWorkerDetailBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment
import com.airbnb.epoxy.EpoxyVisibilityTracker
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkerDetailsFragment : NanopoolFragment<FragmentWorkerDetailBinding>() {

    @Inject
    lateinit var controller: WorkerDetailsController

    override val viewModel: WorkerDetailsViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentWorkerDetailBinding> =
        FragmentWorkerDetailBinding::inflate

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    override fun onViewCreated(binding: FragmentWorkerDetailBinding, savedInstanceState: Bundle?) {

        withBinding {
            withState(viewModel) {
                state = it
            }
            onNavigateUp = View.OnClickListener { findNavController().popBackStack() }
            title = requireContext().getString(R.string.worker_details, state?.workerName)
            controller.callbacks = controllerCallbacks
            dashboardRv.setController(controller)
            swRefresh.setOnRefreshListener {
                withState(viewModel){state->
                    viewModel.init(state.workerName.orEmpty())
                    swRefresh.isRefreshing = false
                }
            }
            val epoxyVisibilityTracker = EpoxyVisibilityTracker()
            epoxyVisibilityTracker.attach(dashboardRv)
        }

    }

    override fun onResume() = withState(viewModel) { state->
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = false
        viewModel.init(state.workerName.orEmpty())
        if (sp.getBoolean(OptionsFragment.AUTO_UPDATE_ENABLED, false)) viewModel.startAutoUpdate(state.workerName.orEmpty())
        super.onResume()
    }

    override fun onStop() {
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = true
        viewModel.stopAutoUpdate()
        super.onStop()
    }

    private val controllerCallbacks = object : WorkerDetailsController.Callbacks {
    }
}