package com.example.devrevassignment.fagments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.devrevassignment.R
import com.example.devrevassignment.adapter.MovieListAdapter
import com.example.devrevassignment.databinding.FragmentMovielistBinding
import com.example.devrevassignment.extensions.gone
import com.example.devrevassignment.extensions.setAnchorId
import com.example.devrevassignment.extensions.visible
import com.example.devrevassignment.models.MovieData
import com.example.devrevassignment.models.Movies
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.viewmodel.PopularMovieViewModel
import com.example.devrevassignment.widgets.PaginationScrollListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularMovieFragment() : Fragment(R.layout.fragment_movielist),MovieListAdapter.OnItemClickListener {

    private val popularMovieViewModel: PopularMovieViewModel by sharedViewModel()
    private val movieListAdapter:MovieListAdapter by inject()
    private var popularMovieBinding:FragmentMovielistBinding?=null
    private val binding get() = popularMovieBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        popularMovieBinding= FragmentMovielistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        setUpSwipeRefresh()
        popularMovieViewModel.refreshPopularMovies()
        viewLifecycleOwner.lifecycleScope.launch {
            popularMovieViewModel.popularMovies.collect{
                handlePopularMovieResponse(it)
            }
        }
    }



    private fun handlePopularMovieResponse(state: Resource<List<MovieData>>){
        when(state.status){
            Resource.Status.LOADING->{
                binding.swipeRefreshLayout.isRefreshing=true
            }

            Resource.Status.SUCCESS->{
                binding.swipeRefreshLayout.isRefreshing=false
                loadData(state.data)

            }

            Resource.Status.ERROR->{
                binding.swipeRefreshLayout.isRefreshing=false
                binding.progressBar.gone()
                Snackbar.make(binding.swipeRefreshLayout,getString(R.string.strErrorMesssagePattern,state.message),
                    Snackbar.LENGTH_LONG)
                    .setAnchorId(R.id.bottom_navigation).show()
            }

            Resource.Status.EMPTY->{
                Log.d("TAG","Empty data state")
            }
        }
    }


    private fun loadData(popularMovies:List<MovieData>?){
        popularMovies?.let {
            if (popularMovieViewModel.isFirstPage()){
                movieListAdapter.clear()
            }
            movieListAdapter.fillList(it)
        }
    }

    private fun setUpRecyclerView(){
        movieListAdapter.setOnMovieClickListener(this)
        binding.recyclerViewMovies.adapter=movieListAdapter
        binding.recyclerViewMovies.addOnScrollListener(object: PaginationScrollListener(binding.recyclerViewMovies.linearLayoutManager){
            override fun isLastPage(): Boolean {
                return popularMovieViewModel.isLastPage()
            }

            override fun isLoading(): Boolean {
                val isLoading=binding.swipeRefreshLayout.isRefreshing
                if (isLoading){
                    binding.progressBar.visible()
                }
                else{
                    binding.progressBar.gone()
                }
                return isLoading
            }

            override fun loadMoreItems() {
                popularMovieViewModel.fetchNextPopularMovies()
            }
        })
    }


    private fun setUpSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            popularMovieViewModel.refreshPopularMovies()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        popularMovieBinding=null
        popularMovieViewModel.disposable?.dispose()
    }

    override fun onItemClick(movieData: MovieData, container: View) {
        val action=PopularMovieFragmentDirections.navigateToMovieDetails(movieData.id,movieData.posterPath)
        val options= ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            Pair.create(container,container.transitionName))
        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }
}