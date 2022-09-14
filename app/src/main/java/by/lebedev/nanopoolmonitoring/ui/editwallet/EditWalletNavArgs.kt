package by.lebedev.nanopoolmonitoring.ui.editwallet

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import kotlinx.parcelize.Parcelize

@Parcelize
class EditWalletNavArgs(
    val wallet: Wallet,
    val isFromWidgetConfigure: Boolean = false
) : Parcelable
