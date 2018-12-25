package com.example.kishorebaktha.newsapp

import android.icu.text.ScientificNumberFormatter
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
//object is used to access all methods
object QueryUtils
{
    private val logTag=QueryUtils::class.java.simpleName
    fun fetchArticleData(requestUrl:String):List<Article>?
    {
        val url=createUrl(requestUrl)
        var jsonResponse:String?=null
        try {
            jsonResponse=getResponsefromHttpUrl(url)
        }
        catch (e:IOException)
        {
            Log.e(logTag,"IOE exception",e)
        }
        return extractfromArticle(jsonResponse)
    }

    private fun extractfromArticle(jsonResponse: String?): List<Article>? {
        if(jsonResponse==null || jsonResponse.isEmpty())
            return null
        var articles= mutableListOf<Article>()
        try {
            val basicjsonresponse=JSONObject(jsonResponse)
            var articleArray=basicjsonresponse.getJSONArray("articles")
            for(i in 0..articleArray.length()-1)
            {
                val currentarticle=articleArray.getJSONObject(i)
                val title= if (currentarticle.has("title")) currentarticle.getString("title")
                else "null" //values are string
                val url=if(currentarticle.has("url")) currentarticle.getString("url")
                else "null"
                articles.add(Article(title,url))
            }
        }
        catch (e:JSONException)
        {
            Log.e(logTag,"Problem parsing article result",e)
        }
        return articles

    }
   @Throws(IOException::class)
    private fun getResponsefromHttpUrl(url: URL?):String? {
     val urlConnection=url?.openConnection() as HttpURLConnection?
        try{
            if(urlConnection?.responseCode==HttpURLConnection.HTTP_OK)
            {
                val inputStream=urlConnection.inputStream
                val scanner=Scanner(inputStream)
                scanner.useDelimiter("\\A")//making inputString readable,\A indicates start of string
                if(scanner.hasNext())
                {
                    return scanner.next()
                }
            }
            else
                Log.e(logTag,"Error in response : "+urlConnection?.responseCode)
        }
        finally {
            urlConnection?.disconnect()
        }
        return null

    }

    private fun createUrl(requestUrl: String): URL? {
      var url:URL?=null//initialise object to null,question mark is mandatory
        try {
            url=URL(requestUrl)
        }
        catch (e:MalformedURLException)
        {
            Log.e(logTag,"Problem handling url")
        }
        return url
    }
}