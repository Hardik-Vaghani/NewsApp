package com.hardik.bottomenavigation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardik.bottomenavigation.databinding.ItemArticlePreviewBinding
import com.hardik.bottomenavigation.models.Article
import com.hardik.bottomenavigation.models.Source

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    val TAG = NewsAdapter::class.java.simpleName

    inner class ArticleViewHolder(binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivArticleImage: ImageView = binding.ivArticleImage
        val tvSource: TextView = binding.tvSource
        val tvTitle: TextView = binding.tvTitle
        val tvDescription: TextView = binding.tvDescription
        val tvPublishedAt: TextView = binding.tvPublishedAt
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            Log.d(
                TAG,
                "areItemsTheSame: oldItem.url == newItem.url : ${oldItem.url == newItem.url}"
            )
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            Log.d(TAG, "areContentsTheSame: oldItem == newItem : ${oldItem == newItem}")
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@NewsAdapter, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        Log.d(
            TAG,
            "onBindViewHolder: differ.currentList[position] : ${differ.currentList[position]}"
        )
        // Apply fade-in animation
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(500).start()

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.ivArticleImage)
            holder.tvSource.text = article.source?.name
            holder.tvTitle.text = article.title
            holder.tvDescription.text = article.description
            holder.tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(
                        Article(
                            id = article.id ?: "$position ${article.title}",
                            author = article.author ?: "",
                            content = article.content ?: "",
                            description = article.description ?: "",
                            publishedAt = article.publishedAt ?: "",
                            source = Source(id = article.source?.id ?: "", name = article.source?.name ?: ""),
                            title = article.title ?: "",
                            url = article.url ?: "",
                            urlToImage = article.urlToImage ?: ""
                        )
                    )
                }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}