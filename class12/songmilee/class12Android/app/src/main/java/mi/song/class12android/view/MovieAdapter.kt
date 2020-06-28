package mi.song.class12android.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mi.song.class12android.R
import mi.song.class12android.model.data.MovieInfo

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieVH>() {
    private val movieList = ArrayList<MovieInfo>()

    fun addMovieInfo(movieInfo:MovieInfo){
        movieList.add(movieInfo)
        notifyItemInserted(movieList.size - 1)
    }

    fun resetMovieList(){
        movieList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        return MovieVH(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(movieList[position])
    }

    inner class MovieVH(itemView:View) : RecyclerView.ViewHolder(itemView){
        private val imgThumbnail:ImageView = itemView.findViewById(R.id.iv_thumbnail)
        private val  tvTitle:TextView = itemView.findViewById(R.id.tv_title)
        private val  tvDirector:TextView = itemView.findViewById(R.id.tv_director)
        private val  tvActor:TextView = itemView.findViewById(R.id.tv_actor)

        fun bind(movieInfo: MovieInfo){
            tvTitle.text = movieInfo.title
            tvDirector.text = movieInfo.director
            tvActor.text = movieInfo.actor

            movieInfo.image?.let { Glide.with(itemView.context).load(movieInfo.image).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imgThumbnail) }
        }
    }
}