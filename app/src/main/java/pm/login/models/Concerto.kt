package pm.login.models

data class Concerto(
    val id_concerto: Int? = null,
    val nome: String? = null,
    val preco: Double = 0.0,
)

data class ConcertoResponse(
    val status: String,
    val data: List<Festival>?,
    val message: String?
)