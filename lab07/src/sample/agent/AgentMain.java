package sample.agent;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class AgentMain {
    public static void main(String[] args) throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName mxbeanName = new ObjectName("sample.agent:type=JMX - lab07");

        List<MyCategory> list = new ArrayList<>();
        Random r = new Random();

        for(String s : args){
            int random = r.nextInt((10 - 1) + 1) + 1; //((max - min) + 1) + min;
            list.add(new MyCategory(s, random));
            System.out.println(s + " -- " + random);
        }

        AgentMXBeanImpl mxbean = new AgentMXBeanImpl();
        mxbean.setCategories(list);

        mbs.registerMBean(mxbean, mxbeanName);
        System.out.println("AgentMain is ready...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
