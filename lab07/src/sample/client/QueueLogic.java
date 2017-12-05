package sample.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueLogic {

    private List<Ticket> queue;
    private Map<String, Integer> countersMap;

    public QueueLogic() {
        queue = new ArrayList<>();
        countersMap = new HashMap<>();
    }

    public Ticket getTicket(String nameOfCategory, int priority) {
        int index = queue.size();
        for (Ticket ticket : queue) if (ticket.getPriority() < priority) index--;

        int newCounter;
        if(countersMap.get(nameOfCategory) != null){
            int counter = countersMap.get(nameOfCategory);
            newCounter = counter + 1;
            countersMap.put(nameOfCategory, newCounter);
        } else {
            newCounter = 1;
            countersMap.put(nameOfCategory, newCounter);
        }

        Ticket ticket = new Ticket(nameOfCategory, newCounter, priority);
        queue.add(index, ticket);
        return ticket;
    }

    public boolean returnTicket() {
        if (queue.size() > 0) {
            queue.remove(0);
            return true;
        }
        return false;
    }

    public List<Ticket> getQueue() {
        return queue;
    }

}
