<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Apply" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<title>FindHere Administrator</title>
	<link rel="stylesheet" href="/FindHere/css/style.css" type="text/css" media="all" />
</head>
<body>
<!-- Header -->
<div id="header">
	<div class="shell">
		<!-- Logo + Top Nav -->
		<div id="top">
			<h1><a href="#">FindHere</a></h1>
			<div id="top-navigation">
				Welcome <a href="#"><strong>Administrator</strong></a>
				<span>|</span>
				<a href="/FindHere/LogOut">Log out</a>
			</div>
		</div>
		<!-- End Logo + Top Nav -->
		
		<!-- Main Nav -->
		<div id="navigation">
			<ul>
			    <li><a href="#" class="active"><span>Member</span></a></li>
			    <li><a href="#"><span>New Articles</span></a></li>
			    <li><a href="#"><span>User Management</span></a></li>
			    <li><a href="#"><span>Photo Gallery</span></a></li>
			    <li><a href="#"><span>Products</span></a></li>
			    <li><a href="#"><span>Services Control</span></a></li>
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
			<a href="#">Member</a>
			<span>&gt;</span>
			Current application
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
					<!-- Box Head -->
					<div class="box-head">
						<h2 class="left">Current application</h2>
						<div class="right">
							<label>search articles</label>
							<input type="text" class="field small-field" />
							<input type="submit" class="button" value="search" />
						</div>
					</div>
					<!-- End Box Head -->	

					<!-- Table -->
					<div class="table">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<th width="13"><input type="checkbox" class="checkbox" /></th>
								<th>userID</th>
								<th>name</th>
								<th>inc</th>
								<th>nationalID</th>
								<th width="110" class="ac">Content Control</th>
							</tr>
							<%
							List<Apply> applyList = (List<Apply>)request.getAttribute("applyList");
							int pageIndex = Integer.parseInt(request.getAttribute("pageIndex").toString());
							for(int i = 0; i < applyList.size(); i++){
								String odd = (i%2 == 1) ? "odd":"";
							%>
							<tr class=<%=odd %> >
								<td><input type="checkbox" class="checkbox" /></td>
								<td><h3><%=applyList.get(i).getUserID() %></h3></td>
								<td><%=applyList.get(i).getName() %></td>
								<td><%=applyList.get(i).getInc() %></td>
								<td><%=applyList.get(i).getNationalID() %></td>
								<td>
									<a href="/FindHere/RejectApply?
										applyID=<%=applyList.get(i).getId().toString() %>
										&pageIndex=<%=pageIndex %>" class="ico del">Delete</a>
									<a href="/FindHere/AgreeMember?
										userID=<%=applyList.get(i).getUserID() %>
										&applyID=<%=applyList.get(i).getId().toString() %>
										&pageIndex=<%=pageIndex %>" class="ico ok">Agree</a>
								</td>
							</tr>
							<%
							}
							%>
						</table>
						
						
						<!-- Pagging -->
						<div class="pagging">
							<div class="left">Showing 1-12 of 44</div>
							<div class="right">
								<% 
								int previous = (pageIndex-1<0)? 0:pageIndex-1;
								int next = pageIndex + 1;
								%>
								<a href="/FindHere/ConfirmMember?pageIndex=0">Start</a>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=previous %>">Previous</a>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=pageIndex %>"><font color="red"><%=pageIndex+1 %></font></a>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=pageIndex+1 %>"><%=pageIndex+2 %></a>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=pageIndex+2 %>"><%=pageIndex+3 %></a>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=pageIndex+3 %>"><%=pageIndex+4 %></a>
								<span>...</span>
								<a href="/FindHere/ConfirmMember?pageIndex=<%=next %>">Next</a>
							</div>
						</div>
						<!-- End Pagging -->
						
					</div>
					<!-- Table -->
					
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
