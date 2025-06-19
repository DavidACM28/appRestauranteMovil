package entidadesPago

data class SunatResponse(
    val cdrResponse: CdrResponse,
    val cdrZip: String,
    val error: Error,
    val success: Boolean
)