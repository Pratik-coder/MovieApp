package com.example.devrevassignment.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.devrevassignment.R
import com.example.devrevassignment.databinding.RowMoviesListBinding
import com.example.devrevassignment.extensions.dp
import com.example.devrevassignment.extensions.load
import com.example.devrevassignment.models.MovieData
import com.example.devrevassignment.models.Movies
import com.example.devrevassignment.retrofit.RetrofitClient
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MovieListAdapter(val context: Context,var movieList:List<MovieData> = ArrayList()):RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    interface OnItemClickListener
    {
        fun onItemClick(movieData: MovieData,container: View)
    }
    private var onItemTVImageClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return ViewHolder(RowMoviesListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val movieData=movieList[position]
      holder.textViewSeriesTitle.text=movieData.title
        holder.textViewSeriesDescription.text=context.getString(
            R.string.strMovieRowDescPattern,
            if (movieData.relaeseDate.isNotEmpty())
                LocalDate.parse(movieData.relaeseDate).format(
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM))
            else
                context.getString(R.string.strNoReleaseDate),getRating(movieData))
        holder.imageViewPoster.setImageResource(R.drawable.defaultimage)
        holder.imageViewPoster.transitionName=movieData.id.toString()
        holder.linearLayout.setBackgroundColor(Color.DKGRAY)

        holder.imageViewPoster.load(url= RetrofitClient.POSTER_URL + movieData.posterPath,
            crossFade = true, width = 160.dp, height = 160.dp){
                color ->
            holder.linearLayout.setBackgroundColor(color)
        }
        holder.cardView.setOnClickListener {
            onItemTVImageClickListener?.onItemClick(movieData,holder.imageViewPoster)
        }
    }

    override fun getItemCount(): Int {
       return movieList.size
    }

    fun getRating(movies: MovieData):String{
        return if (movies.voteCount==0 && context!=null){
            context.getString(R.string.strNoRating)
        }
        else{
            val starIcon=9733.toChar()
            "${movies.voteAverage} $starIcon"
        }
    }

    fun clear(){
        this.movieList= emptyList()
    }

    fun fillList(movies:List<MovieData>){
        this.movieList+=movies
        notifyDataSetChanged()
    }

    fun setOnMovieClickListener(listener: OnItemClickListener){
        this.onItemTVImageClickListener=listener
    }

    class ViewHolder(binding: RowMoviesListBinding):RecyclerView.ViewHolder(binding.root) {
        val cardView: CardView =binding.cardViewMovie
        val linearLayout: LinearLayout =binding.linearLayoutContainer
        val textViewSeriesTitle: TextView =binding.textViewMovieTitle
        val textViewSeriesDescription: TextView =binding.textViewMovieDescription
        val imageViewPoster: ImageView =binding.imageViewPoster
    }
}