package com.myvisualiq.ums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.measure.unit.SI;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;

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
import com.custardsource.parfait.timing.EventMetricCollector;
import com.custardsource.parfait.timing.EventTimer;
import com.custardsource.parfait.timing.ThreadMetricSuite;
import com.custardsource.parfait.timing.Timeable;

import static com.google.common.collect.Maps.newHashMap;

public class Listenerdemo implements ServletContextListener {
	
	
	
	 private static final boolean enableCpuCollection = true;
	private static final boolean enableContentionCollection = false;
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
	// ThreadMetricSuite threadMetricSuite =new ThreadMetricSuite.withDefaultMetrics();
	// threadMetricSuite.addMetric(StandardThreadMetric.WAITED_TIME);
	 
	// String eventObj="time";
	 private final Map<String, EventTimer> eventTimers = newHashMap();
	 public static final String SEARCH_EVENT_GROUP = "search";
	 private static final List<String> EVENT_GROUPS = Arrays.asList(SEARCH_EVENT_GROUP);
	 
	 
	 
	 
	 
	 PcpMmvWriter bridge=new PcpMmvWriter("IQ",IdentifierSourceSet.DEFAULT_SET);
	 PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
	 Collection<Monitorable<?>> coll = new ArrayList<Monitorable<?>>();
	 
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		bridge1.stopMonitoring(coll);
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		BasicConfigurator.configure();
		for (String eventGroup : EVENT_GROUPS) {
			 EventTimer eventTimer=new EventTimer("viqdemo", MonitorableRegistry.DEFAULT_REGISTRY, ThreadMetricSuite.withDefaultMetrics(), enableCpuCollection, enableContentionCollection);
			 eventTimer.registerMetric(eventGroup);
			// eventTimers.put(eventGroup, eventTimer);
			 EventMetricCollector evColl=eventTimer.getCollector();
		     evColl.startTiming(SEARCH_EVENT_GROUP, SEARCH_EVENT_GROUP);
			 
			 //Timeable t = null;
				   // Creates metrics, and injects the EventTimer into the Timeable
				   // so it can access it later
		    // eventTimer.registerTimeable(timeable.setEventTimer(this);, "someSuitableNameForT");
				
		}
		
		
		 coll.add(done);
		 coll.add(done2);
		 bridge1.startMonitoring(coll);   
		
	}
	
}
