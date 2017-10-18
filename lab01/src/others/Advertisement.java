package others;

import java.time.Duration;

public class Advertisement {
    private int orderId;
    private String advertText;
    private Duration displayPeriod;

    public Advertisement(int orderId, String advertText, Duration displayPeriod) {
        this.orderId = orderId;
        this.advertText = advertText;
        this.displayPeriod = displayPeriod;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
}