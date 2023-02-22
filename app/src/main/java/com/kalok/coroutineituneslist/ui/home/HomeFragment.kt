package com.kalok.coroutineituneslist.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kalok.coroutineituneslist.adapters.HomeListAdapter
import com.kalok.coroutineituneslist.adapters.SongAdapter
import com.kalok.coroutineituneslist.databinding.FragmentHomeBinding
import com.kalok.coroutineituneslist.utils.MediaPlayerUtils
import com.kalok.coroutineituneslist.utils.setup
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

        startShimmer()

        // Get LinearLayoutManager for RecyclerView
        _viewManager = LinearLayoutManager(context)

        val songRecyclerView = binding.songRecyclerView
        // Set recycler view invisible at the beginning for loading
        songRecyclerView.visibility = View.INVISIBLE

        hideKeyboard()

        // Fetch data
        homeViewModel.fetchData()

        // Observe for song list to update
        _viewAdapter = HomeListAdapter(homeViewModel.songValue.value)
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.songValue.collect {
                if (it.isNotEmpty()) {
                    // Update the recycler view data when update is observed
                    _viewAdapter.setDataset(it)
                }
                _shimmerLayout.stopShimmer()
                // Tell the UI to update according to list result
                binding.resultIsEmpty = it.isEmpty()
            }
        }

        // set up recycler view
        songRecyclerView.apply {
            setup()
            layoutManager = _viewManager
            adapter = _viewAdapter
        }

        // Search music behaviour
        binding.searchButton.setOnClickListener {
            val keyword = binding.searchTextView.text.toString()
            if (keyword.trim().isNotEmpty()) {
                // Search by keyword
                homeViewModel.fetchData(keyword)
                // Hide keyboard after search
                hideKeyboard()
                // Dismiss recycler view and start shimmer loading screen
                songRecyclerView.visibility = View.GONE
                binding.noResultTextView.visibility = View.GONE
                startShimmer()
            }
        }

        songRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Hide keyboard if scrolled
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    hideKeyboard()
                }
            }
        })

        return root
    }

    // Hide keyboard
    private fun hideKeyboard() {
        val manager: InputMethodManager? =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        manager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    // Use Shimmer Layout to display shimmer loading screen
    private fun startShimmer() {
        _shimmerLayout = binding.shimmerLayout
        _shimmerLayout.visibility = View.VISIBLE
        _shimmerLayout.startShimmer()
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