<?php

$host = "127.0.0.1";
$user = "root";
$password = "";
$database = "instasocial";

$connect = mysqli_connect($host, $user, $password, $database);

if(mysqli_connect_errno()){
	die("cannot connect to db". mysqli_connect_error());
}

?>