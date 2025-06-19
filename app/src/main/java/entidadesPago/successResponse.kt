package entidadesPago

data class successResponse(
    val hash: String,
    val sunatResponse: SunatResponse,
    val xml: String
)