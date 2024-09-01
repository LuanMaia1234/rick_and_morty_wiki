package com.example.rickandmortywiki.ui.residents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.databinding.FragmentResidentsBinding
import com.example.rickandmortywiki.ui.common.CharacterAdapter
import com.example.rickandmortywiki.ui.common.CharacterState
import com.example.rickandmortywiki.utils.channel.ChannelImpl
import com.example.rickandmortywiki.utils.channel.Event
import com.example.rickandmortywiki.utils.extensions.collectLifecycleFlow
import com.example.rickandmortywiki.utils.extensions.gone
import com.example.rickandmortywiki.utils.extensions.visible
import com.example.rickandmortywiki.utils.listeners.CharacterItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResidentsFragment : Fragment(), CharacterItemClickListener {
    private var _binding: FragmentResidentsBinding? = null

    private val binding get() = _binding!!

    private val args: ResidentsFragmentArgs by navArgs()

    private val viewModel: ResidentViewModel by viewModel()

    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCharactersById(args.residentIdList.toList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResidentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupRecyclerView()
        collectLifecycleFlow(viewModel.state) { state ->
            when (state) {
                is CharacterState.Loading -> {
                    binding.progressBar.visible()
                    binding.emptyState.root.gone()
                }

                is CharacterState.Success -> {
                    binding.progressBar.gone()
                    if (state.characters.isEmpty()) binding.emptyState.root.visible()
                    characterAdapter.submitList(state.characters)
                }

                is CharacterState.Error -> {
                    binding.progressBar.gone()
                    binding.emptyState.root.visible()
                    ChannelImpl.send(Event.ShowSnackBar(state.message))
                }

                else -> Unit
            }

        }
        binding.emptyState.retryButton.setOnClickListener {
            viewModel.getCharactersById(args.residentIdList.toList())
        }
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.appBarMain.toolbar)
        activity.supportActionBar!!.title = getString(R.string.residents)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.appBarMain.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter(this)
        binding.recyclerView.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(character: CharacterEntity) {
        val action =
            ResidentsFragmentDirections.actionResidentsFragmentToCharacterDetailsFragment(character)
        findNavController().navigate(action)
    }
}