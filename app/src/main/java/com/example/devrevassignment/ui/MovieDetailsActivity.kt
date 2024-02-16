package com.example.devrevassignment.ui

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devrevassignment.R
import com.example.devrevassignment.adapter.CastListAdapter
import com.example.devrevassignment.base.BaseActivity
import com.example.devrevassignment.databinding.ActivityMovieDetailsBinding
import com.example.devrevassignment.extensions.dp
import com.example.devrevassignment.extensions.gone
import com.example.devrevassignment.extensions.load
import com.example.devrevassignment.extensions.visible
import com.example.devrevassignment.models.CastData
import com.example.devrevassignment.models.MovieDetail
import com.example.devrevassignment.models.MovieGenres
import com.example.devrevassignment.retrofit.RetrofitClient
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.utils.ColorUtils.darken
import com.example.devrevassignment.utils.TimeUtils
import com.example.devrevassignment.utils.orNa
import com.example.devrevassignment.viewmodel.MovieDetailViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.text.DecimalFormat
import java.util.*

class MovieDetailsActivity : BaseActivity() {

    private val movieDetailsViewModel: MovieDetailViewModel by viewModel()
    private lateinit var binding: ActivityMovieDetailsBinding
    private val args:MovieDetailsActivityArgs by navArgs()
    private lateinit var castListAdapter: CastListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        clearStatusBar()
        postponeEnterTransition()

        movieDetailsViewModel.getMovieDetailsById(args.id.toString())
        movieDetailsViewModel.getCastList(args.id.toString())

        lifecycleScope.launch {
            movieDetailsViewModel.movieDetails.collect {
                handleSingleMovieDataState(it)
            }
        }

        lifecycleScope.launch {
            movieDetailsViewModel.castList.collect {
                handleCastData(it)
            }
        }
    }

    private fun handleSingleMovieDataState(state: Resource<MovieDetail>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadMovieDetailData(state.data)
            }
            Resource.Status.ERROR -> {
                binding.progressBar.gone()
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                finish()
            }
            else->{}
        }
    }

    private fun handleCastData(state:Resource<List<CastData>>){
        when(state.status){
            Resource.Status.LOADING->{
                binding.progressBar.visible()
            }

            Resource.Status.SUCCESS->{
                binding.progressBar.gone()
                handleCastList(state.data)
            }

            Resource.Status.ERROR->{
                binding.progressBar.gone()
                Toast.makeText(this,"Error:${state.message}",Toast.LENGTH_SHORT).show()
            }

            Resource.Status.EMPTY->{
                Log.d("TAG","Empty data state")
            }
        }
    }

    private fun loadMovieDetailData(movieDetails: MovieDetail?){
        movieDetails?.let {
            binding.imageViewMovieDetails.transitionName = args.id.toString()
            binding.imageViewMovieDetails.load(url = RetrofitClient.BACKDROP_URL + movieDetails.backdropPath, width = 160.dp, height = 160.dp) { color ->
                window?.statusBarColor = color.darken
                binding.collapsingToolbarMovieDetails.setBackgroundColor(color)
                binding.collapsingToolbarMovieDetails.setContentScrimColor(color)
                startPostponedEnterTransition()
            }
            binding.collapsingToolbarMovieDetails.title = movieDetails.title
            binding.textViewMovieDescription.text = movieDetails.overview
            binding.textViewCompanyName.text = movieDetails.productionCompanies.firstOrNull()?.name.orNa()

            binding.textViewRuntime.text = if (movieDetails.runtime > 0)
                TimeUtils.formatMinutes(this, movieDetails.runtime) else getString(R.string.strNoData)

            binding.textViewYear.text = if (movieDetails.releaseDate.isNotEmpty())
                LocalDate.parse(movieDetails.releaseDate).format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault())) else getString(R.string.strNoReleaseDate)

            binding.textViewWebsite.text = HtmlCompat.fromHtml(
                getString(
                    R.string.strVisitWebsiteUrl,
                    movieDetails.homepage,
                    getString(R.string.strVisitWebsite)
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.textViewWebsite.movementMethod = LinkMovementMethod.getInstance()
            fillGenres(movieDetails.genres)
            binding.movieDetailExtraInfo.textViewDetailRating.text = if (movieDetails.voteAverage > 0) movieDetails.voteAverage.toString() else getString(R.string.strNoRating)
            binding.movieDetailExtraInfo.textViewDetailVotes.text = if (movieDetails.voteCount > 0) movieDetails.voteCount.toString() else getString(R.string.strNoData)
            binding.movieDetailExtraInfo.textViewDetailRevenue.text = getString(R.string.strRevenuePattern, DecimalFormat("##.##").format(movieDetails.revenue / 1000000.0))
        }
    }

    private fun fillGenres(genres:List<MovieGenres>){
        for (g in genres){
            val chip= Chip(this)
            chip.text=g.name
            binding.chipGroupGenres.addView(chip)
        }
    }

    private fun handleCastList(castList:List<CastData>?){
        if(castList.isNullOrEmpty()){
            binding.textViewCast.gone()
        }
        else{
            binding.textViewCast.visible()
            binding.recyclerViewCast.layoutManager= LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
            castListAdapter= CastListAdapter(this,castList)
            binding.recyclerViewCast.adapter=castListAdapter

        }
    }



    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }



    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }
}