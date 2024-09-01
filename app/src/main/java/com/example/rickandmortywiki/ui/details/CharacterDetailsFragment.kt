package com.example.rickandmortywiki.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickandmortywiki.R
import com.example.rickandmortywiki.databinding.FragmentCharacterDetailsBinding
import com.example.rickandmortywiki.utils.extensions.drawable
import com.facebook.shimmer.Shimmer

class CharacterDetailsFragment : Fragment() {
    private var _binding: FragmentCharacterDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        val character = args.character
        binding.apply {
            Glide.with(imageView).load(character.image)
                .placeholder(Shimmer.AlphaHighlightBuilder().drawable)
                .error(R.drawable.ic_broken_image).into(imageView)
            statusValueTextView.text = getString(character.status.resText)
            statusValueTextView.setTextColor(
                ContextCompat.getColor(requireActivity(), character.status.resColor)
            )
            specieValueTextView.text = character.species
            genderValueTextView.text = getString(character.gender.resValue)
            locationValueTextView.text = character.origin.name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.appBarMain.toolbar)
        activity.supportActionBar!!.title = args.character.name
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.appBarMain.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}