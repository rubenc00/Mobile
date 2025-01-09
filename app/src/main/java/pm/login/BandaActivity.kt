package pm.login


import BandaAdapter
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
import pm.login.R.id.bandaRecycler
import pm.login.models.Banda
import java.io.IOException

class BandaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BandaAdapter
    private lateinit var progressBar: ProgressBar

    private val client = OkHttpClient()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banda)

        recyclerView = findViewById(bandaRecycler)

        // Configurar o RecyclerView
        adapter = BandaAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carregar as bandas da API
        carregarBandasDaAPI()
    }

    private fun carregarBandasDaAPI() {
        progressBar.visibility = View.VISIBLE

        val request = Request.Builder()
            .url("https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g19/api/api_bandas.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@BandaActivity, "Erro ao conectar à API", Toast.LENGTH_LONG).show()
                    Log.e("BandaActivity", "Erro: ${e.message}")
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
                            Toast.makeText(this@BandaActivity, "Erro ao processar os dados da API", Toast.LENGTH_LONG).show()
                            Log.e("BandaActivity", "Erro ao processar resposta: ${e.message}")
                        }
                    } else {
                        Toast.makeText(this@BandaActivity, "Erro na resposta da API", Toast.LENGTH_LONG).show()
                        Log.e("BandaActivity", "Resposta inválida: $responseBody")
                    }
                }
            }
        })
    }

    private fun processarRespostaAPI(responseBody: String) {
        val jsonObject = JSONObject(responseBody)

        if (jsonObject.getString("status") == "success") {
            val BandaArray = jsonObject.getJSONArray("data")
            val concerto = mutableListOf<Banda>() // Usando MutableList agora, para poder usar o método add()

            for (i in 0 until BandaArray.length()) {
                val concertoJson = BandaArray.getJSONObject(i)
                val Festival = Banda(
                    id_banda = concertoJson.optInt("id_banda"),
                    nome = concertoJson.optString("nome"),
                    preco = concertoJson.optDouble("preco"),
                )
                concerto.add(Festival) // Agora o método add() é válido, pois estamos usando MutableList
            }

            // Atualizar o RecyclerView com a lista de livros
            adapter.submitList(concerto)
        } else {
            Toast.makeText(this@BandaActivity, "Erro ao carregar festivais", Toast.LENGTH_LONG).show()
        }
    }
}





