
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
<%@ page import= "java.util.*" %>
  <%class FileIndexer {
	 public  final MonitoredLongValue done = 
	  new  MonitoredLongValue(
	 "visualiq.ums.app.sample",
	 "Time spend indexing",
	 MonitorableRegistry.DEFAULT_REGISTRY,
	 
	 1L, 
	 SI.NANO(SI.SECOND));
  
  }
  FileIndexer obj=new FileIndexer();    //Works fine on first run
  PcpMmvWriter bridge=new PcpMmvWriter("mmvname",IdentifierSourceSet.DEFAULT_SET);
  PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
  List<MonitoredLongValue> coll = new ArrayList<MonitoredLongValue>();
  coll.add(obj.done);  
  MonitoringView monitoringView;
 // monitoringView.startMonitoring(coll); ERROR
  bridge.addMetric(MetricName.parse("visualiq.ums.app.sample"), Semantics.INSTANT,Unit.ONE.times(1000), 7);
  bridge.start();
 
  bridge.updateMetric(MetricName.parse("visualiq.ums.app.sample"), 3);
  
  %>
