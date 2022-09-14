package by.lebedev.nanopoolmonitoring.ui.widget.configure

import android.content.Context
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.data.entities.wallet.Wallet
import by.lebedev.nanopoolmonitoring.itemAddAccount
import by.lebedev.nanopoolmonitoring.itemWallet
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class ConfigureWidgetController @Inject constructor(
    val context: Context,
) : TypedEpoxyController<ConfigureWidgetViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onWalletClick(wallet: Wallet)
        fun onAddWalletClick()
        fun onWalletDeleteClick(wallet: Wallet)
        fun onWalletEditClick(wallet: Wallet)
    }

    override fun buildModels(data: ConfigureWidgetViewState?) {
        data?.wallets?.forEachIndexed { index, item ->
            itemWallet {
                id("wallet_${index}")
                walletName(item.walletName)
                walletNumber(item.walletAddress)
                walletCoinRes(
                    ContextCompat.getDrawable(
                        this@ConfigureWidgetController.context,
                        item.walletCoin.coinImageRes
                    )
                )
                onWalletClick { _ ->
                    this@ConfigureWidgetController.callbacks?.onWalletClick(item)
                }
                onWalletMenuClick { view ->
                    PopupMenu(this@ConfigureWidgetController.context, view)
                        .apply {
                            menuInflater.inflate(R.menu.wallet_menu, menu)
                            setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.edit_wallet -> {
                                        this@ConfigureWidgetController.callbacks?.onWalletEditClick(
                                            item
                                        )
                                        true
                                    }
                                    R.id.delete_wallet -> {
                                        this@ConfigureWidgetController.callbacks?.onWalletDeleteClick(
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
                this@ConfigureWidgetController.callbacks?.onAddWalletClick()
            }
        }
    }
}