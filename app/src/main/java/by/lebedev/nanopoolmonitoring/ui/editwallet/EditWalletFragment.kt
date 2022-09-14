package by.lebedev.nanopoolmonitoring.ui.editwallet


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentEditWalletBinding
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
class EditWalletFragment : NanopoolFragment<FragmentEditWalletBinding>() {

    @Inject
    lateinit var controller: EditWalletController

    override val viewModel: EditWalletViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentEditWalletBinding> =
        FragmentEditWalletBinding::inflate

    override fun onViewCreated(binding: FragmentEditWalletBinding, savedInstanceState: Bundle?) {
        withBinding {
            withState(viewModel) {
                state = it
            }
            onNavigateUp = View.OnClickListener { findNavController().navigateUp() }
            controller.callbacks = controllerCallbacks
            editingRv.setController(controller)
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

    private val controllerCallbacks = object : EditWalletController.Callbacks {
        override fun onCoinSelected(coinTicker: CoinTicker) {
            viewModel.onCoinSelected(coinTicker)
        }

        override fun onWalletNameChanged(input: Input) {
            viewModel.onWalletNameChanged(input)
        }

        override fun onWalletNumberChanged(input: Input) {
            viewModel.onWalletNumberChanged(input)
        }

        override fun onButtonUpdateClick() {
            viewModel.checkWalletAndUpdate()
        }
    }
}