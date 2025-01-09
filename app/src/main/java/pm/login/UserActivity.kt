package pm.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.google.android.material.bottomnavigation.BottomNavigationView
import pm.login.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserBinding.inflate(layoutInflater)
    }
    companion object {
        lateinit var navigation : BottomNavigationView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navigation = binding.bottomNavigation

        binding.bottomNavigation.setOnItemSelectedListener { item->
            binding.placeholder.removeAllViews()
            when(item.itemId) {
                R.id.navigation_listFestivais ->{
                }
                R.id.navigation_listConcertos ->{
                    try {
                        layoutInflater.inflate(R.layout.activity_concerto,binding.placeholder,true)
                    } catch (e: Exception){
                        e.printStackTrace()
                    }

                }
                R.id.navigation_listBandas ->{
                    try {
                        layoutInflater.inflate(R.layout.activity_banda,binding.placeholder,true)
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
                R.id.navigation_Utilizador ->{
                    layoutInflater.inflate(R.layout.activity_user,binding.placeholder,true)
                }


            }


            return@setOnItemSelectedListener true
        }


    }

    private fun performLogout() {
        getSharedPreferences("pmLogin", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        // Redireciona para a tela de login
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    override fun onStart() {
        super.onStart()
        if(binding.placeholder.isEmpty()) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_listFestivais
        }
    }


}