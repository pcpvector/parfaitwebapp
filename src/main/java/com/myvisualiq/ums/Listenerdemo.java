package com.myvisualiq.ums;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.measure.unit.SI;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;

import com.custardsource.parfait.Monitorable;
import com.custardsource.parfait.MonitorableRegistry;
import com.custardsource.parfait.MonitoredIntValue;
import com.custardsource.parfait.MonitoredLongValue;
import com.custardsource.parfait.dxm.IdentifierSourceSet;
import com.custardsource.parfait.dxm.PcpMmvWriter;
import com.custardsource.parfait.jdbc.ParfaitDataSource;
import com.custardsource.parfait.pcp.EmptyTextSource;
import com.custardsource.parfait.pcp.MetricDescriptionTextSource;
import com.custardsource.parfait.pcp.MetricNameMapper;
import com.custardsource.parfait.pcp.PcpMonitorBridge;
import com.custardsource.parfait.timing.EventMetricCollector;
import com.custardsource.parfait.timing.EventTimer;
import com.custardsource.parfait.timing.ThreadMetric;
import com.custardsource.parfait.timing.ThreadMetricSuite;
import com.custardsource.parfait.timing.Timeable;

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
	 PcpMmvWriter bridge=new PcpMmvWriter("IQ",IdentifierSourceSet.DEFAULT_SET);
	 PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
	 Collection<Monitorable<?>> coll = new ArrayList<Monitorable<?>>();
	 public static String eventGroup ="event";
	 public static Statement st;
	 public static EventMetricCollector evColl;
	 public ThreadMetricSuite threadMetricSuite=ThreadMetricSuite.blank();
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		bridge1.stopMonitoring(coll);
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		BasicConfigurator.configure();
		Context initContext;
		DataSource ds;
		
		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
		    ds = (DataSource) envContext.lookup("jdbc/EMP");
		    Connection conn = ds.getConnection();
		    st=conn.createStatement();
		    
			ParfaitDataSource parfaitDataSource=new ParfaitDataSource(ds); 
		    Collection<ThreadMetric> jdbccoll=parfaitDataSource.getThreadMetrics();
		    parfaitDataSource.setLogWriter(parfaitDataSource.getLogWriter());
		    threadMetricSuite.addAllMetrics(jdbccoll);
		    EventTimer eventTimer=new EventTimer("viqdemo", MonitorableRegistry.DEFAULT_REGISTRY, threadMetricSuite, enableCpuCollection, enableContentionCollection);
		    eventTimer.registerMetric(eventGroup);
		    EventMetricCollector evColl=eventTimer.getCollector();
		    
		    evColl.startTiming(eventGroup, eventGroup);
		    
		   // Timeable t=;
		   // t.setEventTimer(eventTimer);
		    
		    // Creates metrics, and injects the EventTimer into the Timeable
		    	   // so it can access it later
		    	//   eventTimer.registerTimeable(t, "someSuitableNameForT");
		    
            
		    //eventTimer.registerTimeable(t, "someSuitableNameForT");
		     
		    
		    coll.add(done);
			coll.add(done2);
			bridge1.startMonitoring(coll);   
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		 
	}
	
}
