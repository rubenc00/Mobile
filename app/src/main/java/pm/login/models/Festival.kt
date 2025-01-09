package pm.login.models

data class Festival(
    val id_festival: Int? = null,
    val nome: String? = null,
    val preco: Double = 0.0,
)

data class FestivalResponse(
    val status: String,
    val data: List<Festival>?,
    val message: String?
)