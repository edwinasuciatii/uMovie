package movie.com.umovie.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import movie.com.umovie.R
import movie.com.umovie.classes.EndlessRecyclerViewScrollListener
import movie.com.umovie.classes.ModelMovie
import movie.com.umovie.classes.PropertyStatusBar
import movie.com.umovie.classes.PropertyToast
import movie.com.umovie.classes.adapter.ListMovie
import movie.com.umovie.databinding.ApplicationMovieBinding
import org.json.JSONException
import org.json.JSONObject

class Movie: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ApplicationMovieBinding
    private val list = ArrayList<ModelMovie>()
    var id : String? = null
    private lateinit var adapter: ListMovie
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PropertyStatusBar().changeColor(R.color.yellow, this)
        id = intent?.getStringExtra("Id")
        binding.btnBack.setOnClickListener(this)
        setData()
    }
    private fun setData() {
        var url = "http://api.themoviedb.org/3/discover/movie?api_key=d41992a61f832a96fde5832501194f78&with_genres=$id&page=1"
        restApi(url, false)
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.rvMovie.layoutManager = gridLayoutManager
        adapter = ListMovie(list, this)
        binding.rvMovie.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.i("cek", "page$page")
                val pageReview = page +1
                url = "http://api.themoviedb.org/3/discover/movie?api_key=d41992a61f832a96fde5832501194f78&with_genres=$id&page=$pageReview"
                restApi(url,true)
            }
        }
        binding.rvMovie.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    private fun restApi(url: String, scroll: Boolean) {
        showLoading(scroll)
        val request = Volley.newRequestQueue(this)
        val stringReq = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                hideLoading(scroll)
                try {
                    val jsonObj = JSONObject(response)
                    callbackResponseListener(jsonObj)
                } catch (ex: JSONException) {
                    PropertyToast().toast(this,"Terjadi Kesalahan")
                }
            },
            Response.ErrorListener {
                hideLoading(scroll)
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
        stringReq.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(stringReq)
    }

    private fun putParams(params: HashMap<String,String>): HashMap<String,String>{
        return params
    }

    private fun callbackResponseListener(jsonObj: JSONObject) {
        val array = jsonObj.getJSONArray("results")
        for (i in 0 until array.length()){
            val title = array.getJSONObject(i).getString("original_title")
            val poster = array.getJSONObject(i).getString("poster_path")
            val id = array.getJSONObject(i).getString("id")
            list.add(ModelMovie(title,poster,id))
        }
        adapter.notifyDataSetChanged()
    }

    private fun hideLoading(scroll: Boolean) {
        if (scroll){
            binding.progressBar2.visibility = View.GONE
        }else{
            binding.layoutLoading.root.visibility = View.GONE
            binding.layoutLoading.loading.stopShimmerAnimation()
        }
    }

    private fun showLoading(scroll: Boolean) {
        if (scroll){
            binding.progressBar2.visibility = View.VISIBLE
        }else{
            binding.layoutLoading.root.visibility = View.VISIBLE
            binding.layoutLoading.loading.startShimmerAnimation()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnBack->{
                finish()
            }
        }
    }
}