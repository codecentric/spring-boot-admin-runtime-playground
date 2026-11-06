package de.codecentric.healthsimulator.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.health.contributor.Status;
import org.springframework.stereotype.Component;

@Component
public class HealthIndicatorSimulation implements HealthIndicator {

  private volatile Status status = Status.UP;

  @Override
  public Health health() {
    return Health.status(status).withDetail("Simulated", "health status").build();
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
