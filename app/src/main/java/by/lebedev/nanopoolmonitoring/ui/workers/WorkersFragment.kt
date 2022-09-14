package by.lebedev.nanopoolmonitoring.ui.workers

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentWorkersBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.utils.setupOnBackPressed
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WorkersFragment : NanopoolFragment<FragmentWorkersBinding>() {

    @Inject
    lateinit var controller: WorkersController

    override val viewModel: WorkersViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentWorkersBinding> =
        FragmentWorkersBinding::inflate

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    override fun onViewCreated(binding: FragmentWorkersBinding, savedInstanceState: Bundle?) {
        setupOnBackPressed(viewLifecycleOwner,sp)
        withBinding {
            withState(viewModel) {
                state = it
            }
            controller.callbacks = controllerCallbacks
            workersRv.setController(controller)

            onRefreshClick = View.OnClickListener {
                viewModel.loadWorkers()
            }
            onAllFilterClick = View.OnClickListener {
                viewModel.onAllFilterClick()
            }
            onAliveFilterClick = View.OnClickListener {
                viewModel.onAliveFilterClick()
            }
            onDeadFilterClick = View.OnClickListener {
                viewModel.onDeadFilterClick()
            }
            totalWokersBtn.setOnClickListener {
                viewModel.onTotalWorkersClick()
            }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.options -> {
                        viewModel.onOptionsClick()
                        true
                    }
                    R.id.logout -> {
                        requireActivity().finish()
                        true
                    }
                    else -> true
                }
            }
            val actionSearch = toolbar.menu.findItem(R.id.action_search).actionView as SearchView
            actionSearch.apply {
                queryHint = getString(R.string.search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?) = true
                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.onQueryTextChange(newText)
                        return true
                    }
                })
            }
        }

        viewModel.loadWorkers()

    }

    private val controllerCallbacks = object : WorkersController.Callbacks {

        override fun onWorkerClick(
            workerName: String,
            workerRating: Long,
            workerLastShareTime: Long
        ) {
            viewModel.onWorkerClick(workerName, workerRating, workerLastShareTime)
        }
    }

    override fun onResume() {
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = true
        (requireActivity() as MainScreenActivity).checkPremiumAndStartAdsTimer()
        super.onResume()
    }

}