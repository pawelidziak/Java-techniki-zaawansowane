package sample.agent;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.*;

public class AgentMXBeanImpl extends NotificationBroadcasterSupport implements AgentMXBean {

    private static long sequenceNumber = 0;
    private List<MyCategory> listOfCategories;

    public AgentMXBeanImpl() {
    }

    @Override
    public List<MyCategory> getCategories() {
        return listOfCategories;
    }

    @Override
    public void setCategories(List<MyCategory> categories) {
        listOfCategories = categories;
    }

    @Override
    public int getPriority(String nameOfCategory) {
        Optional<MyCategory> result = listOfCategories.stream()
                .filter(a -> Objects.equals(a.getName(), nameOfCategory))
                .findFirst();
        int value = result.get().getPriority();
        mySendNotification("Get priority: " + nameOfCategory,
                "priority", "int",
                value, value);
        return value;
    }

    @Override
    public void setPriority(String nameOfCategory, int priority) {
        Optional<MyCategory> result = listOfCategories.stream()
                .filter(a -> Objects.equals(a.getName(), nameOfCategory))
                .findFirst();
        int oldValue = result.get().getPriority();
        result.get().setPriority(priority);
        mySendNotification("New priority for: " + nameOfCategory,
                "nameOfCategory, priority", "String, int",
                oldValue, priority);
    }


    /**
     * Method send notification to NotificationBroadcasterSupport
     *
     * @param msg           - message
     * @param attributeName - attribute name
     * @param attributeType - attribute type
     * @param oldValue      - old value
     * @param newValue      - new value
     */
    private void mySendNotification(String msg, String attributeName, String attributeType, Object oldValue, Object newValue) {
        Notification n = new AttributeChangeNotification(this,
                sequenceNumber++, System.currentTimeMillis(),
                msg, attributeName, attributeType,
                oldValue, newValue);
        System.out.println("NOTIFICATION: " + msg);
        sendNotification(n);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };

        String name = AttributeChangeNotification.class.getName();
        String description = "An attribute of this MBean has changed";
        MBeanNotificationInfo info =
                new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[]{info};
    }
}