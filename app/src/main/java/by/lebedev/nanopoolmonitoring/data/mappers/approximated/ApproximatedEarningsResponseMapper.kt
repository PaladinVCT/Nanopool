package by.lebedev.nanopoolmonitoring.data.mappers.approximated


import by.lebedev.nanopoolmonitoring.data.db.mappers.Mapper
import by.lebedev.nanopoolmonitoring.data.dto.approximated.ApproximatedEarningsResponse
import by.lebedev.nanopoolmonitoring.data.entities.approximated.ApproximatedEarnings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApproximatedEarningsResponseMapper @Inject constructor() :
    Mapper<ApproximatedEarningsResponse, ApproximatedEarnings> {
    override fun map(from: ApproximatedEarningsResponse): ApproximatedEarnings {
        return ApproximatedEarnings(
            status = from.status ?: false,
            earnings = ApproximatedEarnings.Earnings(
                minute = ApproximatedEarnings.Earnings.Minute(
                    coins = from.data?.minute?.coins ?: 0.0,
                    dollars = from.data?.minute?.dollars ?: 0.0,
                    euros = from.data?.minute?.euros ?: 0.0,
                    pounds = from.data?.minute?.pounds ?: 0.0,
                    rubles = from.data?.minute?.rubles ?: 0.0,
                    yuan = from.data?.minute?.yuan ?: 0.0,
                    bitcoins = from.data?.minute?.bitcoins ?: 0.0
                ),
                hour = ApproximatedEarnings.Earnings.Hour(
                    coins = from.data?.hour?.coins ?: 0.0,
                    dollars = from.data?.hour?.dollars ?: 0.0,
                    euros = from.data?.hour?.euros ?: 0.0,
                    pounds = from.data?.hour?.pounds ?: 0.0,
                    rubles = from.data?.hour?.rubles ?: 0.0,
                    yuan = from.data?.hour?.yuan ?: 0.0,
                    bitcoins = from.data?.hour?.bitcoins ?: 0.0
                ),
                day = ApproximatedEarnings.Earnings.Day(
                    coins = from.data?.day?.coins ?: 0.0,
                    dollars = from.data?.day?.dollars ?: 0.0,
                    euros = from.data?.day?.euros ?: 0.0,
                    pounds = from.data?.day?.pounds ?: 0.0,
                    rubles = from.data?.day?.rubles ?: 0.0,
                    yuan = from.data?.day?.yuan ?: 0.0,
                    bitcoins = from.data?.day?.bitcoins ?: 0.0
                ),
                week = ApproximatedEarnings.Earnings.Week(
                    coins = from.data?.week?.coins ?: 0.0,
                    dollars = from.data?.week?.dollars ?: 0.0,
                    euros = from.data?.week?.euros ?: 0.0,
                    pounds = from.data?.week?.pounds ?: 0.0,
                    rubles = from.data?.week?.rubles ?: 0.0,
                    yuan = from.data?.week?.yuan ?: 0.0,
                    bitcoins = from.data?.week?.bitcoins ?: 0.0
                ),
                month = ApproximatedEarnings.Earnings.Month(
                    coins = from.data?.month?.coins ?: 0.0,
                    dollars = from.data?.month?.dollars ?: 0.0,
                    euros = from.data?.month?.euros ?: 0.0,
                    pounds = from.data?.month?.pounds ?: 0.0,
                    rubles = from.data?.month?.rubles ?: 0.0,
                    yuan = from.data?.month?.yuan ?: 0.0,
                    bitcoins = from.data?.month?.bitcoins ?: 0.0
                ),
                prices = ApproximatedEarnings.Earnings.Prices(
                    priceUsd = from.data?.prices?.priceUsd ?: 0.0,
                    priceBtc = from.data?.prices?.priceBtc ?: 0.0,
                    priceCny = from.data?.prices?.priceCny ?: 0.0,
                    priceEur = from.data?.prices?.priceEur ?: 0.0,
                    priceGbp = from.data?.prices?.priceGbp ?: 0.0,
                    priceRur = from.data?.prices?.priceRur ?: 0.0
                )
            )
        )
    }
}