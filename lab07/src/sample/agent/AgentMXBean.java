package sample.agent;

import java.util.List;

public interface AgentMXBean {
    public List<MyCategory> getCategories();
    public void setCategories(List<MyCategory> categories);

    public int getPriority(String nameOfCategory);
    public void setPriority(String nameOfCategory, int priority);
}
