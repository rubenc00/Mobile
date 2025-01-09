package pm.login


import ConcertoAdapter
import android.annotation.SuppressLint
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
import pm.login.R.id.concertoRecycler
import pm.login.models.Concerto
import java.io.IOException

class ConcertoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConcertoAdapter
    private lateinit var progressBar: ProgressBar

    private val client = OkHttpClient()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concerto)

        recyclerView = findViewById(concertoRecycler)

        // Configurar o RecyclerView
        adapter = ConcertoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carregar os concertos da API
        carregarConcertosDaAPI()
    }

    private fun carregarConcertosDaAPI() {
        progressBar.visibility = View.VISIBLE

        val request = Request.Builder()
            .url("https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g19/api/api_concertos.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@ConcertoActivity, "Erro ao conectar à API", Toast.LENGTH_LONG).show()
                    Log.e("ConcertoActivity", "Erro: ${e.message}")
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
                            Toast.makeText(this@ConcertoActivity, "Erro ao processar os dados da API", Toast.LENGTH_LONG).show()
                            Log.e("LivrosActivity", "Erro ao processar resposta: ${e.message}")
                        }
                    } else {
                        Toast.makeText(this@ConcertoActivity, "Erro na resposta da API", Toast.LENGTH_LONG).show()
                        Log.e("LivrosActivity", "Resposta inválida: $responseBody")
                    }
                }
            }
        })
    }

    private fun processarRespostaAPI(responseBody: String) {
        val jsonObject = JSONObject(responseBody)

        if (jsonObject.getString("status") == "success") {
            val ConcertoArray = jsonObject.getJSONArray("data")
            val concerto = mutableListOf<Concerto>() // Usando MutableList agora, para poder usar o método add()

            for (i in 0 until ConcertoArray.length()) {
                val concertoJson = ConcertoArray.getJSONObject(i)
                val Festival = Concerto(
                    id_concerto = concertoJson.optInt("id_festival"),
                    nome = concertoJson.optString("nome"),
                    preco = concertoJson.optDouble("preco"),
                )
                concerto.add(Festival) // Agora o método add() é válido, pois estamos usando MutableList
            }

            // Atualizar o RecyclerView com a lista de livros
            adapter.submitList(concerto)
        } else {
            Toast.makeText(this@ConcertoActivity, "Erro ao carregar festivais", Toast.LENGTH_LONG).show()
        }
    }
}





