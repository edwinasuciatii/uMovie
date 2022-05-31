package movie.com.umovie.classes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import movie.com.umovie.R
import movie.com.umovie.activity.DetailMovie
import movie.com.umovie.classes.ModelMovie

class ListMovie(private var data: ArrayList<ModelMovie>, private val activity: FragmentActivity):
        RecyclerView.Adapter<ListMovie.ViewHolder>(){

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val getData: ModelMovie = data[p1]
        p0.title.text = getData.name
        val url = "http://image.tmdb.org/t/p/original"+getData.photo
        Glide.with(activity)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
            .into(p0.image)
        p0.itemView.setOnClickListener{
            val i = Intent(activity,DetailMovie::class.java)
            i.putExtra("Id",getData.id)
            activity.startActivity(i)
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view : View = LayoutInflater.from(p0.context).inflate(R.layout.application_movie_card, p0, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.image)
        var title: TextView = itemView.findViewById(R.id.title)
    }
}