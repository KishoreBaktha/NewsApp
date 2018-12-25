package com.example.kishorebaktha.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.net.Uri
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

//LoaderManager for asynchrounous callbacks
class MainActivity : AppCompatActivity() ,LoaderManager.LoaderCallbacks<List<Article>>
{
    private val articleLoaderId=1
    private val newsendpointapi="https://newsapi.org/v2/top-headlines?country=us&apiKey=95cd27fd556548aaa30868c58e182cbb"

    override fun onCreateLoader(id: Int, args: Bundle?): android.content.Loader<List<Article>> {

        var baseURI: Uri =Uri.parse(newsendpointapi)
        var UriBuilder:Uri.Builder=baseURI.buildUpon()
//        UriBuilder.appendQueryParameter("source","ars-technica")// ars-technica-website covering news
//        UriBuilder.appendQueryParameter("apiKey",)
        return object:android.content.AsyncTaskLoader<List<Article>>(this)
       {
           override fun onStartLoading() {
               forceLoad()
           }

           override fun loadInBackground(): List<Article>? {
               return QueryUtils.fetchArticleData(UriBuilder.toString())
           }
       }
    }

    override fun onLoadFinished(loader: android.content.Loader<List<Article>>?, data: List<Article>) {
        if(!data.isEmpty()&&data!=null)
        {
          articlesrv.adapter=ArticleAdapter(this,data)
        }
    }

    override fun onLoaderReset(loader: android.content.Loader<List<Article>>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //have only one async task at a time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        articlesrv.layoutManager=LinearLayoutManager(this)
        articlesrv.setHasFixedSize(true)
        runLoaders()
    }

    private fun runLoaders()
    {
        var connManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo=connManager.activeNetworkInfo
        //check if network is available
        if(networkInfo!=null &&networkInfo.isConnected)
        {
            loaderManager.initLoader(articleLoaderId,null, this)
        }
        else
        {
            Toast.makeText(this,"No net",Toast.LENGTH_LONG).show()
        }
    }

}
