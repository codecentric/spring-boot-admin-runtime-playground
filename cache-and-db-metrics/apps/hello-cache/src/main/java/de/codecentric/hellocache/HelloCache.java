package de.codecentric.hellocache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloCache {

  private final Cache cache;

  @GetMapping
  public String hello() {
    var uuid = cache.getUuid();
    log.info("Hello {}", uuid);
    return uuid.toString();
  }
}
