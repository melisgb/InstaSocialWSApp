package com.example.instasocialwsapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.add_post.view.*
import java.net.URLEncoder
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    open var postsList = ArrayList<Post>()
    var postsAdapter : PostAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val savedSettings = SavedSettings(this)
        savedSettings.loadUserSettings()

        postsAdapter = PostAdapter(this, postsList)
        val listViewPost = findViewById<ListView>(R.id.postsListView)
        listViewPost.adapter = postsAdapter

        postsList.clear()
        loadPosts("%", 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        postsAdapter?.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu!!.findItem(R.id.search_note_ic).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                loadPosts("%$query%", 0)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.goToHome -> {
                    //TODO: call operator 3 query
                    loadPosts("%", 0)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun loadPosts(keyword : String, startFrom : Int){
        //TODO: Search into DB based on title, updateList, notifyadapter
        val content = URLEncoder.encode(keyword, "utf-8")
        val url = "http://10.0.2.2:8000/get_posts.php?case=3&keyword=${content}&startFrom=${startFrom}"

        MyAsyncTask(
            onFail = {
                Toast.makeText(applicationContext, "Retrieving posts failed", Toast.LENGTH_SHORT).show()
                postsList.clear()
                postsList.add(
                    Post(
                        "1",
                        "",
                        "url",
                        "2019-12-03",
                        "add",
                        "profImg" ))

                postsAdapter!!.notifyDataSetChanged()
            },
            onSuccess = { listOfPosts ->
                Toast.makeText(applicationContext, "Loading posts", Toast.LENGTH_SHORT).show()
                postsList.clear()
                postsList.add(
                    Post(
                        "1",
                        "",
                        "url",
                        "2019-12-03",
                        "add",
                        "profImg" ))
                postsList.addAll(listOfPosts as ArrayList<Post>)
                postsAdapter!!.notifyDataSetChanged()
            }
        ).execute(url)
    }
    fun getPosts(){
        //TODO: Read from DB and load into the list
    }

}