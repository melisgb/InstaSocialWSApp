<?php

//To call service : 

//Different cases: 
//Case 1 - case 3,   query = ?, startFrom = x.
//http://localhost:8000/get_posts.php?case=3&keyword=ia&startFrom=0

require("DBInfo.inc");
include("logger.php");

if($_GET['case'] == 1){
	//http://localhost:8000/get_posts.php?case=1&user_id=60&startFrom=0
	$query = "select * from instasocial.user_posts where post_user_id in (
		select following_user_id 
		  from instasocial.follower 
		  where user_id = ".
		  $_GET['user_id']. 
		  " ) ".
		  "or post_user_id = ".
		  $_GET['user_id']. 
		  " order by post_date desc".
			" limit 20 offset ".
			 $_GET['startFrom'].
			 ";";	
}
elseif($_GET['case'] == 2){
	//http://localhost:8000/get_posts.php?case=2&user_id=60&startFrom=0
	$query = "select * from instasocial.user_posts where post_user_id =".
		  	$_GET['user_id']. 
			" order by post_date desc ".
			" limit 20 offset ".
			 $_GET['startFrom'].
			 ";";
}
elseif($_GET['case'] == 3){
	//http://localhost:8000/get_posts.php?case=3&keyword=ia&startFrom=0
	$query = "select * from instasocial.user_posts where post_content like '%".
			 $_GET['keyword'].
			 "%' limit 20 offset ".
			 $_GET['startFrom'].
			 ";";
}
// To use only when debugging
// var_dump($query);

wh_log($query);
$result = mysqli_query($connect, $query);

if(!$result){
	die('Error running query');
}

$postsInfo = array();

while( $row = mysqli_fetch_assoc($result)){
	$postsInfo[] = $row;
}

if($postsInfo){
	print("{ 'msg' : 'Loading posts successful', 'postsInfo': '". json_encode($postsInfo)."' }");

} else {
	print("{ 'msg' : 'Loading posts - no posts' }");
}

mysqli_free_result($result);
// mysqli_close($connect);

?>



