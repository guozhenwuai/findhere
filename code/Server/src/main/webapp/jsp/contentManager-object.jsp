<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Apply" %>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>FindHere Member</title>
	<link rel="stylesheet" href="/FindHere/css/style.css" type="text/css" media="all" />
	<link rel="stylesheet" href="/FindHere/css/myStyle.css" type="text/css" media="all" /> 
	<script type="text/javascript" src="/FindHere/js/jQuery.js"></script>
	<script type="text/javascript" src="/FindHere/js/jquery.form.js"></script>
	<script src="/FindHere/webgl/TemplateData/UnityProgress.js"></script>  
    <script src="/FindHere/webgl/Build/UnityLoader.js"></script>
    <script>
      var gameInstance = UnityLoader.instantiate("gameContainer", "/FindHere/webgl/Build/webgl.json", {onProgress: UnityProgress});
    </script>
</head>
<body>

<script type="text/javascript">
$(document).ready(function() {
	$('#uploadObject').ajaxForm({
   		success:function(msg){
   			if(msg == 'success'){
   				alert("Upload SUCCESS");
    		}else{
    			alert(msg);
    		}
    	}
    }); 
});

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

function extractNumber(obj, decimalPlaces, allowNegative) {
	    var temp = obj.value;

	    // avoid changing things if already formatted correctly
	    var reg0Str = '[0-9]*';
	    if (decimalPlaces > 0) {
	        reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
	    } else if (decimalPlaces < 0) {
	        reg0Str += '\\.?[0-9]*';
	    }
	    reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
	    reg0Str = reg0Str + '$';
	    var reg0 = new RegExp(reg0Str);
	    if (reg0.test(temp)) return true;


	    // first replace all non numbers
	    var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
	    var reg1 = new RegExp(reg1Str, 'g');
	    temp = temp.replace(reg1, '');


	    if (allowNegative) {
	        // replace extra negative
	        var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
	        var reg2 = /-/g;
	        temp = temp.replace(reg2, '');
	        if (hasNegative) temp = '-' + temp;
	    }


	    if (decimalPlaces != 0) {
	        var reg3 = /\./g;
	        var reg3Array = reg3.exec(temp);
	        if (reg3Array != null) {
	            // keep only first occurrence of .
	            // and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
	            var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
	            reg3Right = reg3Right.replace(reg3, '');
	            reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
	            temp = temp.substring(0, reg3Array.index) + '.' + reg3Right;
	        }
	    }


	    obj.value = temp;
	} 
function validate(){
	var map = {};
	map["Px"] = document.getElementsByName("Px")[0].value-0;
	map["Py"] = document.getElementsByName("Py")[0].value-0;
	map["Pz"] = document.getElementsByName("Pz")[0].value-0;
	map["Rx"] = document.getElementsByName("Rx")[0].value-0;
	map["Ry"] = document.getElementsByName("Ry")[0].value-0;
	map["Rz"] = document.getElementsByName("Rz")[0].value-0;
	map["Sx"] = document.getElementsByName("Sx")[0].value-0;
	map["Sy"] = document.getElementsByName("Sy")[0].value-0;
	map["Sz"] = document.getElementsByName("Sz")[0].value-0;
	var json = JSON.stringify(map);
	gameInstance.SendMessage("PlaceController","SetTransform",json);
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
			    <li><a href="/FindHere/MemberARModel/View?pageIndex=0"><span>Model Manager</span></a></li>
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
				<div  style="display:inline-block;width:400px; height:400px;padding-left:20px;">
					<form id="uploadObject" action="/FindHere/SetContents/Object" method="post" enctype="multipart/form-data">
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
						<span style="display:inline-block; width:100px">Name</span><input type="text" name="textName">
						<br>
						<br>
						<span class="longLabel">Position: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Px" onkeyup="extractNumber(this, 2, true);" value=0>
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Py" onkeyup="extractNumber(this, 2, true);" value=0>
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Pz" onkeyup="extractNumber(this, 2, true);" value=0>
						<br>
						<br>
						<span class="longLabel">Rotation: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Rx" onkeyup="extractNumber(this, 2, true);" value=0>
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Ry" onkeyup="extractNumber(this, 2, true);" value=0>
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Rz" onkeyup="extractNumber(this, 2, true);" value=0>
						<br>
						<br>
						<span class="longLabel">Scale: </span>
						<span class="shortLabel">X </span><input class="positionInput" type="text" name="Sx" onkeyup="extractNumber(this, 2, true);" value=1>
						<span class="shortLabel">Y </span><input class="positionInput" type="text" name="Sy" onkeyup="extractNumber(this, 2, true);" value=1>
						<span class="shortLabel">Z </span><input class="positionInput" type="text" name="Sz" onkeyup="extractNumber(this, 2, true);" value=1>
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
						<input type="button" value="APPLY" style="margin-right:10px;" onclick="validate()">
						<input id="submitButton" type="submit" value="SUBMIT">
					</form>
					<br><br><br>
					</div>
					
					<div style="display:inline-block;width:560px; height:400px;">
						<br>
						<br>
						<div id="gameContainer" style="width: 560px; height: 400px"></div>
					</div>
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
