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
import com.example.devrevassignment.status.Resource
import com.example.devrevassignment.viewmodel.PopularMovieViewModel
import com.example.devrevassignment.viewmodel.UpComingMovieViewModel
import com.example.devrevassignment.widgets.PaginationScrollListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UpComingMovieFragment : Fragment(R.layout.fragment_movielist),MovieListAdapter.OnItemClickListener {

    private val upComingMovieViewModel: UpComingMovieViewModel by sharedViewModel()
    private val movieListAdapter: MovieListAdapter by inject()
    private var upcomingMovieBinding: FragmentMovielistBinding?=null
    private val binding get() = upcomingMovieBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        upcomingMovieBinding= FragmentMovielistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        setUpSwipeRefresh()
        upComingMovieViewModel.refreshUpComingMovies()
        viewLifecycleOwner.lifecycleScope.launch {
            upComingMovieViewModel.upcomingMovies.collect{
                handleUpComingMovieResponse(it)
            }
        }
    }

    private fun handleUpComingMovieResponse(state: Resource<List<MovieData>>){
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
            if (upComingMovieViewModel.isFirstPage()){
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
                return upComingMovieViewModel.isLastPage()
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
                upComingMovieViewModel.fetchNextUpComingMovies()
            }
        })
    }


    private fun setUpSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            upComingMovieViewModel.refreshUpComingMovies()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        upcomingMovieBinding=null
        upComingMovieViewModel.disposable?.dispose()
    }

    override fun onItemClick(movieData: MovieData, container: View) {
        val action=UpComingMovieFragmentDirections.navigateToMovieDetails(movieData.id,movieData.posterPath)
        val options= ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            Pair.create(container,container.transitionName))
        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }
}