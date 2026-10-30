package de.codecentric.springbootadmin.cluster;

import static de.codecentric.boot.admin.server.config.AdminServerHazelcastAutoConfiguration.DEFAULT_NAME_EVENT_STORE_MAP;
import static de.codecentric.boot.admin.server.config.AdminServerHazelcastAutoConfiguration.DEFAULT_NAME_SENT_NOTIFICATIONS_MAP;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.MergePolicyConfig;
import com.hazelcast.spi.merge.PutIfAbsentMergePolicy;
import de.codecentric.springbootadmin.PlaygroundProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hazelcast member configuration based on the example from the Spring Boot Admin clustering docs,
 * with TCP/IP join (deterministic in Docker) and configurable TTLs (see issue 2795).
 */
@Configuration(proxyBeanMethods = false)
public class HazelcastConfiguration {

  @Bean
  public Config hazelcastConfig(PlaygroundProperties properties) {
    var hazelcast = properties.hazelcast();

    MapConfig eventStoreMap = new MapConfig(DEFAULT_NAME_EVENT_STORE_MAP)
        .setInMemoryFormat(InMemoryFormat.OBJECT)
        .setBackupCount(1)
        .setTimeToLiveSeconds(hazelcast.eventStoreTtlSeconds())
        .setMergePolicyConfig(new MergePolicyConfig(PutIfAbsentMergePolicy.class.getName(), 100));

    MapConfig sentNotificationsMap = new MapConfig(DEFAULT_NAME_SENT_NOTIFICATIONS_MAP)
        .setInMemoryFormat(InMemoryFormat.OBJECT)
        .setBackupCount(1)
        .setTimeToLiveSeconds(hazelcast.sentNotificationsTtlSeconds())
        .setEvictionConfig(new EvictionConfig()
            .setEvictionPolicy(EvictionPolicy.LRU)
            .setMaxSizePolicy(MaxSizePolicy.PER_NODE))
        .setMergePolicyConfig(new MergePolicyConfig(PutIfAbsentMergePolicy.class.getName(), 100));

    Config config = new Config();
    config.setInstanceName(properties.nodeName());
    config.setClusterName(hazelcast.clusterName());
    config.addMapConfig(eventStoreMap);
    config.addMapConfig(sentNotificationsMap);
    config.setProperty("hazelcast.jmx", "true");
    config.setProperty("hazelcast.logging.type", "slf4j");

    JoinConfig join = config.getNetworkConfig().setPort(5701).setPortAutoIncrement(true).getJoin();
    join.getAutoDetectionConfig().setEnabled(false);
    if (hazelcast.members().isEmpty()) {
      join.getMulticastConfig().setEnabled(true);
    } else {
      join.getMulticastConfig().setEnabled(false);
      join.getTcpIpConfig().setEnabled(true).setMembers(hazelcast.members());
    }
    return config;
  }
}
