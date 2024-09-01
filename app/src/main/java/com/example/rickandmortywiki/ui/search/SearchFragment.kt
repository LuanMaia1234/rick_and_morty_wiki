package com.example.rickandmortywiki.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.databinding.FragmentSearchBinding
import com.example.rickandmortywiki.ui.common.CharacterAdapter
import com.example.rickandmortywiki.utils.channel.ChannelImpl
import com.example.rickandmortywiki.utils.channel.Event
import com.example.rickandmortywiki.utils.extensions.collectLifecycleFlow
import com.example.rickandmortywiki.utils.extensions.gone
import com.example.rickandmortywiki.utils.extensions.visible
import com.example.rickandmortywiki.utils.listeners.CharacterItemClickListener
import com.example.rickandmortywiki.utils.pagination.PaginationScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), android.widget.SearchView.OnQueryTextListener,
    CharacterItemClickListener {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        collectLifecycleFlow(viewModel.state) { state ->
            when (state) {
                is SearchViewModel.SearchState.Loading -> {
                    if (state.currentPage == 1) {
                        binding.progressBar.visible()
                        binding.recyclerView.gone()
                    } else {
                        binding.paginationProgressBar.visible()
                    }
                    binding.emptyState.root.gone()
                }

                is SearchViewModel.SearchState.Success -> {
                    if (binding.progressBar.isVisible) {
                        binding.progressBar.gone()
                    } else {
                        binding.paginationProgressBar.gone()
                    }
                    if (state.characters.isNotEmpty()) {
                        binding.recyclerView.visible()
                    } else {
                        binding.emptyState.root.visible()
                    }
                    characterAdapter.submitList(state.characters)
                }

                is SearchViewModel.SearchState.Error -> {
                    if (binding.progressBar.isVisible) {
                        binding.progressBar.gone()
                    } else {
                        binding.paginationProgressBar.gone()
                    }
                    if (state.characters.isEmpty()) binding.emptyState.root.visible()
                    ChannelImpl.send(Event.ShowSnackBar(state.message))
                }

                else -> Unit
            }
        }
        binding.searchView.setOnQueryTextListener(this)
        binding.emptyState.retryButton.gone()
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(this)
        binding.recyclerView.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(
                object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
                    override fun loadMoreItems() {
                        viewModel.getCharacters(isPagination = true)
                    }

                    override fun isLoading(): Boolean =
                        viewModel.state.value is SearchViewModel.SearchState.Loading

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

    override fun onQueryTextSubmit(query: String): Boolean {
        binding.recyclerView.scrollToPosition(0)
        viewModel.getCharacters(query)
        binding.searchView.clearFocus()
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty() && (!binding.searchView.isIconified || binding.searchView.hasFocus())) {
            binding.recyclerView.scrollToPosition(0)
            viewModel.getCharacters()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(character: CharacterEntity) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToCharacterDetailsFragment(character)
        findNavController().navigate(action)
    }
}