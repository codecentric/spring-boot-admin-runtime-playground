package de.codecentric.springbootadmin.cluster;

import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import de.codecentric.springbootadmin.PlaygroundProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Adds the node name to every request SBA sends to a monitored instance, so the instances can
 * report which SBA node polled them (see health-simulator {@code /pollers}).
 */
@Configuration(proxyBeanMethods = false)
public class NodeHeaderConfiguration {

  public static final String NODE_HEADER = "X-SBA-Node";

  @Bean
  public HttpHeadersProvider nodeHeaderProvider(PlaygroundProperties properties) {
    return instance -> {
      HttpHeaders headers = new HttpHeaders();
      headers.add(NODE_HEADER, properties.nodeName());
      return headers;
    };
  }
}
