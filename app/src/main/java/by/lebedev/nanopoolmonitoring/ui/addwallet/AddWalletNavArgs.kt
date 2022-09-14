package by.lebedev.nanopoolmonitoring.ui.addwallet

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import kotlinx.parcelize.Parcelize

@Parcelize
class AddWalletNavArgs(val isFromWidgetConfigure: Boolean = false) : Parcelable
