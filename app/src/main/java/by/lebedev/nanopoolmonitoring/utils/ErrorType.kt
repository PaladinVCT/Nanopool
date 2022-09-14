package by.lebedev.nanopoolmonitoring.utils

enum class ErrorType {
    IO,
    Network,
    NetworkConnection,
    Timeout,
    JsonParsing,
    Unclassified,
    Unauthorized
}