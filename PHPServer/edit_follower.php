<?php

//This is for 1)add a follower and 2) remove a follower.

//To add: 
//http://localhost:8000/edit_follower.php?oper=1&user_id=1&following_user_id=60
//http://localhost:8000/edit_follower.php?oper=2&user_id=1&following_user_id=60

require("DBInfo.inc");
include("logger.php");

if($_GET['oper'] == 1){

	$query = "insert into instasocial.follower (user_id, following_user_id) 
	 			 values ( ".
			 	$_GET['user_id'].
			 	", ".
			 	$_GET['following_user_id'].
			   ");";

	$result = mysqli_query($connect, $query);

	wh_log($query);

	if(!$result){
		print("{ 'msg' : 'Follower failed to add'} ");
	}
	else{
		print("{ 'msg' : 'Follower added'} ");
	}
}

elseif ($_GET['oper'] == 2) {
	$query = "delete from instasocial.follower where ".
			"user_id = ".
		 	$_GET['user_id'].
		 	" and following_user_id = ".
		 	$_GET['following_user_id'].
		    ";";

	$result = mysqli_query($connect, $query);

	wh_log($query);

	if(!$result){
		print("{ 'msg' : 'Follower failed to delete'} ");
	}
	else{
		print("{ 'msg' : 'Follower deleted'} ");
	}
}

// mysqli_close($connect);

?>

