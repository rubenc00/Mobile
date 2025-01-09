package pm.login.models

data class Banda(
    val id_banda: Int? = null,
    val nome: String? = null,
    val preco: Double = 0.0,
)

data class BandaResponse(
    val status: String,
    val data: List<Festival>?,
    val message: String?
)