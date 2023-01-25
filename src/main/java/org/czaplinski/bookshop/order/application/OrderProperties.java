package org.czaplinski.bookshop.order.application;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

@Value
@ConfigurationProperties("app.orders")
public class OrderProperties {
    Duration paymentPeriod;
    String abandonCron;
}
