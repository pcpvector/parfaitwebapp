
<html>
<body>
<h2>Hello World!</h2>
</body>
</html>
 <meta http-equiv="refresh" content="5">
<%@ page import = "com.custardsource.parfait.*" %>
<%@ page import = "javax.measure.unit.*" %>
<%@ page import= "com.custardsource.parfait.dxm.*" %>
<%@ page import= "com.custardsource.parfait.dxm.PcpWriter" %>
<%@ page import= "com.custardsource.parfait.dxm.semantics.*" %>
<%@ page import= "com.custardsource.parfait.dxm.semantics.PcpDimensionSet" %>
<%@ page import= "com.custardsource.parfait.pcp.PcpMonitorBridge" %>
<%@ page import= "com.custardsource.parfait.pcp.*" %>
<%@ page import= "com.custardsource.parfait.Monitorable" %>
<%@ page import= "com.custardsource.parfait.MonitoringView" %>
<%@ page import= "java.io.File" %>
<%@ page import= "java.util.*" %>
  <%! static MonitoredLongValue done = 
		  new  MonitoredLongValue(
					 "visualiq.ums.app.sample",
					 "Time spend indexing",
					 MonitorableRegistry.DEFAULT_REGISTRY,
					 
					 1L, 
					 SI.NANO(SI.SECOND)); 
  %>
  <% 
  done.inc();
  out.println(done.get());
  
  PcpMmvWriter bridge=new PcpMmvWriter("mmvname",IdentifierSourceSet.DEFAULT_SET);
  PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
  Collection<Monitorable<?>> coll = new ArrayList<Monitorable<?>>();
  coll.add(done);  
  MonitoringView monitoringView=new MonitoringView();  //ERROR
 monitoringView.startMonitoring(coll);   
  bridge.addMetric(MetricName.parse("visualiq.ums.app.sample"), Semantics.INSTANT,Unit.ONE.times(1000), 7);
  bridge.start();
 
  bridge.updateMetric(MetricName.parse("visualiq.ums.app.sample"), done.get());
  
  %>
