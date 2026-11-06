package de.codecentric.healthsimulator.poll;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Counts incoming requests per Spring Boot Admin node (header {@code X-SBA-Node}) and path. Used
 * to see whether every SBA node polls this instance (default) or only one (sharding, issue 5743).
 */
@Component
public class PollerTracker extends OncePerRequestFilter {

  public static final String NODE_HEADER = "X-SBA-Node";

  private final Map<String, Poller> pollers = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String node = request.getHeader(NODE_HEADER);
    if (node != null) {
      pollers.computeIfAbsent(node, n -> new Poller()).record(request.getRequestURI());
    }
    chain.doFilter(request, response);
  }

  public Map<String, Object> snapshot() {
    Map<String, Object> result = new TreeMap<>();
    pollers.forEach((node, poller) -> result.put(node, poller.snapshot()));
    return result;
  }

  public void reset() {
    pollers.clear();
  }

  static class Poller {

    private final Map<String, AtomicLong> paths = new ConcurrentHashMap<>();
    private volatile Instant lastSeen;

    void record(String path) {
      paths.computeIfAbsent(path, p -> new AtomicLong()).incrementAndGet();
      lastSeen = Instant.now();
    }

    Map<String, Object> snapshot() {
      Map<String, Object> result = new TreeMap<>();
      result.put("lastSeen", lastSeen);
      Map<String, Long> counts = new TreeMap<>();
      paths.forEach((path, count) -> counts.put(path, count.get()));
      result.put("requests", counts);
      return result;
    }
  }
}
