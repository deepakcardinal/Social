<%@page import="com.deepak.social.google.TokenStatus"%>
<%TokenStatus ts =(TokenStatus) request.getAttribute("ts");
out.print("Email      :"+ts.getEmail()+"<br>");
out.print("Gender     :"+ts.getGender()+"<br>");
out.print("First Name :"+ts.getFirstName()+"<br>");
out.print("Last Name  :"+ts.getLastName()+"<br>");

%>