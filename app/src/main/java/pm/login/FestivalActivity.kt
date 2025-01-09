package pm.login


import FestivalAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import pm.login.models.Festival
import java.io.IOException

class FestivalActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FestivalAdapter
    private lateinit var progressBar: ProgressBar

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_festival)

        recyclerView = findViewById(R.id.FestivalRecycler)

        // Configurar o RecyclerView
        adapter = FestivalAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carregar os livros da API
        carregarFestivaisDaAPI()
    }

    private fun carregarFestivaisDaAPI() {
        progressBar.visibility = View.VISIBLE

        val request = Request.Builder()
            .url("https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g19/api/api_festival.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@FestivalActivity, "Erro ao conectar à API", Toast.LENGTH_LONG).show()
                    Log.e("LivrosActivity", "Erro: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                runOnUiThread {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                        try {
                            processarRespostaAPI(responseBody)
                        } catch (e: Exception) {
                            Toast.makeText(this@FestivalActivity, "Erro ao processar os dados da API", Toast.LENGTH_LONG).show()
                            Log.e("LivrosActivity", "Erro ao processar resposta: ${e.message}")
                        }
                    } else {
                        Toast.makeText(this@FestivalActivity, "Erro na resposta da API", Toast.LENGTH_LONG).show()
                        Log.e("LivrosActivity", "Resposta inválida: $responseBody")
                    }
                }
            }
        })
    }

    private fun processarRespostaAPI(responseBody: String) {
        val jsonObject = JSONObject(responseBody)

        if (jsonObject.getString("status") == "success") {
            val FestivalArray = jsonObject.getJSONArray("data")
            val festival = mutableListOf<Festival>() // Usando MutableList agora, para poder usar o método add()

            for (i in 0 until FestivalArray.length()) {
                val festivalJson = FestivalArray.getJSONObject(i)
                val Festival = Festival(
                    id_festival = festivalJson.optInt("id_festival"),
                    nome = festivalJson.optString("nome"),
                    preco = festivalJson.optDouble("preco"),
                )
                festival.add(Festival) // Agora o método add() é válido, pois estamos usando MutableList
            }

            // Atualizar o RecyclerView com a lista de livros
            adapter.submitList(festival) // Isso funciona porque 'livros' é MutableList<Livro>, mas será passado como List<Livro>
        } else {
            Toast.makeText(this@FestivalActivity, "Erro ao carregar festivais", Toast.LENGTH_LONG).show()
        }
    }
}





