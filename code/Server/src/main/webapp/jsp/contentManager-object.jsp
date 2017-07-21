<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Apply" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>FindHere Member</title>
	<link rel="stylesheet" href="/FindHere/css/style.css" type="text/css" media="all" />
	<link rel="stylesheet" href="/FindHere/css/myStyle.css" type="text/css" media="all" />
</head>
<body>

<script type="text/javascript">
var num = 1;
function addTarget(){
	var el = document.getElementById('add');
	el.parentNode.removeChild(el);
	
	var fileName = "textureFile" + num;
	num = num + 1;
	var append = "<br>"+
	"<br>"+
	"<span style='display:inline-block; width:100px'>Texture File</span><input type='file' name='"+fileName+"'>"+
		"<div id='add' style='display:inline-block;'><span style='display:inline-block; width:5px'></span><a href='javascript:addTarget();'><img width='13px' height='13px' src='/FindHere/css/images/add.gif' /></a></div>";
	document.getElementById("texture").innerHTML += append;
}
</script>

<!-- Header -->
<div id="header">
	<div class="shell">
		<!-- Logo + Top Nav -->
		<div id="top">
			<h1><a href="#">FindHere</a></h1>
			<div id="top-navigation">
				Welcome <a href="#"><strong>Member</strong></a>
				<span>|</span>
				<a href="/FindHere/LogOut">Log out</a>
			</div>
		</div>
		<!-- End Logo + Top Nav -->
		
		<!-- Main Nav -->
		<div id="navigation">
			<ul>
			    <li><a href="/FindHere/MemberWelcome"><span>Welcome</span></a></li>
			    <li><a href="/FindHere/MemberTarget?pageIndex=0"><span>Upload Target</span></a></li>
			    <li><a href="#" class="active"><span>Upload Object</span></a></li>
			    <li><a href="/FindHere/MemberVerifyTarget?pageIndex=0"><span>Verifying Target</span></a></li>
			    <li><a href="#"><span>Manage Content</span></a></li>
			</ul>
		</div>
		<!-- End Main Nav -->
	</div>
</div>
<!-- End Header -->

<!-- Container -->
<div id="container">
	<div class="shell">
		
		<!-- Small Nav -->
		<div class="small-nav">
		</div>
		<!-- End Small Nav -->
		
		<br />
		<!-- Main -->
		<div id="main">
			<div class="cl">&nbsp;</div>
			
			<!-- Content -->
			<div id="content">
				
				<!-- Box -->
				<div class="box" style="width:1000px; height:500px">
					<form action="/FindHere/SetContents/Object" method="post" enctype="multipart/form-data">
						<br>
						<span style="display:inline-block; width:100px">TargetID</span><select name="targetID">
							<%
							List<String> targets = (List<String>)request.getAttribute("targetIDs");
							for(int i = 0; i < targets.size(); i++){
							%>
							<option><%=targets.get(i) %></option>
							<%
							}
							%>
						</select>
						<br>
						<br>
						<span class="longLabel">Position: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Px" >
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Py" >
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Pz" >
						<br>
						<br>
						<span class="longLabel">Rotation: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Rx" >
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Ry" >
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Rz" >
						<br>
						<br>
						<span class="longLabel">Scale: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Sx" >
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Sy" >
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Sz" >
						<br>
						<br>
						<span style="display:inline-block; width:100px">Object File</span><input type="file" name="objectFile">
						<br>
						<br>
						<span style="display:inline-block; width:100px">MTL File</span><input type="file" name="MTLFile">
						<div id="texture">
						<br>
						<br>
						<span style="display:inline-block; width:100px">Texture File</span><input type="file" name="textureFile0">
							<div id="add" style="display:inline-block;"><span style='display:inline-block; width:5px'></span><a href='javascript:addTarget();'><img width='13px' height='13px' src='/FindHere/css/images/add.gif' /></a></div>
						</div>
						<br>
						<br>
						<input type="submit" value="SUBMIT">
					</form>
				</div>
				<!-- End Box -->
				

			</div>
			<!-- End Content -->
			
			<!-- Sidebar -->
			<div id="sidebar">
				
			</div>
			<!-- End Sidebar -->
			
			<div class="cl">&nbsp;</div>			
		</div>
		<!-- Main -->
	</div>
</div>
<!-- End Container -->

<!-- Footer -->
<div id="footer">
	<div class="shell">
		<span class="left">&copy; 2017 - FindHere</span>
	</div>
</div>
<!-- End Footer -->
	
</body>
</html>
