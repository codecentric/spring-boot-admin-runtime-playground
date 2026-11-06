package de.codecentric.springbootadmin.scenario;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.MailNotifier;
import de.codecentric.boot.admin.server.notify.filter.FilteringNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

/**
 * Mail notifier as user {@code @Bean}, wrapped in a user defined {@code @Primary FilteringNotifier}.
 * Active with {@code playground.notifier=filtering-bean}.
 *
 * <p>Avoids the circular reference {@code filteringNotifier -> compositeNotifier -> filteringNotifier}
 * that the auto-configured {@code FilteringNotifier} (PR 5737) causes with a single user notifier bean:
 * the delegate is injected by name, and because a {@code @Primary} notifier exists no
 * {@code CompositeNotifier} is created. The filters are local to the node.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "playground.notifier", havingValue = "filtering-bean")
public class FilteringNotifierBeanScenario {

  private static final Logger log = LoggerFactory.getLogger(FilteringNotifierBeanScenario.class);

  @Bean
  @ConfigurationProperties("spring.boot.admin.notify.mail")
  public MailNotifier mailNotifier(JavaMailSender mailSender, InstanceRepository repository,
      @Qualifier("mailNotifierTemplateEngine") TemplateEngine mailNotifierTemplateEngine) {
    return new MailNotifier(mailSender, repository, mailNotifierTemplateEngine);
  }

  @Bean
  @Primary
  public FilteringNotifier filteringNotifier(@Qualifier("mailNotifier") MailNotifier delegate,
      InstanceRepository repository) {
    log.info("Scenario: MailNotifier @Bean wrapped in user defined @Primary FilteringNotifier");
    return new FilteringNotifier(delegate, repository);
  }
}
