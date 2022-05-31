package movie.com.umovie.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import movie.com.umovie.R
import movie.com.umovie.classes.PropertyStatusBar
import movie.com.umovie.databinding.ApplicationYoutubePreviewBinding


class YoutubePlayerActivity:AppCompatActivity(), YouTubePlayer.OnInitializedListener{
    private lateinit var binding : ApplicationYoutubePreviewBinding
    private var apikey : String = "AIzaSyCTwpdE50s0iohABCblgbC045mB_2dPGYs"
    private val recoveryDialogRequest = 1
    private var key: String? = null
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationYoutubePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PropertyStatusBar().changeColor(R.color.yellow, this)
        key = intent?.getStringExtra("Key")
        val youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(
            apikey,
            this
        )
    }
    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (!p2) {
            p1?.cueVideo(key)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        if (p1 != null) {
            if (p1.isUserRecoverableError) {
                p1.getErrorDialog(this, recoveryDialogRequest).show()
            } else {
                val errorMessage = java.lang.String.format(
                    "There was an error initializing the YouTubePlayer (%1\$s)",
                    p1.toString()
                )
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

}