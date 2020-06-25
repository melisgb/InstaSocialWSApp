package com.example.instasocialwsapp

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//To use as HTTP Request
class MyAsyncTask(val onSuccess: (Any?) -> Unit, val onFail: () -> Unit) : AsyncTask<String, String, String>() {

    override fun onPreExecute() {
        //before task started
        super.onPreExecute()
    }
    override fun doInBackground(vararg p0: String?): String {
        //http call
        try {
            val url = URL(p0[0])
            val urlConnect = url.openConnection() as HttpURLConnection
            urlConnect.connectTimeout = 5000
            var inString = convertStreamToString(urlConnect.inputStream)
            //this function will publish the progress to the UI
            publishProgress(inString)
        } catch (ex: Exception) {
            Log.e("Exception error", ex.message, ex)
        }
        return " "
    }

    override fun onProgressUpdate(vararg values: String?) {
        try {
            var json = JSONObject(values[0])
            val msg = json.getString("msg")
            if(msg== "Register User is added"){
                Log.d("UserRegistration", msg)
                onSuccess(null) //In RegisterActivity, will finish()
            }
            else if(msg== "Login Successful"){
                val msgInfo = JSONArray(json.getString("info"))
                val userInfo = msgInfo.getJSONObject(0)
                val user_id = userInfo.getString("user_id")
                val username = userInfo.getString("user_name")
                Log.d("UserLogin", username)
                onSuccess(user_id) //For LoginActivity
            }
            else if(msg== "Post saved") {
                Log.d("PostSaved", msg)
                onSuccess(null) //For PostAdapter - Saving post
            }
            else if(msg == "Loading posts successful"){
                Log.d("PostSaved", msg)
                val listOfPosts = ArrayList<Post>()
                val msgInfo = JSONArray(json.getString("postsInfo"))
                for(i in 0 until msgInfo.length()) {
                    val postInfo = msgInfo.getJSONObject(i)
                    listOfPosts.add(
                        Post(
                            postInfo.getString("post_id"),
                            postInfo.getString("post_content"),
                            postInfo.getString("post_image_url"),
                            postInfo.getString("post_date"),
                            postInfo.getString("user_name"),
                            postInfo.getString("user_profile_url") ))
                    //Is it necessary to add an ADD element?
                }

                onSuccess(listOfPosts) //For MainActivity - Retrieving posts
                //TODO: Send the postsList
            }
            else {
                Log.d("Failed", msg)
                onFail()
            }

        } catch (ex: Exception) {
            Log.e("Exception error", ex.message, ex)
        }
    }
    override fun onPostExecute(result : String?) {
        //after the task is done
        super.onPostExecute(result)
    }


    fun convertStreamToString(inputStrm: InputStream) : String{
        val bufferReader = BufferedReader(InputStreamReader(inputStrm))
        var line: String
        var allString :String = ""

        try{
            do {
                line = bufferReader.readLine()
                if(line!=null){
                    allString+=line
                }
            }
            while(line != null)
            inputStrm.close()
        }catch (ex:Exception){
            Log.d("Exception error", ex.message)
        }
        return allString
    }
}

