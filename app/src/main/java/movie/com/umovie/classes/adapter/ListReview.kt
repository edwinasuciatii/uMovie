package movie.com.umovie.classes.adapter

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
import movie.com.umovie.classes.ModelReview

class ListReview(private var data: ArrayList<ModelReview>, private val activity: FragmentActivity):
    RecyclerView.Adapter<ListReview.ViewHolder>(){

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val getData: ModelReview = data[p1]
        p0.username.text = getData.name
        p0.content.text = getData.content
        val url = getData.photo
        Glide.with(activity)
            .load(url.drop(1))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(60)))
            .into(p0.avatar)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view : View = LayoutInflater.from(p0.context).inflate(R.layout.application_review_card, p0, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var avatar: ImageView = itemView.findViewById(R.id.avatar)
        var username: TextView = itemView.findViewById(R.id.username)
        var content: TextView = itemView.findViewById(R.id.content)
    }
}