
<html>
<body>
<h2>Hello World!</h2>
</body>
</html>
 
  <%@ page import = "com.custardsource.parfait.*" %>
 <%@ page import = "javax.measure.unit.*" %>
 <%@ page import= "com.custardsource.parfait.dxm.*" %>
  <%@ page import= "com.custardsource.parfait.dxm.PcpWriter" %>
<%@ page import= "com.custardsource.parfait.dxm.semantics.*" %>
<%@ page import= "com.custardsource.parfait.dxm.semantics.PcpDimensionSet" %>
<%@ page import= "java.io.File" %>
 
  <%class FileIndexer {
	 private final MonitoredLongValue done = 
	  new  MonitoredLongValue(
	 "aconex.indexes.time",
	 "Time spend indexing",
	 MonitorableRegistry.DEFAULT_REGISTRY,
	 
	 0L, 
	 SI.NANO(SI.SECOND));

}
  PcpMmvWriter bridge=new PcpMmvWriter("java",IdentifierSourceSet.DEFAULT_SET);
  bridge.addMetric(MetricName.parse("sheep[baabaablack].bagsfull.count"), Semantics.COUNTER,Unit.ONE.times(1000), 3);
  bridge.start();
  %>
