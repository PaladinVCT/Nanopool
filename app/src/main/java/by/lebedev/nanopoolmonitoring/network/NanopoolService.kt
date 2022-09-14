package by.lebedev.nanopoolmonitoring.network

import by.lebedev.nanopoolmonitoring.data.dto.approximated.ApproximatedEarningsResponse
import by.lebedev.nanopoolmonitoring.data.dto.hashrate.AverageHashratesResponse
import by.lebedev.nanopoolmonitoring.data.dto.balance.BalanceResponse
import by.lebedev.nanopoolmonitoring.data.dto.chart.CalculatedChartDataResponse
import by.lebedev.nanopoolmonitoring.data.dto.chart.ChartDataResponse
import by.lebedev.nanopoolmonitoring.data.dto.general.GeneralInfoResponse
import by.lebedev.nanopoolmonitoring.data.dto.hashrate.HashrateResponse
import by.lebedev.nanopoolmonitoring.data.dto.payout.PayoutLimitResponse
import by.lebedev.nanopoolmonitoring.data.dto.pool.NanopoolPoolInfoResponse
import by.lebedev.nanopoolmonitoring.data.dto.prices.CoinFiatPricesResponse
import by.lebedev.nanopoolmonitoring.data.dto.wallet.WalletExistResponse
import by.lebedev.nanopoolmonitoring.data.dto.workers.WorkersResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NanopoolService {

    @GET("{coin}/accountexist/{wallet}")
    suspend fun checkWalletExists(
        @Path("coin") coinTicker: String,
        @Path("wallet") address: String
    ): WalletExistResponse

    @GET("{coin}/avghashrate/{account}")
    suspend fun loadAverageAccountHashrates(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): AverageHashratesResponse

    @GET("{coin}/reportedhashrate/{account}")
    suspend fun loadLastReportedHashrate(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): HashrateResponse

    @GET("{coin}/hashrate/{account}")
    suspend fun loadCurrentHashrate(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): HashrateResponse

    @GET("{coin}/balance/{account}")
    suspend fun loadBalance(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): BalanceResponse

    @GET("{coin}/hashratechart/{account}")
    suspend fun loadChartData(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): ChartDataResponse

    @GET("{coin}/history/{account}")
    suspend fun loadCalculatedChartData(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): CalculatedChartDataResponse

    @GET("{coin}/approximated_earnings/{hashrate}")
    suspend fun loadApproximatedEarnings(
        @Path("coin") coinTicker: String,
        @Path("hashrate") hashrate: Double
    ): ApproximatedEarningsResponse

    @GET("{coin}/usersettings/{account}")
    suspend fun loadPayoutSettings(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): PayoutLimitResponse

    @GET("{coin}/user/{account}")
    suspend fun loadGeneralInfo(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): GeneralInfoResponse

    @GET("{coin}/workers/{account}")
    suspend fun loadWorkers(
        @Path("coin") coinTicker: String,
        @Path("account") account: String
    ): WorkersResponse

    @GET("{coin}/avghashrate/{account}/{workerName}")
    suspend fun loadAverageWorkerHashrates(
        @Path("coin") coinTicker: String,
        @Path("account") account: String,
        @Path("workerName") workerName: String
    ): AverageHashratesResponse

    @GET("{coin}/reportedhashrate/{account}/{workerName}")
    suspend fun loadWorkerLastReportedHashrate(
        @Path("coin") coinTicker: String,
        @Path("account") account: String,
        @Path("workerName") workerName: String
    ): HashrateResponse

    @GET("{coin}/hashrate/{account}/{workerName}")
    suspend fun loadWorkerCurrentHashrate(
        @Path("coin") coinTicker: String,
        @Path("account") account: String,
        @Path("workerName") workerName: String
    ): HashrateResponse

    @GET("{coin}/hashratechart/{account}/{workerName}")
    suspend fun loadWorkerChartData(
        @Path("coin") coinTicker: String,
        @Path("account") account: String,
        @Path("workerName") workerName: String
    ): ChartDataResponse

    @GET("{coin}/prices")
    suspend fun loadCoinFiatPrices(
        @Path("coin") coinTicker: String
    ): CoinFiatPricesResponse

    @GET("{coin}/pool/activeminers")
    suspend fun loadNanopoolMinersNumber(
        @Path("coin") coinTicker: String
    ): NanopoolPoolInfoResponse

    @GET("{coin}/pool/hashrate")
    suspend fun loadNanopoolHashrate(
        @Path("coin") coinTicker: String
    ): NanopoolPoolInfoResponse

    @GET("{coin}/pool/sharecoef")
    suspend fun loadNanopoolShareCoefficient(
        @Path("coin") coinTicker: String
    ): NanopoolPoolInfoResponse
}