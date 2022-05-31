package movie.com.umovie.classes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import movie.com.umovie.R
import movie.com.umovie.activity.Movie
import movie.com.umovie.classes.ModelGenre


class ListGenre(private var data: ArrayList<ModelGenre>, private val activity: FragmentActivity):
    RecyclerView.Adapter<ListGenre.ViewHolder>(){

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val getData: ModelGenre = data[p1]
        p0.name.text = getData.name
        p0.itemView.setOnClickListener{
            val i = Intent(activity,Movie::class.java)
            i.putExtra("Id",getData.id)
            activity.startActivity(i)
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view : View = LayoutInflater.from(p0.context).inflate(R.layout.application_genre_card, p0, false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById(R.id.name)
    }
}