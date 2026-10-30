package de.codecentric.springbootadmin.scenario;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.eventstore.InMemoryEventStore;
import de.codecentric.boot.admin.server.eventstore.InstanceEventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Workaround from issue 4773: keep the event store local while notifications are still
 * deduplicated through Hazelcast. Active with {@code playground.event-store=in-memory}.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "playground.event-store", havingValue = "in-memory")
public class InMemoryEventStoreScenario {

  private static final Logger log = LoggerFactory.getLogger(InMemoryEventStoreScenario.class);

  @Bean
  public InstanceEventStore eventStore(AdminServerProperties adminServerProperties) {
    log.info("Scenario: using local InMemoryEventStore instead of HazelcastEventStore (issue 4773)");
    var eventStore = adminServerProperties.getEventStore();
    return new InMemoryEventStore(eventStore.getMaxLogSizePerAggregate(), eventStore.isPruneInfoUpdatedEvents());
  }
}
