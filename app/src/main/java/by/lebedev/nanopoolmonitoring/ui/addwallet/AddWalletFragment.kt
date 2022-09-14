package by.lebedev.nanopoolmonitoring.ui.addwallet


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentAddWalletBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.utils.*
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.*
import com.afollestad.materialdialogs.MaterialDialog
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddWalletFragment : NanopoolFragment<FragmentAddWalletBinding>() {

    @Inject
    lateinit var controller: AddWalletController

    override val viewModel: AddWalletViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentAddWalletBinding> =
        FragmentAddWalletBinding::inflate


    override fun onViewCreated(binding: FragmentAddWalletBinding, savedInstanceState: Bundle?) {
        withBinding {
            withState(viewModel) {
                state = it
            }
            onNavigateUp = View.OnClickListener { findNavController().popBackStack() }
            controller.callbacks = controllerCallbacks
            addingRv.setController(controller)
        }
        viewModel.onWalletNotExist.observeEvent(viewLifecycleOwner) {
            showWalletNotFoundAlert(it)
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }

    private fun showWalletNotFoundAlert(message: String) {
        MaterialDialog(requireContext()).apply {
            title(text = message)
            message(R.string.error_wallet_not_exist)
            positiveButton(res = R.string.common_ok) {
                dismiss()
            }
        }.show()
    }

    private val controllerCallbacks = object : AddWalletController.Callbacks {
        override fun onCoinSelected(coinTicker: CoinTicker) {
            viewModel.onCoinSelected(coinTicker)
        }

        override fun onWalletNameChanged(input: Input) {
            viewModel.onWalletNameChanged(input)
        }

        override fun onWalletNumberChanged(input: Input) {
            viewModel.onWalletNumberChanged(input)
        }

        override fun onButtonAddClick() {
            viewModel.checkWalletAndInsert()
        }
    }
}