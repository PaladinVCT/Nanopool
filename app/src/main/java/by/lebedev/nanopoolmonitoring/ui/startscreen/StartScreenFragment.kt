package by.lebedev.nanopoolmonitoring.ui.startscreen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.databinding.FragmentStartBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.AUTO_LOGIN_ACCOUNT
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.IS_LOGGED_OUT
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.login
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartScreenFragment : NanopoolFragment<FragmentStartBinding>() {

    override val viewModel: StartScreenViewModel by fragmentViewModel()

    @Inject
    lateinit var controller: StartScreenController

    override val bindingInflater: BindingInflater<FragmentStartBinding> =
        FragmentStartBinding::inflate

    override fun onViewCreated(binding: FragmentStartBinding, savedInstanceState: Bundle?) {

        withBinding {
            controller.callbacks = controllerCallbacks
            startRv.setController(controller)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.options -> {
                        viewModel.onOptionsClick()
                        true
                    }
                    else -> true
                }
            }
        }
        viewModel.onEach(StartScreenViewState::wallets) { walletList ->
            val autoLoginAccount = sp.getString(AUTO_LOGIN_ACCOUNT, null)
            val isLoggedOut = sp.getBoolean(IS_LOGGED_OUT, false)
            if (autoLoginAccount != null && !isLoggedOut) {
                val autoLoginWallet = walletList[walletList.map { it.walletAddress }
                    .indexOf(autoLoginAccount)]
                SessionBearer.wallet = autoLoginWallet
                viewModel.saveSession(autoLoginWallet)
                requireContext().startActivity(
                    Intent(
                        requireContext(),
                        MainScreenActivity::class.java
                    )
                )
            }
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        controller.setData(state)
    }

    private val controllerCallbacks = object : StartScreenController.Callbacks {
        override fun onWalletClick(wallet: Wallet) {
            sp.login()
            SessionBearer.wallet = wallet
            viewModel.saveSession(wallet)
            requireContext().startActivity(Intent(requireContext(), MainScreenActivity::class.java))
        }

        override fun onAddWalletClick() {
            viewModel.onAddWalletClick()
        }

        override fun onWalletDeleteClick(wallet: Wallet) {
            viewModel.deleteWallet(wallet)
        }

        override fun onWalletEditClick(wallet: Wallet) {
            viewModel.onEditWalletClick(wallet)
        }
    }

    override fun onDestroy() {
        sp.login()
        super.onDestroy()
    }
}