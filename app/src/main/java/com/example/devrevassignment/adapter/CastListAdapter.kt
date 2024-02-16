package com.example.devrevassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devrevassignment.R
import com.example.devrevassignment.databinding.RowMovieCastlistBinding
import com.example.devrevassignment.databinding.RowMoviesListBinding
import com.example.devrevassignment.models.CastData
import com.example.devrevassignment.retrofit.RetrofitClient

class CastListAdapter(val context: Context,var castList:List<CastData> = ArrayList()):RecyclerView.Adapter<CastListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CastListAdapter.ViewHolder(
            RowMovieCastlistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val castData= castList[position]
        holder.textViewCastName.text=castData.originalName
        val creditImageUrl= RetrofitClient.PROFILE_URL + castData.profilePath
        Glide.with(context).load(creditImageUrl).error(R.drawable.defaultimage).into(holder.imageViewMovieCast)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    class ViewHolder(binding: RowMovieCastlistBinding):RecyclerView.ViewHolder(binding.root) {

        val imageViewMovieCast: ImageView =binding.imageViewMovieCast
        val textViewCastName: TextView =binding.textViewCastName
        val linearLayoutCredits: LinearLayout =binding.linearLayoutCredits
    }
}