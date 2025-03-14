package de.codecentric.hellodata;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Cache {

  @Cacheable("uuid")
  public UUID getUuid() {
    var uuid = UUID.randomUUID();
    log.info("New UUID: {}", uuid);
    return uuid;
  }
}
