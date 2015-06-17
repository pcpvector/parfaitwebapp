package com.myvisualiq.ums;

import java.util.ArrayList;
import java.util.Collection;

import javax.measure.unit.SI;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.custardsource.parfait.Monitorable;
import com.custardsource.parfait.MonitorableRegistry;
import com.custardsource.parfait.MonitoredIntValue;
import com.custardsource.parfait.MonitoredLongValue;
import com.custardsource.parfait.dxm.IdentifierSourceSet;
import com.custardsource.parfait.dxm.PcpMmvWriter;
import com.custardsource.parfait.pcp.EmptyTextSource;
import com.custardsource.parfait.pcp.MetricDescriptionTextSource;
import com.custardsource.parfait.pcp.MetricNameMapper;
import com.custardsource.parfait.pcp.PcpMonitorBridge;

public class Listenerdemo implements ServletContextListener {
	
	 public static MonitoredLongValue done = 
			  new  MonitoredLongValue(
						 "visualiq.ums.app.sample",
						 "Time spend indexing",
						 MonitorableRegistry.DEFAULT_REGISTRY,
						 
						 1L, 
						 SI.NANO(SI.SECOND)); 
	 public static MonitoredIntValue done2 = 
			  new  MonitoredIntValue(
						 "visualiq.ums.app.dec",
						 "Decrement Counter",
						 MonitorableRegistry.DEFAULT_REGISTRY,
						 
						 100, 
						 SI.SECOND); 
	 PcpMmvWriter bridge=new PcpMmvWriter("IQ",IdentifierSourceSet.DEFAULT_SET);
	 PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
	 Collection<Monitorable<?>> coll = new ArrayList<Monitorable<?>>();
	 
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		bridge1.stopMonitoring(coll);
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		 coll.add(done);
		 coll.add(done2);
		 bridge1.startMonitoring(coll);   
		 
	}
	
}
