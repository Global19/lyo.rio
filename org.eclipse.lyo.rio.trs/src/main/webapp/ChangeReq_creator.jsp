<!DOCTYPE html>

<%--
 Licensed Materials - Use restricted, please refer to the "Samples Gallery" terms and conditions in the IBM International Program License Agreement (IPLA).
 © Copyright IBM Corporation 2012, 2013. All Rights Reserved. 
 U.S. Government Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 --%>
 
 <%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
 <%
 	String creatorUri = (String) request.getAttribute("creatorUri");
 	String scriptPath = (String) request.getContextPath()+"/TestApp.js";
 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>Test App OSLC Adapter: Resource Creator</title>
 <script type="text/javascript" src=  '<%= scriptPath %>' ></script>
</head>
<body>
<body style="padding: 10px;">
	<div id="changereq-body">
    	<form id="Create" method="POST" class="enter_CR_form">
			<table style="clear: both;">

				<tr>
					<th class="field_label required">Title:</th>
					<td><input name="title" class="required text_input"
						type="text" style="width: 400px" id="title" required autofocus></td>
				</tr>

				<tr>
					<th class="field_label">Description:</th>
					<td><textarea style="width: 400px; height: 150px;"
							id="description" name="description"></textarea></td>
				</tr>
				
				<tr>
					<th class="field_label">Filed Against:</th>
					<td>
						<select name="filedAgainst">
  							<option value="Server">Server</option>
  							<option value="Client">Client</option>
						</select>
					</td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<input type="button"
							value="Submit Bug"
							onclick="javascript: create( '<%= creatorUri %>' )">
						<input type="button" value="Cancel" onclick="javascript: cancel()">
					</td>
				</tr>
						
			
			</table>
		</form>
		
	<div style="clear: both;">
	<p></p>
	</div>
	<div id="status"></div>	

	</div>
</body>
</html>
 