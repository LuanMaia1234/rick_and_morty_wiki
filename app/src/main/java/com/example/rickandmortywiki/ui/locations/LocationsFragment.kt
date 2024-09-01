package com.example.rickandmortywiki.ui.locations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortywiki.databinding.FragmentLocationsBinding
import com.example.rickandmortywiki.utils.channel.ChannelImpl
import com.example.rickandmortywiki.utils.channel.Event
import com.example.rickandmortywiki.utils.extensions.collectLifecycleFlow
import com.example.rickandmortywiki.utils.extensions.gone
import com.example.rickandmortywiki.utils.extensions.visible
import com.example.rickandmortywiki.utils.listeners.LocationItemClickListener
import com.example.rickandmortywiki.utils.pagination.PaginationScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocationsFragment : Fragment(), LocationItemClickListener {

    private var _binding: FragmentLocationsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: LocationViewModel by viewModel()

    private lateinit var locationAdapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        collectLifecycleFlow(viewModel.state) { state ->
            when (state) {
                is LocationViewModel.LocationState.Loading -> {
                    if (state.currentPage == 1) {
                        binding.progressBar.visible()
                    } else {
                        binding.paginationProgressBar.visible()
                    }
                    binding.emptyState.root.gone()
                }

                is LocationViewModel.LocationState.Success -> {
                    if (binding.progressBar.isVisible) {
                        binding.progressBar.gone()
                    } else {
                        binding.paginationProgressBar.gone()
                    }
                    if (state.locations.isEmpty()) binding.emptyState.root.visible()
                    locationAdapter.submitList(state.locations)
                }

                is LocationViewModel.LocationState.Error -> {
                    if (binding.progressBar.isVisible) {
                        binding.progressBar.gone()
                    } else {
                        binding.paginationProgressBar.gone()
                    }
                    if (state.locations.isEmpty()) binding.emptyState.root.visible()
                    ChannelImpl.send(Event.ShowSnackBar(state.message))
                }

                else -> Unit
            }

        }
        binding.emptyState.retryButton.setOnClickListener {
            viewModel.getLocations()
        }
    }

    private fun setupRecyclerView() {
        locationAdapter = LocationAdapter(this)
        binding.recyclerView.apply {
            adapter = locationAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(
                object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
                    override fun loadMoreItems() {
                        viewModel.getLocations()
                    }

                    override fun isLoading(): Boolean =
                        viewModel.state.value is LocationViewModel.LocationState.Loading

                    override fun isLastPage(): Boolean {
                        val isLastPage =
                            viewModel.state.value.currentPage == viewModel.state.value.totalPages
                        if (isLastPage) binding.recyclerView.setPadding(0)
                        return isLastPage
                    }

                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(residentIdList: List<String>) {
        val action =
            LocationsFragmentDirections.actionLocationsFragmentToResidentsFragment(residentIdList.toTypedArray())
        findNavController().navigate(action)
    }
}
