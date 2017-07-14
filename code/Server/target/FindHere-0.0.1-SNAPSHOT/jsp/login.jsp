<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>FindHere Administrator Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="description" content="Expand, contract, animate forms with jQuery wihtout leaving the page" />
        <meta name="keywords" content="expand, form, css3, jquery, animate, width, height, adapt, unobtrusive javascript"/>
        <link rel="stylesheet" type="text/css" href="/FindHere/css/style2.css" />
		<script src="/FindHere/js/cufon-yui.js" type="text/javascript"></script>
		<script src="/FindHere/js/ChunkFive_400.font.js" type="text/javascript"></script>
		<script type="text/javascript">
			Cufon.replace('h1',{ textShadow: '1px 1px #fff'});
			Cufon.replace('h2',{ textShadow: '1px 1px #fff'});
			Cufon.replace('h3',{ textShadow: '1px 1px #000'});
			Cufon.replace('.back');
		</script>
    </head>
    <body>
		<div class="wrapper">
			<h1>Welcome Administrator</h1>
			<h2>This is the <span>Manager System</span> of FindHere</h2>
			<br>
			<br>
			<div class="content">
				<div id="form_wrapper" class="form_wrapper">
					<form class="login active" action="/FindHere/webLogin">
						<h3>Login</h3>
						<div>
							<label>Username:  (email)</label>
							<input type="text" name="userID" />
							<span class="error">This is an error</span>
						</div>
						<div>
							<label>Password:</label>
							<input type="password" name="password" />
							<span class="error">This is an error</span>
						</div>
						<div class="bottom">
							<input type="submit" value="Login"></input>
							<div class="clear"></div>
						</div>
					</form>
				</div>
				<div class="clear"></div>
			</div>
			<a class="back" href="http://tympanus.net/codrops/2011/01/06/animated-form-switching/">FindHere</a>
		</div>
		

		<!-- The JavaScript -->
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    </body>
</html>