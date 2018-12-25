package com.example.kishorebaktha.newsapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kishorebaktha.newsapp.R.id.parent
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


class ArticleAdapter(val context:Context,val articleData:List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ArticleViewHolder {
       var inflater=LayoutInflater.from(parent.context)
        return ArticleViewHolder(inflater.inflate(R.layout.list_item,parent,false))
    }

    override fun getItemCount(): Int {
       return articleData.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
       holder.titleview.text=articleData[position].title
        holder.titleview.setOnClickListener {
            var uri=Uri.parse(articleData[position].url)
            val i = Intent(Intent.ACTION_VIEW,uri)
            context.startActivity(i)
        }
    }

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
  {
     var titleview:TextView=itemView.findViewById(R.id.title) as TextView
  }
}