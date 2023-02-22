package com.kalok.coroutineituneslist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kalok.coroutineituneslist.adapters.SongAdapter
import com.kalok.coroutineituneslist.adapters.HomeListAdapter
import com.kalok.coroutineituneslist.utils.setup
import com.kalok.coroutineituneslist.databinding.FragmentHomeBinding
import com.kalok.coroutineituneslist.utils.MediaPlayerUtils
import com.kalok.coroutineituneslist.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var _viewAdapter : SongAdapter
    private lateinit var _viewManager : RecyclerView.LayoutManager
    private lateinit var _shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val homeViewModel: HomeViewModel by viewModel()

        // Use Shimmer Layout to display shimmer loading screen
        _shimmerLayout = binding.shimmerLayout
        _shimmerLayout.visibility = View.VISIBLE
        _shimmerLayout.startShimmer()

        // Get LinearLayoutManager for RecyclerView
        _viewManager = LinearLayoutManager(context)

        val songRecyclerView = binding.songRecyclerView
        // Set recycler view invisible at the beginning for loading
        songRecyclerView.visibility = View.INVISIBLE

        // Fetch data
        homeViewModel.fetchData()

        // Observe for song list to update
        _viewAdapter = HomeListAdapter(homeViewModel.songValue.value)
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.songValue.collect {
                if (it.isNotEmpty()) {
                    // Update the recycler view data when update is observed
                    _viewAdapter.setDataset(it)
                    _shimmerLayout.stopShimmer()
                    songRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        // set up recycler view
        songRecyclerView.apply {
            setup()
            layoutManager = _viewManager
            adapter = _viewAdapter
        }

        return root
    }

    override fun onDetach() {
        MediaPlayerUtils.releasePlayer()
        _binding?.songRecyclerView?.adapter = null
        super.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}