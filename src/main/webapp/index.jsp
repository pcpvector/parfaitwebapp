<html>
<body>

<%@ page import= "MonitoredLongValue.class" %>
<%
 class FileIndexer {
private final MonitoredLongValue done =
new MonitoredLongValue(
"aconex.indexes.time",
"Time spend indexing",
MonitorableRegistry.DEFAULT_REGISTRY,
// injection = better!
0L, // initial value
SI.NANO(SI.SECOND));
}
%>
<h2>Hello World!</h2>
</body>
</html>




