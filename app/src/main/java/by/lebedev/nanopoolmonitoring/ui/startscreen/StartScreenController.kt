package by.lebedev.nanopoolmonitoring.ui.startscreen

import android.content.Context
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.itemAddAccount
import by.lebedev.nanopoolmonitoring.itemWallet
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class StartScreenController @Inject constructor(
    val context: Context,
) : TypedEpoxyController<StartScreenViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onWalletClick(wallet: Wallet)
        fun onAddWalletClick()
        fun onWalletDeleteClick(wallet: Wallet)
        fun onWalletEditClick(wallet: Wallet)
    }

    override fun buildModels(data: StartScreenViewState?) {
        data?.wallets?.forEachIndexed { index, item ->
            itemWallet {
                id("wallet_${index}")
                walletName(item.walletName)
                walletNumber(item.walletAddress)
                walletCoinRes(
                    ContextCompat.getDrawable(
                        this@StartScreenController.context,
                        item.walletCoin.coinImageRes
                    )
                )
                onWalletClick { _ ->
                    this@StartScreenController.callbacks?.onWalletClick(item)
                }
                onWalletMenuClick { view ->
                    PopupMenu(this@StartScreenController.context, view)
                        .apply {
                            menuInflater.inflate(R.menu.wallet_menu, menu)
                            setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.edit_wallet -> {
                                        this@StartScreenController.callbacks?.onWalletEditClick(
                                            item
                                        )
                                        true
                                    }
                                    R.id.delete_wallet -> {
                                        this@StartScreenController.callbacks?.onWalletDeleteClick(
                                            item
                                        )
                                        true
                                    }
                                    else -> {
                                        true
                                    }
                                }
                            }
                        }.show()
                }
            }
        }

        itemAddAccount {
            id("add_account_item")
            onAddAccountClick { _ ->
                this@StartScreenController.callbacks?.onAddWalletClick()
            }
        }
    }
}