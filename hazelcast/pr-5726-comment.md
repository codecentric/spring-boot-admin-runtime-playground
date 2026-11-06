Thanks @YadavKshitiz, and thanks @cdprete for the clarification on scope.

I checked the PR (head `4c5b3eed6`) against my two open points:

- The Javadoc on `sentNotificationsMap` now describes the lazily resolved `ObjectProvider`, with no wrong claim about auto-configuration ordering.
- The clustering docs now cover the circular dependency between `FilteringNotifier` and `CompositeNotifier`, with the `@Primary` + `@Qualifier` fix and a counter-example.
- The release note about the removed `NotifierTriggerConfiguration` is in the description.

I also ran it on a 3-node Hazelcast cluster behind a load balancer (setup: https://github.com/codecentric/spring-boot-admin-runtime-playground/pull/37). One status change (`UP` → `DOWN`) gave:

| Setup | 4.1.4 / master | This PR |
|---|---|---|
| Mail notifier via `spring.boot.admin.notify.mail.*` | plain `NotificationTrigger`, **3 mails** | `HazelcastNotificationTrigger`, **1 mail** |
| Single user `@Bean` notifier | master fails to start (`filteringNotifier -> compositeNotifier` cycle from #5737) | starts, 1 mail |
| User `@Bean` notifier + own `@Primary FilteringNotifier` | not tested | 1 mail |
| Property notifier with AOT enabled | not tested | 1 mail |
| Property notifier, one node restarted before the change | not tested | 1 mail |

The 4 Hazelcast-related test classes pass as well (17 tests): `AdminServerHazelcastAutoConfigurationTest`, `AdminServerAutoConfigurationTest`, `HazelcastNotificationTriggerTest`, `AdminApplicationHazelcastTest`.

That covers exactly what this PR claims: the trigger wiring and the startup regression from #5737. It does not claim to fix #5716, and I didn't test `DelayingMailNotifier` or per-node last status, so that issue stays open. The shared last-status map is a separate change.

I'm happy to merge this as is. One heads-up for follow-ups: #5739 conflicts with this in `AdminServerHazelcastAutoConfiguration`, so it will need a rebase after this lands.
