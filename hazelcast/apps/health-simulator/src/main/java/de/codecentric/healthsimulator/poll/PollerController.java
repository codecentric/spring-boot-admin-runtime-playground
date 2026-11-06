package de.codecentric.healthsimulator.poll;

import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollerController {

  private final PollerTracker pollerTracker;

  public PollerController(PollerTracker pollerTracker) {
    this.pollerTracker = pollerTracker;
  }

  @GetMapping("/pollers")
  public Map<String, Object> pollers() {
    return pollerTracker.snapshot();
  }

  @DeleteMapping("/pollers")
  public void reset() {
    pollerTracker.reset();
  }
}
