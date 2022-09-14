package by.lebedev.nanopoolmonitoring.ui.addwallet

import android.content.Context
import android.util.Log
import android.view.Gravity
import by.lebedev.nanopoolmonitoring.R
import by.lebedev.nanopoolmonitoring.title
import by.lebedev.nanopoolmonitoring.ui.startscreen.StartScreenController
import by.lebedev.nanopoolmonitoring.utils.*
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.*
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class AddWalletController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<AddWalletViewState>() {

    var callbacks: AddWalletController.Callbacks? = null

    interface Callbacks {
        fun onCoinSelected(coinTicker: CoinTicker)
        fun onWalletNameChanged(input: Input)
        fun onWalletNumberChanged(input: Input)
        fun onButtonAddClick()

    }

    override fun buildModels(data: AddWalletViewState) {

        coinCarousel {
            id("cardCarousel")
            models(CoinBearer.coinModels)
            currentItemSmooth(0)
            onPageChange {
                this@AddWalletController.callbacks?.onCoinSelected(CoinTicker.values()[it])
            }
        }

        title {
            id("wallet_title")
            title(this@AddWalletController.context.getString(R.string.your_wallet_name))
        }

        inputView {
            id("wallet name")
            tag("wallet name")
            hint(R.string.wallet_name)
            helperTextEnabled(true)
            helperText(R.string.optional)
            input(data.walletName)
            padding(
                PaddingView.resource(
                    topRes = R.dimen.top_text_input_with_error
                ).horizontal(R.dimen.activity_horizontal_space)
                    .vertical(R.dimen.activity_horizontal_space)
            )
            onChange(OnInputChanged {
                this@AddWalletController.callbacks?.onWalletNameChanged(it)
            })
        }
        title {
            id("wallet_address")
            title(this@AddWalletController.context.getString(R.string.your_wallet_address))
        }

        inputView {
            id("wallet address")
            tag("wallet address")
            hint(R.string.wallet_address)
            helperTextEnabled(true)
            helperText(R.string.required)
            input(data.walletAddress)
            errorEnabled(data.walletAddressErrorRes != 0)
            error(data.walletAddressErrorRes)
            padding(
                PaddingView.resource(
                    topRes = R.dimen.top_text_input_with_error
                ).horizontal(R.dimen.activity_horizontal_space)
                    .vertical(R.dimen.activity_horizontal_space)
            )
            onChange(OnInputChanged {
                this@AddWalletController.callbacks?.onWalletNumberChanged(it)
            })
        }

        buttonModel {
            id("button_add_wallet")
            text(R.string.add_wallet)
            onClick { _ ->
                this@AddWalletController.callbacks?.onButtonAddClick()
            }
            isEnabled(!data.isLoading)
            gravity(Gravity.END)
            padding(
                PaddingView.resource(
                    topRes = R.dimen.top_text_input_with_error,
                    bottomRes = R.dimen.activity_vertical_space
                ).horizontal(R.dimen.activity_horizontal_space)
            )
        }
    }
}