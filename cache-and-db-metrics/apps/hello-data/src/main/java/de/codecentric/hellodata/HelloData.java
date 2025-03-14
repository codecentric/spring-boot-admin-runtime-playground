package de.codecentric.hellodata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloData {

  private final Cache cache;
  private final JdbcTemplate jdbcTemplate;
  private final ExecutorService executorService = Executors.newFixedThreadPool(15);

  @GetMapping("/trigger-cache")
  public String triggerCache() {
    var uuid = cache.getUuid();
    log.info("Hello {}", uuid);
    return uuid.toString();
  }

  @GetMapping("/trigger-db")
  public void triggerDb() {
    for (int i = 0; i < 10_000; i++) {
      final int finalI = i;
      executorService.submit(() -> {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test" + finalI + " (id INT PRIMARY KEY)");
        jdbcTemplate.execute("INSERT INTO test" + finalI + " VALUES (" + finalI + ")");
      });
    }
  }
}
