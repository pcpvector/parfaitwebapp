<html>
<body>
<h2>Hello World!</h2>

</body>
</html>
 <meta http-equiv="refresh" content="5">
<%@ page import= "static com.myvisualiq.ums.Listenerdemo.*" %>
<%@ page import="java.sql.*" %>
 
<% 
  done.inc();
  done2.dec();
  out.println(" Increment :"+done.get()+", Decrement "+done2.get() );
  
  ResultSet rs=st.executeQuery("select * from Employees");
  out.print("<table border='1' class='table table-striped table-bordered'>");
  out.println("<thead><tr>");
  out.println("<th>Client ID</th>");
  out.println("<th>Client Name</th>");
  out.println("<th>Client Last Name</th>");
  out.println("<th>Age</th>");

  while(rs.next()){
      out.print("<tr>");
      out.print("<td>"+rs.getInt("id")+"</td>");
      out.print("<td>"+rs.getString("first")+"</td>");
      out.print("<td>"+rs.getString("last")+"</td>");
      out.print("<td>"+rs.getInt("age")+"</td>");
      out.print("</tr>");
  }
  out.print("</table>");
  
%>