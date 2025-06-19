package entidadesPago

data class CdrResponseX(
    val accepted: Boolean,
    val code: String,
    val description: String,
    val id: String,
    val notes: List<String>
)