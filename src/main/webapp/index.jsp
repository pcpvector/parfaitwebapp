
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
<%@ page import= "com.custardsource.parfait.pcp.PcpMonitorBridge" %>
<%@ page import= "com.custardsource.parfait.pcp.*" %>
<%@ page import= "java.io.File" %>
 
  <%class FileIndexer {
	 public  final MonitoredLongValue done = 
	  new  MonitoredLongValue(
	 "aconex.indexes.time",
	 "Time spend indexing",
	 MonitorableRegistry.DEFAULT_REGISTRY,
	 
	 1L, 
	 SI.NANO(SI.SECOND));
  
  }
  FileIndexer obj;
  PcpMmvWriter bridge=new PcpMmvWriter("mmvname",IdentifierSourceSet.DEFAULT_SET);
  PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
 //MonitoringView monitoringView;
// monitoringView.startMonitoring(Collection<Monitorable<?>> monitorables);
  bridge.addMetric(MetricName.parse("aconex.indexes.time"), Semantics.INSTANT,Unit.ONE.times(1000), 7);
  bridge.start();
 // try {
	//    Thread.sleep(5000);                 //1000 milliseconds is one second.
	//} catch(InterruptedException ex) {
	//    Thread.currentThread().interrupt();
	//}
  bridge.updateMetric(MetricName.parse("aconex.indexes.time"), 3);
  
  %>
