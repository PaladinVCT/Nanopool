package by.lebedev.nanopoolmonitoring.ui.options

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import kotlinx.parcelize.Parcelize

@Parcelize
class OptionsNavArgs(val isNotificationsEnabled: Boolean) : Parcelable
