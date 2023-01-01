package com.yeyintwin.kbz_movie_db.adapter
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yeyintwin.kbz_movie_db.PhotoDetailViewActivity
import com.yeyintwin.kbz_movie_db.R
import com.yeyintwin.kbz_movie_db.databinding.PosterItemBinding
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.model.MoviePosterModel

class MoviePosterAdapter(var list: ArrayList<MoviePosterModel>,) : RecyclerView.Adapter<MoviePosterAdapter.ViewHolder>() {

    inner class ViewHolder(private val posterItemBinding: PosterItemBinding) : RecyclerView.ViewHolder(posterItemBinding.root){
        fun bind(moviePosterModel: MoviePosterModel){
            val context = posterItemBinding.root.context

            posterItemBinding.apply {

                Glide.with(context)
                    .load(MovieDbConstant().basePosterPath + moviePosterModel.file_path)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .transform(CenterInside(), RoundedCorners(24))
                    .into(imgPoster)

                imgPoster.setOnClickListener {
                    val intent = Intent(it.context, PhotoDetailViewActivity::class.java).apply {
                        putExtra("poster", moviePosterModel.file_path)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val posterItemBinding = PosterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(posterItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val moviePosterModel: MoviePosterModel = this.list[position]
        holder.bind(moviePosterModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(list: ArrayList<MoviePosterModel>){
        this.list = list
        notifyDataSetChanged()
    }

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

}