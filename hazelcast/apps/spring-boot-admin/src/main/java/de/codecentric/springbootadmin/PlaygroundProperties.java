package de.codecentric.springbootadmin;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("playground")
public record PlaygroundProperties(
    @DefaultValue("sba") String nodeName,
    @DefaultValue Hazelcast hazelcast,
    @DefaultValue("hazelcast") EventStoreMode eventStore,
    @DefaultValue("properties") NotifierMode notifier,
    @DefaultValue Sharding sharding) {

  public record Hazelcast(
      @DefaultValue("spring-boot-admin") String clusterName,
      @DefaultValue List<String> members,
      @DefaultValue("0") int eventStoreTtlSeconds,
      @DefaultValue("0") int sentNotificationsTtlSeconds) {
  }

  public record Sharding(@DefaultValue("false") boolean enabled) {
  }

  public enum EventStoreMode {
    HAZELCAST, IN_MEMORY
  }

  public enum NotifierMode {
    PROPERTIES, BEAN
  }
}
