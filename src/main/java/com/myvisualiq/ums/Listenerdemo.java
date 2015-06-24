package com.myvisualiq.ums;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

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
import com.custardsource.parfait.timing.InProgressExporter;
import com.custardsource.parfait.timing.InProgressSnapshot;
import com.custardsource.parfait.timing.ThreadContext;
import com.custardsource.parfait.timing.ThreadMetric;
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
	 PcpMmvWriter bridge=new PcpMmvWriter("IQ",IdentifierSourceSet.DEFAULT_SET);
	 PcpMonitorBridge bridge1 = new PcpMonitorBridge(bridge, MetricNameMapper.PASSTHROUGH_MAPPER, new MetricDescriptionTextSource(), new EmptyTextSource());
	 //Collection<Monitorable<?>> coll = new ArrayList<Monitorable<?>>();
	 public static String eventGroup ="event";
	 public static Statement st;
	 public static EventMetricCollector evColl;
	 public ThreadMetricSuite threadMetricSuite=ThreadMetricSuite.blank();
	 
	 private final Map<String, EventTimer> eventTimers = newHashMap();
	 private static final Random RANDOM = new Random();
	    public static final Object LOCK = new Object();
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		bridge1.stopMonitoring(MonitorableRegistry.DEFAULT_REGISTRY.getMonitorables());
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		BasicConfigurator.configure();
		Context initContext;
		DataSource ds;
		//coll.add(done);
		//coll.add(done2);
		//bridge1.startMonitoring(coll);
		
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
		    //eventTimer.registerMetric(eventGroup);
		   // EventMetricCollector evColl=eventTimer.getCollector();
		    
		 //   evColl.startTiming(eventGroup, eventGroup);
		    
		  //  eventTimers.put(eventGroup, eventTimer);
		    ThreadContext context = new ThreadContext();
	        EmailSender sender = new EmailSender(context);
	        CheckoutBuyer buyer = new CheckoutBuyer(context);
	        eventTimer.registerTimeable(sender, "sendEmail7");
	        eventTimer.registerTimeable(buyer, "buySomething9");

	        Thread t1 = new Thread(sender);
	        Thread t2 = new Thread(buyer);

	        t1.start();
	        t2.start();
	        bridge1.startMonitoring(MonitorableRegistry.DEFAULT_REGISTRY.getMonitorables());
	        InProgressExporter exporter = new InProgressExporter(eventTimer, context);

	        for (int i = 1; i <= 5; i++) {
	            Thread.sleep(1000);

	            InProgressSnapshot snapshot = exporter.getSnapshot();
	            System.out.println(snapshot.asFormattedString());
	        }

	        t1.join();
	        t2.join();
	        
	        
		   
		    
		       
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 
		 
	}
	
	public static abstract class FakeTask implements Timeable, Runnable {
        private EventTimer timer;
        private String action;
        protected ThreadContext context;
        private final Random random = new Random();

        public FakeTask(String action, ThreadContext context) {
            this.action = action;
            this.context = context;
        }
        @Override
        public void setEventTimer(EventTimer timer) {
            this.timer = timer;
        }
        @Override
        public void run() {
            EventMetricCollector collector = timer.getCollector();
            for (int i = 1; i < 30; i++) {
                try {
                    context.put("Name", randomName());
                    context.put("Company", randomCompany());
                    collector.startTiming(this, action);
                    ResultSet rs=st.executeQuery("select * from Employees");
                    doJob(i);
                    
                    collector.stopTiming();
                    context.remove("Name");
                    context.remove("Company");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private String randomCompany() {
            final String[] COMPANIES = new String[]{"ABC Corp", "Boople Inc", "Cabbages Pty Ltd", "Druggles MV"};
            return COMPANIES[random.nextInt(COMPANIES.length)];
        }

        private String randomName() {
            final String[] NAMES = new String[]{"Alex", "Betty", "Carlos", "Dietrich", "Edna"};
            return NAMES[random.nextInt(NAMES.length)];
        }

        protected abstract void doJob(int i) throws Exception;
    }

    public static class CheckoutBuyer extends FakeTask {
        public CheckoutBuyer(ThreadContext context) {
            super("buyItem", context);
        }

        @Override
        protected void doJob(int i) throws InterruptedException {
        	try {
				ResultSet rs=st.executeQuery("select * from Employees");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (i > 10 && i < 20) {
                synchronized (LOCK) {
                    Thread.sleep(RANDOM.nextInt(500) + 500);
                }
            } else {
                Thread.sleep(RANDOM.nextInt(500) + 500);
            }
            
        }
    }

    public static class EmailSender extends FakeTask {
        public EmailSender(ThreadContext context) {
            super("sendMail", context);
        }

        @Override
        protected void doJob(int i) throws Exception {
        	ResultSet rs=st.executeQuery("select * from Employees");
            synchronized (LOCK) {
                Thread.sleep(RANDOM.nextInt(400) + 400);
                if (i >= 25) {
                    for (int j = 0; j < 10000000; j++) {
                        System.out.print("");
                    }
                }
            }

        }
    }
}