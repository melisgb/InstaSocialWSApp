<?php

//To call service : 
//http://localhost:8000/add_post.php?post_user_id=1&post_content=Hola,soyYo&post_image_url=home/tester.png

require("DBInfo.inc");
include("logger.php");

$query = "insert into instasocial.post(post_user_id, post_content, post_image_url) ".
		 "values (".
		 	$_GET['post_user_id'].
		 	", ".
		 	"'".$_GET['post_content']."'".
		 	", ".
		 	"'".$_GET['post_image_url']."'".
		 ");";

$result = mysqli_query($connect, $query);

wh_log($connect->insert_id);
if(!$result){
	print("{ 'msg' : 'Post failed to save'} ");
}
else{
	$select_query = "select * from instasocial.user_posts where post_id =".
			 $connect->insert_id .";";

	$result = mysqli_query($connect, $select_query);
	$postInfo = array();
	
	while( $row = mysqli_fetch_assoc($result)){
		$postInfo[] = $row;
		break;
	}
	print("{ 'msg' : 'Post saved', 'postInfo': '". json_encode($postInfo)."' }");
}

// mysqli_close($connect);

?>

