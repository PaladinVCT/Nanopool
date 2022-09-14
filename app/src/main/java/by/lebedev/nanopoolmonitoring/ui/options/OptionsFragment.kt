package by.lebedev.nanopoolmonitoring.ui.options


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.work.*
import androidx.work.WorkInfo.State
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.databinding.FragmentOptionsBinding
import by.lebedev.nanopoolmonitoring.ui.BindingInflater
import by.lebedev.nanopoolmonitoring.ui.NanopoolFragment
import by.lebedev.nanopoolmonitoring.ui.mainscreen.MainScreenActivity
import by.lebedev.nanopoolmonitoring.utils.SessionBearer
import by.lebedev.nanopoolmonitoring.utils.autoupdate.AutoUpdateWorker
import by.lebedev.nanopoolmonitoring.utils.checkPremium
import by.lebedev.nanopoolmonitoring.utils.getPremium
import by.lebedev.nanopoolmonitoring.utils.rateApp
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.materialdialogs.utils.MDUtil.getStringArray
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class OptionsFragment : NanopoolFragment<FragmentOptionsBinding>() {

    companion object {
        const val SELECTED_FIAT = "selected fiat"
        const val AUTO_UPDATE_ENABLED = "auto update enabled"
        const val AUTO_LOGIN_ACCOUNT = "auto login account"
        const val IS_LOGGED_OUT = "is logged out"
        const val IS_PREMIUM = "is premium"
        const val ADS_INTERNAL = "ads interval"
    }

    @Inject
    lateinit var controller: OptionsController

    override val viewModel: OptionsViewModel by fragmentViewModel()

    override val bindingInflater: BindingInflater<FragmentOptionsBinding> =
        FragmentOptionsBinding::inflate


    override fun onViewCreated(binding: FragmentOptionsBinding, savedInstanceState: Bundle?) {
        withBinding {
            withState(viewModel) {
                state = it
            }
            onNavigateUp = View.OnClickListener { findNavController().navigateUp() }

            setOnNotificationInfoClick {
                MaterialDialog(requireContext()).apply {
                    title(R.string.information)
                    message(R.string.information_notifications)
                    positiveButton(res = R.string.common_ok) {
                        dismiss()
                    }
                }.show()
            }
            setOnAutoLoginInfoClick {
                MaterialDialog(requireContext()).apply {
                    title(R.string.information)
                    message(R.string.information_autologin)
                    positiveButton(res = R.string.common_ok) {
                        dismiss()
                    }
                }.show()
            }
            setOnAutoupdateInfoClick {
                MaterialDialog(requireContext()).apply {
                    title(R.string.information)
                    message(R.string.information_autoupdate)
                    positiveButton(res = R.string.common_ok) {
                        dismiss()
                    }
                }.show()
            }

            checkNotificationsEnqueued(requireBaseContext())

            setOnNotificationsClick {
                if ((it as SwitchCompat).isChecked) {
                    enqueueWorker(requireBaseContext())
                } else cancelNotifications(requireBaseContext())
            }

            setOnAutoLoginClick {
                withState(viewModel) { state ->
                    if ((it as SwitchCompat).isChecked) {
                        MaterialDialog(requireContext()).show {
                            title(R.string.title_autologin)
                            onDismiss {
                                isAutoLoginChecked = sp.getString(AUTO_LOGIN_ACCOUNT, null) != null
                            }
                            listItemsSingleChoice(
                                items = state.accounts.map { "${it.walletName} ${it.walletAddress}" },
                                initialSelection = state.accounts.map { it.walletAddress }
                                    .indexOf(sp.getString(AUTO_LOGIN_ACCOUNT, null))
                            ) { _, index, text ->
                                val selectedAccount = state.accounts.map { it.walletAddress }[index]
                                isAutoLoginChecked = true
                                autoLoginAccount = selectedAccount
                                sp.edit().putString(AUTO_LOGIN_ACCOUNT, selectedAccount).apply()
                            }
                        }
                    } else {
                        sp.edit().putString(AUTO_LOGIN_ACCOUNT, null).apply()
                        isAutoLoginChecked = false
                    }
                }
            }

            setOnPremiumInfoClick {
                MaterialDialog(requireContext()).apply {
                    title(R.string.information)
                    message(R.string.information_premium)
                    positiveButton(res = R.string.common_ok) {
                        dismiss()
                    }
                    negativeButton(res = R.string.common_restore) {
                        viewModel.restorePremium { isSuccess ->
                            if (isSuccess) {
                                Snackbar.make(
                                    binding.root,
                                    R.string.subscription_restored,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    R.string.subscription_not_found,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                        dismiss()
                    }
                }.show()
            }

            setOnAutoUpdateClick {
                sp.edit().putBoolean(AUTO_UPDATE_ENABLED, (it as SwitchCompat).isChecked).apply()
            }

            isAutoUpdateChecked = sp.getBoolean(AUTO_UPDATE_ENABLED, false)
            isAutoLoginChecked = sp.getString(AUTO_LOGIN_ACCOUNT, null) != null
            autoLoginAccount = sp.getString(AUTO_LOGIN_ACCOUNT, null)

            selectedFiat =
                sp.getString(
                    SELECTED_FIAT,
                    requireContext().getStringArray(R.array.fiatCurrency)[0]
                )

            setOnCurrencyClick {
                MaterialDialog(requireContext()).show {
                    listItemsSingleChoice(
                        R.array.fiatCurrency,
                        initialSelection = requireContext().getStringArray(R.array.fiatCurrency)
                            .indexOf(selectedFiat)
                    ) { _, _, text ->
                        selectedFiat = text.toString()
                        sp.edit().putString(SELECTED_FIAT, text.toString()).apply()
                    }
                }
            }

            setOnPremiumClick {
                getPremium()
            }

            setOnRateAppClick {
                rateApp()
            }

            viewModel.onEach(OptionsViewState::isPremium) {
                with( binding.premiumTv){
                    text = if (it) requireContext().getString(R.string.you_have_premium) else requireContext().getString(
                        R.string.get_premium
                    )
                    val color = if (it) ContextCompat.getColor(requireContext(),R.color.premium_gold) else ContextCompat.getColor(requireContext(),R.color.white)
                setTextColor(color)
                }
            }
        }
    }

    private fun enqueueWorker(context: Context) {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<AutoUpdateWorker>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SessionBearer.wallet?.walletAddress.orEmpty(),
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

    private fun checkNotificationsEnqueued(context: Context) {
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkLiveData(SessionBearer.wallet?.walletAddress.orEmpty())
            .observe(viewLifecycleOwner) {
                requireBinding().isNotificationsChecked =
                    (it.isNotEmpty() && it[0].state != State.CANCELLED)
            }
    }

    private fun cancelNotifications(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(SessionBearer.wallet?.walletAddress.orEmpty())
        viewModel.cancelNotifications()
    }

    override fun onResume() {
        (requireActivity() as? MainScreenActivity)?.binding?.navView?.isVisible = false
        viewModel.checkPremium()
        super.onResume()
    }

    override fun invalidate() = withState(viewModel) { state ->
        requireBinding().state = state
        controller.setData(state)
    }
}