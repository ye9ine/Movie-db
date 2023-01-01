package com.yeyintwin.kbz_movie_db.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yeyintwin.kbz_movie_db.MovieDetailActivity
import com.yeyintwin.kbz_movie_db.R
import com.yeyintwin.kbz_movie_db.databinding.MovieItemBinding
import com.yeyintwin.kbz_movie_db.helper.MovieDbConstant
import com.yeyintwin.kbz_movie_db.model.MovieModel


class MovieListAdapter(private val type: String) : PagingDataAdapter<MovieModel, MovieListAdapter.ViewHolder>(DataDifferntiator) {

    inner class ViewHolder(private val movieItemBinding: MovieItemBinding) : RecyclerView.ViewHolder(movieItemBinding.root){
        fun bind(movieModel: MovieModel){
            val context = movieItemBinding.root.context
            movieItemBinding.apply {
                tvTitle.text = if (type == MovieDbConstant().movie) movieModel.title else movieModel.original_name
                tvReleaseDate.text = if (type == MovieDbConstant().movie) movieModel.release_date else movieModel.first_air_date
                tvOverview.text = movieModel.overview
                tvRating.text = movieModel.vote_average.toString()

                Glide.with(context)
                    .load(MovieDbConstant().basePosterPath + movieModel.poster_path)
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .transform(CenterInside(),RoundedCorners(24))
                    .into(imgPoster)

                linearItem.setOnClickListener{
                    movieModel.type = type
                    val intent = Intent(it.context, MovieDetailActivity::class.java).apply {
                        putExtra("obj", movieModel)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movieModel: MovieModel = getItem(position)!!
        holder.bind(movieModel)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val movieItemBinding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(movieItemBinding)
    }

    object DataDifferntiator : DiffUtil.ItemCallback<MovieModel>() {

        override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
            return oldItem == newItem
        }
    }

}
