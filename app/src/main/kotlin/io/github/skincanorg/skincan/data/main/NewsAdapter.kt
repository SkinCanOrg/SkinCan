package io.github.skincanorg.skincan.data.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import io.github.skincanorg.skincan.databinding.NewsCardBinding
import org.json.JSONArray
import org.json.JSONObject

class NewsAdapter(private val data: JSONArray) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: NewsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: JSONObject) {
            binding.apply {
                Glide.with(ivNewsThumbnail)
                    .load(data.getString("img"))
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(ivNewsThumbnail)
                tvNewsTitle.text = data.getString("title")
                tvNewsSummary.text = data.getString("content")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.length()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position] as JSONObject)
    }
}