package by.lebedev.nanopoolmonitoring.ui.coins

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentCoinsBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.utils.setupOnBackPressed
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoinsFragment : NanopoolFragment<FragmentCoinsBinding>() {

    @Inject
    lateinit var controller: CoinsController

    override val viewModel: CoinsViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentCoinsBinding> =
        FragmentCoinsBinding::inflate

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    override fun onViewCreated(binding: FragmentCoinsBinding, savedInstanceState: Bundle?) {
        setupOnBackPressed(viewLifecycleOwner,sp)
        withBinding {
            withState(viewModel) {
                state = it
            }
            swRefresh.setOnRefreshListener {
                viewModel.loadAllCoins()
                swRefresh.isRefreshing = false
            }
            controller.callbacks = controllerCallbacks
            coinsRv.setController(controller)

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
                setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?) = true
                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.onQueryTextChange(newText)
                        return true
                    }
                })
            }
        }

    }

    private val controllerCallbacks = object : CoinsController.Callbacks {

    }

    override fun onResume() {
        (requireActivity() as MainScreenActivity).binding?.navView?.isVisible = true
        (requireActivity() as MainScreenActivity).checkPremiumAndStartAdsTimer()
        super.onResume()
    }

}