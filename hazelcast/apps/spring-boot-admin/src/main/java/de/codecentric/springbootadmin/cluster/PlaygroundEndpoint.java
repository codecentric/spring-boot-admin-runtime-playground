package de.codecentric.springbootadmin.cluster;

import com.hazelcast.core.HazelcastInstance;
import de.codecentric.boot.admin.server.eventstore.InstanceEventStore;
import de.codecentric.boot.admin.server.notify.NotificationTrigger;
import de.codecentric.boot.admin.server.notify.Notifier;
import de.codecentric.springbootadmin.PlaygroundProperties;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * {@code /actuator/playground}: shows how this node is wired, to verify a scenario quickly.
 */
@Component
@Endpoint(id = "playground")
public class PlaygroundEndpoint {

  private final PlaygroundProperties properties;
  private final HazelcastInstance hazelcastInstance;
  private final ObjectProvider<InstanceEventStore> eventStore;
  private final ObjectProvider<NotificationTrigger> notificationTrigger;
  private final ObjectProvider<Notifier> notifiers;

  public PlaygroundEndpoint(PlaygroundProperties properties, HazelcastInstance hazelcastInstance,
      ObjectProvider<InstanceEventStore> eventStore, ObjectProvider<NotificationTrigger> notificationTrigger,
      ObjectProvider<Notifier> notifiers) {
    this.properties = properties;
    this.hazelcastInstance = hazelcastInstance;
    this.eventStore = eventStore;
    this.notificationTrigger = notificationTrigger;
    this.notifiers = notifiers;
  }

  @ReadOperation
  public Map<String, Object> info() {
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("node", properties.nodeName());
    result.put("scenario", properties);
    result.put("clusterMembers", hazelcastInstance.getCluster().getMembers().stream()
        .map(m -> m.getAddress() + (m.localMember() ? " (this)" : ""))
        .toList());
    result.put("eventStore", className(eventStore.getIfAvailable()));
    result.put("notificationTrigger", className(notificationTrigger.getIfAvailable()));
    result.put("notifiers", notifiers.orderedStream().map(PlaygroundEndpoint::className).toList());
    Map<String, Integer> maps = new TreeMap<>();
    hazelcastInstance.getDistributedObjects().stream()
        .filter(o -> "hz:impl:mapService".equals(o.getServiceName()))
        .forEach(o -> maps.put(o.getName(), hazelcastInstance.getMap(o.getName()).size()));
    result.put("maps", maps);
    return result;
  }

  private static String className(Object bean) {
    return bean == null ? "<none>" : bean.getClass().getName();
  }
}
