package others;

import client.IClient;

import java.io.Serializable;
import java.time.Duration; // available since JDK 1.8

public class Order implements Serializable{
    private String advertText;
    private Duration displayPeriod;
    private IClient client;

    public Order(String advertText, Duration displayPeriod, IClient client) {
        this.advertText = advertText;
        this.displayPeriod = displayPeriod;
        this.client = client;
    }

    public String getAdvertText() {
        return advertText;
    }

    public void setAdvertText(String advertText) {
        this.advertText = advertText;
    }

    public Duration getDisplayPeriod() {
        return displayPeriod;
    }

    public void setDisplayPeriod(Duration displayPeriod) {
        this.displayPeriod = displayPeriod;
    }

    public IClient getClient() {
        return client;
    }

    public void setClient(IClient client) {
        this.client = client;
    }
}
