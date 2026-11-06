package de.codecentric.springbootadmin.scenario;

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.MailNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

/**
 * Declares the mail notifier as a user {@code @Bean} instead of letting SBA create it from
 * {@code spring.boot.admin.notify.mail.*}. Active with {@code playground.notifier=bean}.
 *
 * <p>Up to 4.1.4 this changes which notification trigger is created when Hazelcast is present
 * (see PR 5726): with a user bean the {@code HazelcastNotificationTrigger} is used, with the
 * property-based notifier a plain {@code NotificationTrigger} is used and every node notifies.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "playground.notifier", havingValue = "bean")
public class MailNotifierBeanScenario {

  private static final Logger log = LoggerFactory.getLogger(MailNotifierBeanScenario.class);

  @Bean
  @ConfigurationProperties("spring.boot.admin.notify.mail")
  public MailNotifier mailNotifier(JavaMailSender mailSender, InstanceRepository repository,
      @Qualifier("mailNotifierTemplateEngine") TemplateEngine mailNotifierTemplateEngine) {
    log.info("Scenario: MailNotifier declared as user @Bean (PR 5726)");
    return new MailNotifier(mailSender, repository, mailNotifierTemplateEngine);
  }
}
