package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.myapplication.databinding.MarvelItemBinding
import com.example.myapplication.ui.model.MarvelDisplayData

class MarvelAdapter : Adapter<MarvelAdapter.MarvelViewHolder>() {

    private val list: MutableList<MarvelDisplayData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelViewHolder {
        val binding = MarvelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarvelViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MarvelViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun add(list: List<MarvelDisplayData>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class MarvelViewHolder(
        private val binding: MarvelItemBinding,
    ) : ViewHolder(binding.root) {

        fun bind(marvelDisplayData: MarvelDisplayData) {
            binding.name.text = marvelDisplayData.name
            binding.picture.load(marvelDisplayData.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(2f))
            }
        }
    }
}