package movie.com.umovie.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import movie.com.umovie.R
import movie.com.umovie.classes.PropertyStatusBar
import movie.com.umovie.databinding.ApplicationSplashBinding

class Splash:AppCompatActivity() {
    private lateinit var binding : ApplicationSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PropertyStatusBar().changeColor(R.color.yellow, this)
        val background = object : Thread() {
            override fun run() {
                try {
                    sleep((800).toLong())
                        setContent()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
    private fun setContent(){
        startActivity(Intent(this, Genre::class.java))
        finish()
    }
}