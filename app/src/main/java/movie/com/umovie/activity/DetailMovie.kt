package movie.com.umovie.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailLoader.OnThumbnailLoadedListener
import com.google.android.youtube.player.YouTubeThumbnailView
import movie.com.umovie.R
import movie.com.umovie.classes.*
import movie.com.umovie.classes.adapter.ListReview
import movie.com.umovie.databinding.ApplicationMovieDetailBinding
import org.json.JSONException
import org.json.JSONObject


class DetailMovie: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ApplicationMovieDetailBinding
    private val list = ArrayList<ModelReview>()
    var id : String? = null
    private var urlDetail : String? = null
    private var urlVideo : String? = null
    var urlReview : String? = null
    private var apikey : String = "AIzaSyCTwpdE50s0iohABCblgbC045mB_2dPGYs"
    private var key : String? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private lateinit var adapter: ListReview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PropertyStatusBar().changeColor(R.color.yellow, this)
        id = intent?.getStringExtra("Id")
        binding.btnBack.setOnClickListener(this)
        binding.youTubePlayerView.setOnClickListener(this)
        setData()
    }

    private fun setData() {
        urlDetail = "https://api.themoviedb.org/3/movie/$id?api_key=d41992a61f832a96fde5832501194f78"
        urlVideo = "https://api.themoviedb.org/3/movie/$id/videos?api_key=d41992a61f832a96fde5832501194f78&language=en-US"
        urlReview = "https://api.themoviedb.org/3/movie/$id/reviews?api_key=d41992a61f832a96fde5832501194f78&language=en-US&page=1"
        restApi(urlDetail,false)
        restApi(urlVideo,false)
        restApi(urlReview,false)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = linearLayoutManager
        adapter = ListReview(list, this)
        binding.rvReview.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                val pageReview = page +1
                urlReview = "https://api.themoviedb.org/3/movie/$id/reviews?api_key=d41992a61f832a96fde5832501194f78&language=en-US&page=$pageReview"
                restApi(urlReview,true)
            }
        }
        binding.rvReview.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }


    private fun restApi(url: String?, scroll: Boolean) {
        showLoading(scroll)
        val request = Volley.newRequestQueue(this)
        val stringReq = object : StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    hideLoading()
                    try {
                        val jsonObj = JSONObject(response)
                        callbackResponseListener(jsonObj, url)
                    } catch (ex: JSONException) {
                        PropertyToast().toast(this,"Terjadi Kesalahan")
                    }
                },
                Response.ErrorListener {
                    hideLoading()
                    PropertyToast().toast(this,"Terjadi Kesalahan")
                }
        ){
            override fun getParams(): Map<String, String> {
                //Creating HashMap
                val params = HashMap<String, String>()
                putParams(params)
                return params
            }
        }
        stringReq.retryPolicy = DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        request.add(stringReq)
    }

    private fun putParams(params: HashMap<String, String>): HashMap<String, String>{
        return params
    }

    private fun callbackResponseListener(jsonObj: JSONObject, url: String?) {
       if (url == urlDetail){
           val posterPath = jsonObj.getString("poster_path")
           val title = jsonObj.getString("original_title")
           val releaseDate = jsonObj.getString("release_date")
           val tagline = jsonObj.getString("tagline")
           val voteAverage = jsonObj.getString("vote_average")
           binding.title.text = title
           binding.tagline.text = tagline
           binding.releaseDate.text = releaseDate
           binding.ratings.text = voteAverage
           val urlImage = "http://image.tmdb.org/t/p/original$posterPath"
           Glide.with(this)
               .load(urlImage)
               .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
               .into(binding.moviePoster)
       }
        if (url == urlVideo) {
            val results = jsonObj.getJSONArray("results")
            for (i in 0 until results.length()) {
                key = results.getJSONObject(i).getString("key")
            }
            binding.youTubePlayerView.initialize(
                    apikey,
                    object : YouTubeThumbnailView.OnInitializedListener {
                        override fun onInitializationSuccess(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                youTubeThumbnailLoader: YouTubeThumbnailLoader
                        ) {
                            youTubeThumbnailLoader.setVideo(key)
                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                                    OnThumbnailLoadedListener {
                                override fun onThumbnailLoaded(
                                        youTubeThumbnailView: YouTubeThumbnailView,
                                        s: String
                                ) {
                                    //need to release the loader!!!
                                    youTubeThumbnailLoader.release()
                                }

                                override fun onThumbnailError(
                                        youTubeThumbnailView: YouTubeThumbnailView,
                                        errorReason: YouTubeThumbnailLoader.ErrorReason
                                ) {
                                    //need to release the loader!!!
                                    youTubeThumbnailLoader.release()
                                }
                            })
                        }

                        override fun onInitializationFailure(
                                youTubeThumbnailView: YouTubeThumbnailView,
                                youTubeInitializationResult: YouTubeInitializationResult
                        ) {
                            //handle error here
                        }
                    })
        }
        if (url == urlReview){
            val results = jsonObj.getJSONArray("results")
//            list.clear()
            for (i in 0 until results.length()) {
                val author = results.getJSONObject(i).getJSONObject("author_details")
                val username = author.getString("username")
                val avatarPath = author.getString("avatar_path")
                val content = results.getJSONObject(i).getString("content")
                list.add(ModelReview(username, content, avatarPath))
            }
            adapter.notifyDataSetChanged()
//            scrollListener?.resetState()
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading(scroll: Boolean) {
        if (scroll){
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnBack->{
                finish()
            }
            R.id.youTubePlayerView -> {
                val i = Intent(this, YoutubePlayerActivity::class.java)
                i.putExtra("Key", key)
                startActivity(i)
            }
        }
    }
}