package com.example.instasocialwsapp

class Post {
    var postID : String? = null
    var postContent : String? = null
    var postImageUrl : String? = null
    var dateCreated : String? = null
    var postPersonUID : String? = null

    constructor(postID : String, postContent : String, postImageUrl : String, dateCreated: String, postPersonUID : String){
        this.postID = postID
        this.postContent = postContent
        this.postImageUrl = postImageUrl
        this.dateCreated = dateCreated
        this.postPersonUID = postPersonUID
    }


}