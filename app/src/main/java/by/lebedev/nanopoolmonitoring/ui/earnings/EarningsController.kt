package by.lebedev.nanopoolmonitoring.ui.earnings


import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import by.lebedev.nanopoolmonitoring.*
import by.lebedev.nanopoolmonitoring.utils.*
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.Input
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.OnInputChanged
import by.lebedev.nanopoolmonitoring.utils.epoxymodels.inputView
import com.airbnb.epoxy.TypedEpoxyController
import javax.inject.Inject

class EarningsController @Inject constructor(
    private val context: Context
) : TypedEpoxyController<EarningsViewState>() {

    var callbacks: Callbacks? = null

    interface Callbacks {
        fun onCoinPriceFiatSelected(position: Int)
        fun onEarningsFiatSelected(position: Int)
        fun onAthFiatSelected(position: Int)
        fun onAthChangeFiatSelected(position: Int)
        fun onCalculatorFiatSelected(position: Int)
        fun onCalculatorClick()
        fun onHashrateChange(input: Input)
    }

    override fun buildModels(data: EarningsViewState) {

        earningCoinInfo {
            id("earning_coin_info")
            sessionBearer(SessionBearer)
            coinPrice(data.coinFiatPrice)
            shimmerTrigger(data.isLoading)
            fiatSelection(data.globalFiatSelection)
            spinnerListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    this@EarningsController.callbacks?.onCoinPriceFiatSelected(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            })

        }

        title {
            id("shares_chart_title")
            title(this@EarningsController.context.getString(R.string.approximated_earnings))
        }

        earningsTable {
            id("earnings_table")
            aprx(data.approximatedEarnings)
            fiat(data.earningsSelectedFiat)
            isLoading(data.isLoading)
            shimmerTrigger(data.isLoading)
            fiatSelection(data.globalFiatSelection)
            spinnerListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    this@EarningsController.callbacks?.onEarningsFiatSelected(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            })
        }


        if (data.coinAnalytics != null) {

            if (data.coinAnalytics.sentimentVotesDownPercentage != 0.0 && data.coinAnalytics.sentimentVotesUpPercentage != 0.0) {

                title {
                    id("sentiments_title")
                    title(this@EarningsController.context.getString(R.string.sentiments_title))
                }

                sentiments {
                    id("sentiments")
                    shimmerTrigger(data.isLoading)
                    upPercent(data.coinAnalytics.sentimentVotesUpPercentage.toFloat())
                    downPercent(data.coinAnalytics.sentimentVotesDownPercentage.toFloat())
                    upPercentRes(
                        this@EarningsController.context.getString(
                            R.string.sentiments_up_res,
                            data.coinAnalytics.sentimentVotesUpPercentage.toFloat()
                        )
                    )
                    downPercentRes(
                        this@EarningsController.context.getString(
                            R.string.sentiments_down_res,
                            data.coinAnalytics.sentimentVotesDownPercentage.toFloat()
                        )
                    )
                }
            }

            title {
                id("ath_title")
                title(
                    this@EarningsController.context.getString(
                        R.string.ath_title,
                        SessionBearer.wallet?.walletCoin?.name?.capitalizeFirstLetter()
                    )
                )
            }

            coinAth {
                id("coin_ath")
                shimmerTrigger(data.isLoading)
                coinAthPrice(data.coinAthPrice)
                fiatSelection(data.globalFiatSelection)
                spinnerListener(object : OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        this@EarningsController.callbacks?.onAthFiatSelected(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                })
            }

            coinAthChange {
                id("coin_ath_change")
                shimmerTrigger(data.isLoading)
                coinAthChange(data.coinAthChange)
                fiatSelection(data.globalFiatSelection)
                spinnerListener(object : OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        this@EarningsController.callbacks?.onAthChangeFiatSelected(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                })
            }
        }

        buttonCalculatedEarnings {
            id("calculated_earnings_button")
            state(data)
            onClick { _ ->
                this@EarningsController.callbacks?.onCalculatorClick()
            }
        }

        if (data.isCalculatorExpanded) {
            inputView {
                id("entered_hashrate")
                tag("entered_hashrate")
                hint(R.string.common_hashrate)
                input(Input(data.enteredHashrate))
                inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER)
                padding(
                    PaddingView.resource(
                        topRes = R.dimen.top_text_input_with_error
                    ).horizontal(R.dimen.activity_horizontal_space)
                        .vertical(R.dimen.activity_horizontal_space)
                )
                onChange(OnInputChanged {
                    this@EarningsController.callbacks?.onHashrateChange(it)
                })
            }

            earningsTable {
                id("calculator_earnings_table")
                aprx(data.calculatorApproximatedEarnings)
                fiat(data.calculatorSelectedFiat)
                isLoading(data.isCalculatorLoading)
                fiatSelection(data.globalFiatSelection)
                shimmerTrigger(data.isCalculatorLoading)
                spinnerListener(object : OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        this@EarningsController.callbacks?.onCalculatorFiatSelected(position)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                })
            }
        }
        buildPoolInfo(data)
    }

    private fun buildPoolInfo(data: EarningsViewState) {

        title {
            id("pool_info")
            title(
                this@EarningsController.context.getString(R.string.pool_info)
            )
        }

        commonInfoBlock {
            id("nanopool_miners_number")
            titleInfo(
                this@EarningsController.context.getString(
                    R.string.number_of_miners
                )
            )
            valueInfo(
                if (data.isLoading) this@EarningsController.context.getString(R.string.common_loading) else data.nanopoolMinersNumber?.data?.toInt()
                    .toString()
            )
            shimmerTrigger(data.isLoading)
        }

        commonInfoBlock {
            id("nanopool_hashrate")
            titleInfo(
                this@EarningsController.context.getString(
                    R.string.pool_hashrate
                )
            )
            valueInfo(
                if (data.isLoading) this@EarningsController.context.getString(R.string.common_loading) else data.nanopoolHashrate?.data?.roundPlusHashrate(
                    2
                ).toString()
            )
            shimmerTrigger(data.isLoading)
        }

        commonInfoBlock {
            id("nanopool_share_coef")
            titleInfo(
                this@EarningsController.context.getString(
                    R.string.pool_share_coef
                )
            )
            valueInfo(
                if (data.isLoading) this@EarningsController.context.getString(R.string.common_loading) else data.nanopoolShareCoefficient?.data.toString()
            )
            shimmerTrigger(data.isLoading)
        }
    }
}
