package movie.com.umovie.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import movie.com.umovie.R
import movie.com.umovie.classes.ModelGenre
import movie.com.umovie.classes.PropertyStatusBar
import movie.com.umovie.classes.PropertyToast
import movie.com.umovie.classes.adapter.ListGenre
import movie.com.umovie.databinding.ApplicationMovieBinding
import org.json.JSONException
import org.json.JSONObject

class Genre: AppCompatActivity(),View.OnClickListener {
    private lateinit var binding : ApplicationMovieBinding
    private val list = ArrayList<ModelGenre>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PropertyStatusBar().changeColor(R.color.yellow, this)
        binding.btnBack.setOnClickListener(this)
        setData()
        restApi()
    }
    private fun setData() {
        binding.rvMovie.layoutManager = LinearLayoutManager(this)
        val adapter = ListGenre(list,this)
        binding.rvMovie.adapter = adapter
    }

    private fun restApi() {
        showLoading()
        val url = "http://api.themoviedb.org/3/genre/movie/list?api_key=d41992a61f832a96fde5832501194f78&language=en-US"
        val request = Volley.newRequestQueue(this)
        val stringReq = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                hideLoading()
                try {
                    val jsonObj = JSONObject(response)
                    callbackResponseListener(jsonObj)
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
        stringReq.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(stringReq)
    }

    private fun putParams(params: HashMap<String,String>): HashMap<String,String>{
        return params
    }

    private fun callbackResponseListener(jsonObj: JSONObject) {
        val array = jsonObj.getJSONArray("genres")
        for (i in 0 until array.length()){
            val id = array.getJSONObject(i).getString("id")
            val name = array.getJSONObject(i).getString("name")
            list.add(ModelGenre(name,id))
        }
        val adapter = ListGenre(list,this)
        binding.rvMovie.adapter = adapter
    }

    private fun hideLoading() {
        binding.layoutLoading.root.visibility = View.GONE
        binding.layoutLoading.loading.stopShimmerAnimation()
    }

    private fun showLoading() {
        binding.layoutLoading.root.visibility = View.VISIBLE
        binding.layoutLoading.loading.startShimmerAnimation()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnBack->{
                finish()
            }
        }
    }
}