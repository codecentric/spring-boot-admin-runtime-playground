package de.codecentric.springbootadmin;

import java.util.List;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discovery")
public class DiscoveryController {
  
  private final DiscoveryClient discoveryClient;
  
  public DiscoveryController(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  @GetMapping("/services")
  public List<String> services() {
    return discoveryClient.getServices();
  }

  @GetMapping("/instances/{serviceId}")
  public List<ServiceInstance> instances(@PathVariable String serviceId) {
    return discoveryClient.getInstances(serviceId);
  }

}
