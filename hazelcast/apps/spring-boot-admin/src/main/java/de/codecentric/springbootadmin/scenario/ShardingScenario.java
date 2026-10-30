package de.codecentric.springbootadmin.scenario;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.partition.Partition;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.values.Endpoints;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.services.ApiMediaTypeHandler;
import de.codecentric.boot.admin.server.services.EndpointDetector;
import de.codecentric.boot.admin.server.services.HealthGroupsCache;
import de.codecentric.boot.admin.server.services.InfoUpdater;
import de.codecentric.boot.admin.server.services.StatusUpdater;
import de.codecentric.boot.admin.server.services.endpoints.ChainingStrategy;
import de.codecentric.boot.admin.server.services.endpoints.EndpointDetectionStrategy;
import de.codecentric.boot.admin.server.services.endpoints.ProbeEndpointsStrategy;
import de.codecentric.boot.admin.server.services.endpoints.QueryIndexEndpointStrategy;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * Prototype for issue 5743: split the polling work across the cluster by Hazelcast partition
 * ownership. Every node keeps scheduling all instances, but only the owner of the partition of an
 * instance id actually calls the instance. Results are shared via the Hazelcast event store, so
 * this only makes sense with {@code playground.event-store=hazelcast}.
 *
 * <p>The updaters are wrapped (and not the triggers), so the interval checks keep tracking every
 * instance. When a node leaves, partition ownership moves and another node takes over on its next
 * tick. Active with {@code playground.sharding.enabled=true}.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "playground.sharding.enabled", havingValue = "true")
public class ShardingScenario {

  private static final Logger log = LoggerFactory.getLogger(ShardingScenario.class);

  @Bean
  public PartitionOwnership partitionOwnership(HazelcastInstance hazelcastInstance) {
    log.info("Scenario: polling is sharded by Hazelcast partition ownership (issue 5743)");
    return new PartitionOwnership(hazelcastInstance);
  }

  @Bean
  public StatusUpdater statusUpdater(InstanceRepository repository, InstanceWebClient.Builder webClientBuilder,
      HealthGroupsCache healthGroupsCache, AdminServerProperties adminServerProperties,
      PartitionOwnership ownership) {
    var monitor = adminServerProperties.getMonitor();
    StatusUpdater updater = new StatusUpdater(repository, webClientBuilder.build(), new ApiMediaTypeHandler(),
        monitor.getStatusChangeDetectionStrategy().asPredicate(), healthGroupsCache) {
      @Override
      protected Mono<Instance> doUpdateStatus(Instance instance) {
        return ownership.isLocal(instance.getId()) ? super.doUpdateStatus(instance) : Mono.empty();
      }
    };
    Duration timeout = monitor.getDefaultTimeout();
    Duration interval = monitor.getStatusInterval();
    updater.timeout(timeout.compareTo(interval) > 0 ? interval : timeout);
    return updater;
  }

  @Bean
  public InfoUpdater infoUpdater(InstanceRepository repository, InstanceWebClient.Builder webClientBuilder,
      PartitionOwnership ownership) {
    return new InfoUpdater(repository, webClientBuilder.build(), new ApiMediaTypeHandler()) {
      @Override
      protected Mono<Instance> doUpdateInfo(Instance instance) {
        return ownership.isLocal(instance.getId()) ? super.doUpdateInfo(instance) : Mono.empty();
      }
    };
  }

  @Bean
  public EndpointDetector endpointDetector(InstanceRepository repository, InstanceWebClient.Builder webClientBuilder,
      AdminServerProperties adminServerProperties, PartitionOwnership ownership) {
    InstanceWebClient webClient = webClientBuilder.build();
    EndpointDetectionStrategy delegate = new ChainingStrategy(
        new QueryIndexEndpointStrategy(webClient, new ApiMediaTypeHandler()),
        new ProbeEndpointsStrategy(webClient, adminServerProperties.getProbedEndpoints()));
    EndpointDetectionStrategy sharded = instance -> ownership.isLocal(instance.getId())
        ? delegate.detectEndpoints(instance)
        : Mono.<Endpoints>empty();
    return new EndpointDetector(repository, sharded);
  }

  public static class PartitionOwnership {

    private final HazelcastInstance hazelcastInstance;

    PartitionOwnership(HazelcastInstance hazelcastInstance) {
      this.hazelcastInstance = hazelcastInstance;
    }

    public boolean isLocal(InstanceId instanceId) {
      Partition partition = hazelcastInstance.getPartitionService().getPartition(instanceId.getValue());
      boolean local = partition.getOwner() != null && partition.getOwner().localMember();
      log.debug("Instance {} -> partition {} -> local={}", instanceId, partition.getPartitionId(), local);
      return local;
    }
  }
}
