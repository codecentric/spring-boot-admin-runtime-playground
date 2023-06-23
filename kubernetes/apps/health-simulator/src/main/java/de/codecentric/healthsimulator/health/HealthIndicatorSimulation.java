package de.codecentric.healthsimulator.health;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class HealthIndicatorSimulation implements HealthIndicator {

  private Status status = Status.UP;

  @Override
  public Health health() {
    return Health.status(status).withDetail("Simulated", "health status").build();
  }
}
