package by.lebedev.nanopoolmonitoring.data.entities.wallet

import android.os.Parcelable
import by.lebedev.nanopoolmonitoring.utils.Coin
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Wallet(
    val serverId: Long = Calendar.getInstance().timeInMillis,
    val walletName: String,
    val walletAddress: String,
    val walletCoin: Coin
):Parcelable
