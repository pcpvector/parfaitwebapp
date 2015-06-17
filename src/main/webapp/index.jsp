
<html>
<body>
<h2>Hello World!</h2>

</body>
</html>
 <meta http-equiv="refresh" content="5">
<%@ page import= "static com.myvisualiq.ums.Listenerdemo.*" %>

 
<% 
  done.inc();
  done2.dec();
  out.println(" Increment :"+done.get()+", Decrement "+done2.get() );
%>
