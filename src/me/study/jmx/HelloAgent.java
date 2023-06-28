package me.study.jmx;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;

public class HelloAgent {

    public static void main(String[] args) {
        new HelloAgent();
        System.out.println("HelloAgent is running...");
        waitForEnterPressed();
    }

    private static void waitForEnterPressed() {
        System.out.println("Press to continew...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HelloAgent() {
        // MBeanServer 생성
        MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer("HelloDomain");

        // MBean 인스턴스 생성
        Hello helloMBean = new Hello();

        try {
            LocateRegistry.createRegistry(7777);
            JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7777/hello");

            // Hello MBean Name 정의
            // 도메인명 : name=value,...
            ObjectName helloMBeanName = new ObjectName("HelloDomain:name=helloMBean");

            // helloMBean 등록
            mBeanServer.registerMBean(helloMBean,helloMBeanName);

            // Client에서 접속할 수 있도록 커넥터 생성 및 시작
            JMXConnectorServer connectorServer =
                    JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, mBeanServer);
            connectorServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
