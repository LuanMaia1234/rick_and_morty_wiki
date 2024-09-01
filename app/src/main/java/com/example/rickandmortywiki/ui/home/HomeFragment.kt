package com.example.rickandmortywiki.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.data.entities.CharacterEntity
import com.example.rickandmortywiki.databinding.FragmentHomeBinding
import com.example.rickandmortywiki.ui.common.CharacterState
import com.example.rickandmortywiki.utils.channel.ChannelImpl
import com.example.rickandmortywiki.utils.channel.Event
import com.example.rickandmortywiki.utils.extensions.collectLifecycleFlow
import com.example.rickandmortywiki.utils.extensions.gone
import com.example.rickandmortywiki.utils.extensions.visible
import com.example.rickandmortywiki.utils.listeners.CharacterItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class HomeFragment : Fragment(), CharacterItemClickListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    private var indicatorList: List<ImageView> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCarousel()
        collectLifecycleFlow(viewModel.state) { state ->
            when (state) {
                is CharacterState.Loading -> {
                    binding.progressBar.visible()
                    binding.emptyState.root.gone()
                }

                is CharacterState.Success -> {
                    binding.progressBar.gone()
                    val characters = state.characters
                    if (characters.isEmpty()) binding.emptyState.root.visible()
                    homeAdapter.submitList(characters)
                    setupIndicator(characters.size)
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
            viewModel.getCharacters()
        }
    }

    private fun setupCarousel() {
        homeAdapter = HomeAdapter(this)
        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updatePagePosition(position)
                indicatorList.forEachIndexed { index, imageView ->
                    if (position == index) {
                        imageView.setImageResource(R.drawable.selected_indicator)
                    } else {
                        imageView.setImageResource(R.drawable.unselected_indicator)
                    }
                }
            }
        }
        binding.carouselViewPager.apply {
            adapter = homeAdapter
            registerOnPageChangeCallback(pageChangeListener)
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            setPageTransformer { page, position ->
                page.scaleY = 0.85f + (1 - abs(position)) * 0.15f
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.carouselViewPager.setCurrentItem(viewModel.currentPagePosition, false)
    }

    private fun setupIndicator(size: Int) {
        if (binding.carouselIndicator.childCount == 0) {
            indicatorList = List(size) { ImageView(context) }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                setMargins(0, 0, 8, 0)
            }
            indicatorList.forEach {
                it.setImageResource(R.drawable.unselected_indicator)
                binding.carouselIndicator.addView(it, params)
            }

            if (indicatorList.isNotEmpty()) {
                indicatorList[0].setImageResource(R.drawable.selected_indicator)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.carouselViewPager.unregisterOnPageChangeCallback(pageChangeListener)
        _binding = null
    }

    override fun onItemClick(character: CharacterEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToCharacterDetailsFragment(character)
        findNavController().navigate(action)
    }
}