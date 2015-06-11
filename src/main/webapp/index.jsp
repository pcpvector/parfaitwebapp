
<html>
<body>
<h2>Hello World!</h2>
</body>
</html>
 
  <%@ page import = "com.custardsource.parfait.*" %>
  <%@ page import = "javax.measure.unit.*" %>

  <%class FileIndexer {
	 private final MonitoredLongValue done = 
	  new  MonitoredLongValue(
	 "aconex.indexes.time",
	 "Time spend indexing",
	 MonitorableRegistry.DEFAULT_REGISTRY,
	
	 0L, 
	 SI.NANO(SI.SECOND));

}
%>