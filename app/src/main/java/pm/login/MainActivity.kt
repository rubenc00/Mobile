package pm.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pm.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    fun doLogin(view: View) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g19/api/login_cliente.php"


        // POST request + get string response from the provided URL.
        val postRequest = object : StringRequest(Method.POST, url,
            { response ->
                try {
                    // Tenta parse o JSON de resposta
                    val jsonResponse = JSONObject(response)

                    if (jsonResponse.getString("status") == "OK") {
                        // Obtém o nome do usuário
                        val userName = jsonResponse.getString("userName")

                        // Salva o nome do usuário nas SharedPreferences
                        getSharedPreferences("pmLogin", Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean("login", true)
                            .putString("userName", userName)
                            .apply()

                        // Inicia a UserActivity
                        startActivity(Intent(this, UserActivity::class.java))
                        finish()
                    } else {
                        // Caso o status não seja OK
                        Toast.makeText(this, "Login falhou", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Erro ao processar a resposta
                    Toast.makeText(this, "Erro ao processar resposta: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params: MutableMap<String,String> = HashMap()
                params["email"] = binding.txtEmail.text.toString()
                params["password"] = binding.txtPassword.text.toString()
                return params
            }
        }
        // Add the request to the RequestQueue.
        queue.add(postRequest)
    }
}