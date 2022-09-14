package by.lebedev.nanopoolmonitoring.ui.widget.configure

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.databinding.FragmentStartBinding
import by.lebedev.nanopoolmonitoring.databinding.FragmentWidgetWalletsBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.AUTO_LOGIN_ACCOUNT
import by.lebedev.nanopoolmonitoring.ui.options.OptionsFragment.Companion.IS_LOGGED_OUT
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.login
import by.lebedev.nanopoolmonitoring.utils.saveWidgetWalletAddress
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfigureWidgetFragment : NanopoolFragment<FragmentWidgetWalletsBinding>() {

    override val viewModel: ConfigureWidgetViewModel by fragmentViewModel()

    @Inject
    lateinit var controller: ConfigureWidgetController

    override val bindingInflater: BindingInflater<FragmentWidgetWalletsBinding> =
        FragmentWidgetWalletsBinding::inflate

    override fun onViewCreated(binding: FragmentWidgetWalletsBinding, savedInstanceState: Bundle?) {

        withBinding {
            controller.callbacks = controllerCallbacks
            widgetWalletsRv.setController(controller)

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
        viewModel.onEach(ConfigureWidgetViewState::wallets) { walletList ->
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

    private val controllerCallbacks = object : ConfigureWidgetController.Callbacks {
        override fun onWalletClick(wallet: Wallet) {
            (requireActivity() as WidgetConfigureCallback).onWalletSelected(wallet)
//            sp.login()
//            SessionBearer.wallet = wallet
//            viewModel.saveSession(wallet)
//            requireContext().startActivity(Intent(requireContext(), MainScreenActivity::class.java))
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

    interface WidgetConfigureCallback{
        fun onWalletSelected(wallet: Wallet)
    }
}