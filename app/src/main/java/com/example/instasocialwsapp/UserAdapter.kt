package com.example.instasocialwsapp

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_element.view.*

class UserAdapter(val context: Activity, val usersList: ArrayList<User>, myUserID : String) : BaseAdapter() {
    val layoutInflater = LayoutInflater.from(context)
    val myUserID = myUserID

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var currentUser = usersList[position]

        //load the layout to show each post element previously saved into Firebase
        val myView = layoutInflater.inflate(R.layout.user_element, null)

        myView.userFollowNameTxtView.text = currentUser.userName.toString()
        Picasso.get()
            .load(currentUser.userProfile)
            .resize(100, 100)
            .centerCrop()
            .into(myView.userFollowPhotoImgView)

        myView.followImgView.setOnClickListener {
            val txt = myView.labelFollowTxtView.text
            if(txt == "Unfollow"){
                removeFollowing(currentUser.userID.toString())
                myView.labelFollowTxtView.text = "Follow"
                myView.labelFollowTxtView.setTextColor(context.getColor(R.color.colorPrimaryDark))
                myView.followImgView.setImageResource(R.drawable.add_user)
            }
            else {
                addFollowing(currentUser.userID.toString())
                myView.labelFollowTxtView.text = "Unfollow"
                myView.labelFollowTxtView.setTextColor(context.getColor(R.color.colorPink))
                myView.followImgView.setImageResource(R.drawable.remove_user)
            }
        }

        if(currentUser.isFollowed!!){
            myView.labelFollowTxtView.text = "Unfollow"
            myView.labelFollowTxtView.setTextColor(context.getColor(R.color.colorPink))
            myView.followImgView.setImageResource(R.drawable.remove_user)
        }
        else{
            myView.labelFollowTxtView.text = "Follow"
            myView.labelFollowTxtView.setTextColor(context.getColor(R.color.colorPrimaryDark))
            myView.followImgView.setImageResource(R.drawable.add_user)
        }
        return myView

    }

    override fun getItem(position: Int): Any {
        return usersList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return usersList.size
    }

    fun addFollowing(userID : String){
        val url = Uri.parse("http://10.0.2.2:8000/edit_follower.php?")
            .buildUpon()
            .appendQueryParameter("oper", "1")
            .appendQueryParameter("user_id", userID)
            .appendQueryParameter("following_user_id", myUserID )
            .build()
            .toString()

        MyAsyncTask(
            onFail = {
                Toast.makeText(context, "Following failed", Toast.LENGTH_SHORT).show()
            },
            onSuccess = { listOfUsers ->
                Toast.makeText(context, "Following this user", Toast.LENGTH_SHORT).show()
            }
        ).execute(url)
    }
    fun removeFollowing(userID : String){
        val url = Uri.parse("http://10.0.2.2:8000/edit_follower.php?")
            .buildUpon()
            .appendQueryParameter("oper", "2")
            .appendQueryParameter("user_id", userID)
            .appendQueryParameter("following_user_id", myUserID )
            .build()
            .toString()

        MyAsyncTask(
            onFail = {
                Toast.makeText(context, "Unfollow failed", Toast.LENGTH_SHORT).show()
            },
            onSuccess = { listOfUsers ->
                Toast.makeText(context, "Unfollowing this user", Toast.LENGTH_SHORT).show()
            }
        ).execute(url)
    }

}