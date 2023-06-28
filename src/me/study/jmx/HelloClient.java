package me.study.jmx;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.RuntimeErrorException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;

public class HelloClient {
    public static void main(String[] args) {
        try {
            JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7777/hello");
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);

            // MBeanServerConnection 취득
            MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

            // domains 취득
            String[] domains = connection.getDomains();
            Arrays.sort(domains);
            for (String domain : domains) {
                System.out.println(domain);
            }

            // ObjectName 생성
            ObjectName helloMBeanName = new ObjectName("HelloDomain:name=helloMBean");

            // MBean에 대한 전용 Proxy 생성
            HelloMBean helloMBean = JMX.newMBeanProxy(connection, helloMBeanName, HelloMBean.class, true);

            // MBean 메서드 원격 호출
            helloMBean.setMessage("Hi~~");
            System.out.println(helloMBean.sayHello());

        } catch (RuntimeErrorException e) {
            System.out.println("Error ---> " + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
