
<html>
<body>
<h2>Hello World!</h2>
</body>
</html>
 
  <%@ page import = "com.custardsource.parfait.*" %>

  <%class FileIndexer {
	 private final MonitoredLongValue done = 
	  new  MonitoredLongValue(
	 "aconex.indexes.time",
	 "Time spend indexing",
	 MonitorableRegistry.DEFAULT_REGISTRY,
	 // injection = better!
	 0L, // initial value
	 SI.NANO(SI.SECOND));

}
%>