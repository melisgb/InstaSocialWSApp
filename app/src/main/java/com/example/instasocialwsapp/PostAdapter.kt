package com.example.instasocialwsapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.add_post.view.*
import kotlinx.android.synthetic.main.post_element.view.*
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(val context: Activity, val postsList: ArrayList<Post>) : BaseAdapter() {
    var downloadUrl : String? = null
    val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var currentPost = postsList[position]
        if (currentPost.postPersonName.equals("add")) {
            //load the layout to Create a Post in Firebase
            val myView = layoutInflater.inflate(R.layout.add_post, null)
            myView.postAttachmentImgView.setOnClickListener {
                loadImage()
                myView.postAttachmentImgView.isEnabled = false
            }

            myView.postSendImgView.setOnClickListener {
                //savePost into DB
                val url = Uri.parse("http://10.0.2.2:8000/add_post.php?")
                    .buildUpon()
                    .appendQueryParameter("post_user_id", SavedSettings.userID)
                    .appendQueryParameter("post_content", myView.postContentEText.text.toString())
                    .appendQueryParameter("post_image_url", downloadUrl)
                    .build()
                    .toString()

                MyAsyncTask(
                    onFail = {
                        Toast.makeText(context, "Post failed", Toast.LENGTH_SHORT).show()
                    },
                    onSuccess = {newPost ->
                        Toast.makeText(context, "Post successful", Toast.LENGTH_SHORT).show()
                        myView.postContentEText.setText("")
                        //TODO: Receive the new post info and add it to the list.

                        postsList.add(newPost as Post)
                        notifyDataSetChanged()
                    }
                ).execute(url)

            }
            return myView
        }
        else if (currentPost.postPersonName.equals("loading")){
            val myView = layoutInflater.inflate(R.layout.loading_image_post, null)
            return myView

        }
        else {
            //load the layout to show each post element previously saved into Firebase
            val myView = layoutInflater.inflate(R.layout.post_element, null)

            myView.dateTxtView.text = currentPost.dateCreated.toString()
            myView.contentTxtView.text = currentPost.postContent.toString()
            myView.userNameTxtView.text = currentPost.postPersonName.toString()
            Picasso.get()
                .load(currentPost.postImageUrl.toString())
                .resize(100, 100)
                .centerCrop()
                .into(myView.contentImgView)
            return myView
        }
    }

    override fun getItem(position: Int): Any {
        return postsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return postsList.size
    }

    val PICK_IMG_CODE = 123 //this is used as a code for requesting images from device.
    fun loadImage(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
        context.startActivityForResult(intent, PICK_IMG_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMG_CODE && data != null && resultCode == AppCompatActivity.RESULT_OK){
            val selectedImage = data.data
            val pathImageCol = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(
                selectedImage!!,
                pathImageCol,
                null,
                null )
            cursor!!.moveToFirst()

            val colIndex = cursor.getColumnIndex(pathImageCol[0])
            val pathImage = cursor.getString(colIndex)
            cursor.close()

            //this will be set to a low size file.
            uploadImage(BitmapFactory.decodeFile(pathImage))
        }
    }

    fun uploadImage(bitmap: Bitmap){
        postsList.add(0,
            Post(
                "120",
                "Empty",
                "url",
                "date",
                "loading",
                "profileurl" ))  //to show LOADING layout
        notifyDataSetChanged()

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://instasocialwsapp.appspot.com")

        val df = SimpleDateFormat("ddMMyy_hhmmss")
        val currentDate = df.format(Date())
        val imageFilename = SavedSettings.userID +"_" + currentDate + ".jpg"
        val imageRef = storageRef.child("postImages/"+ imageFilename)

        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArray) //this is to reduce the file size from the phone's image  Range from 0-100
        val data = byteArray.toByteArray()

        val uploadTask = imageRef.putBytes(data)  //Uploading image into Firebase using the imageRef.

        var a : Task<Int>? = null
        a.toString()
        uploadTask
            .addOnFailureListener {
                Toast.makeText(context, "Failed image upload", Toast.LENGTH_SHORT).show()
                Log.e("Upload Error", uploadTask.exception.toString())
            }
            .continueWithTask { imageRef.downloadUrl }
            .addOnSuccessListener { uri ->
                downloadUrl = uri.toString()
                postsList.removeAt(0) //to delete the loading node in this list
                notifyDataSetChanged()
                Log.d("DOWNLOAD URL: ", downloadUrl)
            }
    }
}