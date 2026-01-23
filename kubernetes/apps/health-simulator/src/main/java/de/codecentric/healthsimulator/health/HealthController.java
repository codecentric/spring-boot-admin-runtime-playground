package de.codecentric.healthsimulator.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {

  private final HealthIndicatorSimulation healthIndicatorSimulation;

  @GetMapping("/")
  public Status status() {
    return healthIndicatorSimulation.getStatus();
  }

  @GetMapping("/up")
  public Status up() {
    healthIndicatorSimulation.setStatus(Status.UP);
    return healthIndicatorSimulation.getStatus();
  }

  @GetMapping("/down")
  public Status down() {
    healthIndicatorSimulation.setStatus(Status.DOWN);
    return healthIndicatorSimulation.getStatus();
  }

  @GetMapping("/unknown")
  public Status unknown() {
    healthIndicatorSimulation.setStatus(Status.UNKNOWN);
    return healthIndicatorSimulation.getStatus();
  }

  @GetMapping("/out_of_service")
  public Status outOfService() {
    healthIndicatorSimulation.setStatus(Status.OUT_OF_SERVICE);
    return healthIndicatorSimulation.getStatus();
  }
}
