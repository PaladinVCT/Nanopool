package by.lebedev.nanopoolmonitoring.repository.nanopool.wallets


import by.lebedev.nanopoolmonitoring.data.entities.wallet.WalletExistence
import by.lebedev.nanopoolmonitoring.data.mappers.hashrate.AverageHashratesResponseMapper
import by.lebedev.nanopoolmonitoring.data.mappers.wallet.WalletExistResponseMapper
import by.lebedev.nanopoolmonitoring.di.Nanopool
import by.lebedev.nanopoolmonitoring.network.NanopoolService
import by.lebedev.nanopoolmonitoring.repository.RetrofitRunner
import by.lebedev.nanopoolmonitoring.utils.Result
import javax.inject.Inject
import javax.inject.Provider

class WalletDataSource @Inject constructor(
    @Nanopool
    private val service: Provider<NanopoolService>,
    private val walletExistResponseMapper: WalletExistResponseMapper,
    private val averageHashratesResponseMapper: AverageHashratesResponseMapper,
    private val retrofitRunner: RetrofitRunner
) {

    suspend fun checkWalletExists(
        coinTicker: String,
        address: String
    ): Result<WalletExistence> {
        return retrofitRunner.invoke(walletExistResponseMapper) {
            service.get().checkWalletExists(coinTicker, address)
        }
    }


//    suspend fun loadCard(cardId: String): Result<Card> {
//        return retrofitRunner.invoke(cardResponseMapper) {
//            service.get().getCard(cardId).single()
//        }
//    }
//
//    suspend fun loadCardStatement(
//        cardId: String,
//        dateFrom: String,
//        dateTo: String
//    ): Result<CardStatement> {
//        return retrofitRunner.invoke(cardStatementResponseMapper) {
//            service.get().getCardStatement(cardId, dateFrom, dateTo)
//        }
//    }
//
//    suspend fun loadCashByCodeHistory(): Result<List<CashByCodeHistoryItem>> {
//        return retrofitRunner.invoke(cashByCodeHistoryResponseMapper.toListSafeMapper()) {
//            service.get().getCashByCodeHistory()
//        }
//    }
//
//    suspend fun deleteInactiveCard(cardId: String): Result<Unit> = retrofitRunner {
//        service.get().deleteInactiveCard(cardId)
//    }
//
//
//    suspend fun activateCard(
//        cardNumber: String,
//        expiryMonth: String,
//        expiryYear: String
//    ): Result<Unit> = retrofitRunner {
//        val body = CardActivationBody(
//            number = cardNumber,
//            expiryDate = expiryMonth + expiryYear
//        )
//        service.get().activateCard(body)
//    }
//
//    suspend fun requireSmsCardActivation(cardId: String): Result<CardActivateKey> {
//        return retrofitRunner.invoke({ CardActivateKey(it.key) }) {
//            val bode = CardIdBody(cardId)
//            service.get().requireSmsCardActivation(bode)
//        }
//    }
//
//    suspend fun requireStatementCardActivation(cardId: String): Result<CardActivateKey> {
//        return retrofitRunner.invoke({ CardActivateKey(it.key) }) {
//            val bode = CardIdBody(cardId)
//            service.get().requireStatementCardActivation(bode)
//        }
//    }
//
//    suspend fun activateCardBySms(key: String, cardId: String): Result<Unit> = retrofitRunner {
//        val body = CardKeyActivationBody(key = key, cardId = cardId)
//        service.get().activateCardBySms(body)
//    }
//
//    suspend fun activateCardByStatement(key: String, cardId: String): Result<Unit> =
//        retrofitRunner {
//            val body = CardKeyActivationBody(key = key, cardId = cardId)
//            service.get().activateCardByStatement(body)
//        }
//
//    suspend fun changeCardDesign(cardId: String, designId: Int): Result<Unit> = retrofitRunner {
//        val body = CardDesignBody(cardId = cardId, designId = designId)
//        service.get().changeCardDesign(body)
//    }
//
//    suspend fun changePhone(newPhone: String, cardId: String): Result<Unit> =
//        retrofitRunner {
//            val body = ChangePhoneBody(newPhone = newPhone, cardId = cardId.toString())
//            service.get().changePhone(body)
//        }
//
//    suspend fun resetPinCounter(cardId: String): Result<Unit> =
//        retrofitRunner {
//            val body = CardPinCounterBody(cardId = cardId)
//            service.get().resetCardPinCounter(body)
//        }
//
//    suspend fun getVirtualCardCvv(cardId: String): Result<Unit> =
//        retrofitRunner {
//            service.get().getVirtualCardCvv(cardId)
//        }
//
//    suspend fun getVirtualCardNumber(cardId: String): Result<String> =
//        retrofitRunner.invoke(virtualCardNumberEncodedResponseMapper) {
//            service.get().getVirtualCardNumber(cardId)
//        }
//
//    suspend fun requestCashByCode(
//        cardId: String,
//        amount: String,
//        currency: String
//    ): Result<CashByCodeResponseBody> =
//        retrofitRunner {
//            val body = CashByCodeRequestBody(
//                cardId = cardId,
//                currency = currency,
//                amount = amount
//            )
//            service.get().createCashByCode(body)
//        }
//
//    suspend fun connectSms(cardId: String): Result<Unit> =
//        retrofitRunner {
//            val body = ConnectSmsBody(cardId = cardId)
//            service.get().connectSms(body)
//        }
//
//    suspend fun block(cardId: String, type: Int): Result<Unit> =
//        retrofitRunner {
//            val body = CardBlockBody(cardId = cardId, type = type)
//            service.get().block(body)
//        }
//
//    suspend fun disconnectSms(cardId: String): Result<Unit> =
//        retrofitRunner {
//            service.get().disconnectSms(cardId = cardId)
//        }
//
//    suspend fun loadLimits(cardId: String, typeText: String): Result<List<CardLimit>> {
//        return retrofitRunner.invoke(cardLimitResponseMapper) {
//            val body = CardGetLimitsBody(cardId = cardId, type = typeText)
//            service.get().getLimits(body)
//        }
//    }
//
//    suspend fun setLimit(
//        cardId: String,
//        code: String,
//        maxNumber: String,
//        maxSingleAmountText: String,
//        maxAmountText: String,
//        currency: String
//    ): Result<CardLimitResult> {
//        return retrofitRunner.invoke(cardLimitResultResponseMapper) {
//            val body = CardSetLimitBody(
//                cardId = cardId,
//                code = code,
//                maxNumber = maxNumber,
//                maxSingleAmount = CardLimitsResponse.MaxAmount(
//                    amount = maxSingleAmountText,
//                    currency = currency
//                ),
//                maxAmount = CardLimitsResponse.MaxAmount(
//                    amount = maxAmountText,
//                    currency = currency
//                )
//            )
//            service.get().setLimit(body)
//        }
//    }
//
//    suspend fun deactivateLimit(cardId: String, code: String): Result<CardLimitResult> {
//        return retrofitRunner.invoke(cardLimitResultResponseMapper) {
//            val body = CardDeactivateLimitBody(cardId, code)
//            service.get().deactivateLimit(body)
//        }
//
//    }
//
//    suspend fun cancelCashByCode(cashcodeId: String): Result<Unit> =
//        retrofitRunner {
//            val body = CancelCashByCodeRequest(
//                cashcodeId = cashcodeId
//            )
//            service.get().cancelCashByCode(body = body)
//        }

}