package entidadesPago

data class CdrResponse(
    val accepted: Boolean,
    val code: String,
    val description: String,
    val id: String,
    val notes: List<String>
)