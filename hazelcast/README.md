## Spring Boot Admin - Hazelcast Example

Runs a cluster of three Spring Boot Admin nodes connected via Hazelcast, behind an nginx load balancer, plus two
health simulator apps that register themselves (SBA client) through the load balancer. Mailpit receives the mail
notifications, so you can see which node sent which notification.

This setup is meant to reproduce Hazelcast related issues and to try out changes from open PRs:

| Topic | Scenario |
|---|---|
| [#2795](https://github.com/codecentric/spring-boot-admin/issues/2795) Old instances not removed after redeploy | `default`, `event-store-ttl` + `./redeploy.sh` |
| [#4773](https://github.com/codecentric/spring-boot-admin/issues/4773) Selective Hazelcast support (workaround) | `in-memory-event-store` |
| [#5739](https://github.com/codecentric/spring-boot-admin/pull/5739) Selective Hazelcast caches via properties | `selective-caches`, `sent-notifications-disabled`, `hazelcast-disabled`, `aot` (need PR build) |
| [#5726](https://github.com/codecentric/spring-boot-admin/pull/5726) Hazelcast notification trigger with property notifiers | `default` vs. `notifier-bean` |
| [#5743](https://github.com/codecentric/spring-boot-admin/issues/5743) Splitting the work across nodes | `sharding` |
| [#5266](https://github.com/codecentric/spring-boot-admin/pull/5266) Server side actuator response cache | `endpoint-cache` (needs PR build) |

```
                    ┌──────────── Hazelcast cluster (TCP/IP, port 5701) ────────────┐
 browser ──► nginx  │  sba-1 :8091/:8191   sba-2 :8092/:8192   sba-3 :8093/:8193     │ ──► mailpit :8026
 :8080   round robin└────────────────────────────────────────────────────────────────┘
               ▲                      │ every node polls every instance (X-SBA-Node header)
   register    │                      ▼
        health-simulator-1 :8281   health-simulator-2 :8282
```

## Prerequisites

- Java
- Maven
- Docker and Docker Compose

## Run Everything

```bash
chmod u+x *.sh apps/*/build.sh
./buildAndRunAll.sh            # builds apps + images, starts the "default" scenario
./run.sh notifier-bean         # switch scenario (recreates containers, no rebuild)
./inspect.sh                   # wiring per node, instances per node, pollers per app, mail count
```

| URL | What |
|---|---|
| http://localhost:8080/ | SBA UI via load balancer (round robin, response header `X-SBA-Upstream` shows the node) |
| http://localhost:8091/, :8092, :8093 | SBA UI of a single node |
| http://localhost:8191/actuator/playground (8192, 8193) | Wiring of the node: cluster members, event store, notification trigger, notifiers, Hazelcast maps |
| http://localhost:8026/ | Mailpit, sender name shows the node (`SBA sba-2`) |
| http://localhost:8281/down, /up, /unknown, /out_of_service | Change the health of health-simulator-1 (8282 for -2) |
| http://localhost:8281/pollers | Requests per SBA node and path, `DELETE` resets the counters |
| http://localhost:8180/ | Hazelcast Management Center, only with `docker compose --profile tools up -d` |

### Testing a different SBA version / a PR

```bash
# in your spring-boot-admin checkout, on the PR branch
./mvnw install -DskipTests -Drevision=4.1.5-pr5739-SNAPSHOT -pl spring-boot-admin-starter-server -am
# here
SBA_VERSION=4.1.5-pr5739-SNAPSHOT ./buildAndRunAll.sh selective-caches
```

## Stop Everything

```bash
docker compose down -v
```

## Scenarios

Scenarios are env files in [scenarios](scenarios). They set the `playground.*` switches of the SBA app (see
[application.yml](apps/spring-boot-admin/src/main/resources/application.yml)) and SBA properties.
The code for each switch is in
[scenario](apps/spring-boot-admin/src/main/java/de/codecentric/springbootadmin/scenario).

### `default`: SBA defaults

Hazelcast event store and sent-notifications map, mail notifier configured via `spring.boot.admin.notify.mail.*`.
The Hazelcast config follows the example from the [clustering docs](https://docs.spring-boot-admin.com/current/docs/server/Clustering/).

### PR #5726: notification trigger with property notifiers

```bash
./run.sh default
curl -X DELETE localhost:8026/api/v1/messages; curl localhost:8281/down
```
Up to 4.1.4 you get **three mails** (one per node). `/actuator/playground` shows `NotificationTrigger` instead of
`HazelcastNotificationTrigger`, because `AdminServerHazelcastAutoConfiguration` checks
`@ConditionalOnBean(Notifier.class)` before the property notifiers exist.

```bash
./run.sh notifier-bean
```
Same notifier, but declared as user `@Bean` ([MailNotifierBeanScenario](apps/spring-boot-admin/src/main/java/de/codecentric/springbootadmin/scenario/MailNotifierBeanScenario.java)):
`HazelcastNotificationTrigger`, **one mail**. With the PR build both scenarios should send one mail.

Not covered yet: `AbstractStatusChangeNotifier` keeps the last status per node (#5716). It is not shared, so nodes can
still disagree on `from` status, for example after a node restart.

### Issue #2795: old instances stay after redeploy

```bash
./run.sh default
./redeploy.sh          # graceful: old instance deregisters through the load balancer
./redeploy.sh --kill   # hard kill: no deregistration
./inspect.sh
```
`redeploy.sh` replaces health-simulator-1 with a new container under a new hostname, like a new pod or task. The
instance id is derived from the health URL, so the new instance gets a new id. After `--kill` the old instance stays
registered as `OFFLINE` on all nodes, because self-registered instances are never removed automatically.
`./run.sh event-store-ttl` uses the 30s TTL from the issue on both maps. Then events expire while the instance still
exists, and the state on the nodes drifts apart.

### Issue #4773 / PR #5739: selective Hazelcast caches

```bash
./run.sh in-memory-event-store
```
Workaround from the issue: a local `InMemoryEventStore` bean, notifications still go through Hazelcast
([InMemoryEventStoreScenario](apps/spring-boot-admin/src/main/java/de/codecentric/springbootadmin/scenario/InMemoryEventStoreScenario.java)).

Things to look at:
- With self-registration through a load balancer, each registration reaches only **one** node. Until the client has
  re-registered on every node (period 10s, round robin), the nodes show different instances (`./inspect.sh`).
  With discovery (Eureka, Kubernetes, ...) every node discovers the instances on its own.
- Deduplication of notifications then relies on the event versions being the same on all nodes. With local event
  stores the versions are counted per node, so `HazelcastNotificationTrigger` can drop or duplicate notifications.
  Use `notifier-bean` together with this scenario to test it
  (`docker compose --env-file scenarios/in-memory-event-store.env --env-file scenarios/notifier-bean.env up -d --force-recreate`).

`./run.sh selective-caches` sets the properties from PR #5739 (`spring.boot.admin.hazelcast.*-enabled`). It needs a
build that contains the PR. Older versions ignore the properties.

#### Results with the PR build (head `cf35dd37b`)

```bash
# in the spring-boot-admin checkout, on the PR branch; own version so 4.1.4-SNAPSHOT in ~/.m2 is not overwritten
./mvnw install -DskipTests -Drevision=4.1.5-pr5739-SNAPSHOT -pl spring-boot-admin-starter-server -am
# here
SBA_VERSION=4.1.5-pr5739-SNAPSHOT ./buildAndRunAll.sh selective-caches
```

| Scenario | Event store | Notification trigger | Hazelcast maps | Mails per status change |
|---|---|---|---|---|
| `default` | Hazelcast | `NotificationTrigger` | event-store | 3 (bug from #5726, not fixed by this PR) |
| `selective-caches` | **InMemory** | `NotificationTrigger` | none | — |
| `hazelcast-disabled` | InMemory | `NotificationTrigger` | none | — |
| `notifier-bean` | — | — | — | **context fails to start** (see below) |
| `notifier-filtering-bean` | Hazelcast | `HazelcastNotificationTrigger` | event-store, sent-notifications | 1 |
| `sent-notifications-disabled` | Hazelcast | **`NotificationTrigger`** | event-store | 3 |
| `selective-caches` + `notifier-filtering-bean` | **InMemory** | `HazelcastNotificationTrigger` | sent-notifications | 1 |
| `aot` + `selective-caches` | **InMemory** | `NotificationTrigger` | none | — |

Combine scenarios with several env files:
`docker compose --env-file scenarios/selective-caches.env --env-file scenarios/notifier-filtering-bean.env up -d --force-recreate`.

Findings:
- The flags work at runtime, **also with AOT** (`aot` + `selective-caches`, image built without the property set).
  The implementation is picked inside the factory methods, as asked in the review.
- `notifier-bean` (a single user `MailNotifier` bean) does not start:
  `filteringNotifier -> compositeNotifier -> filteringNotifier` circular reference. This comes from #5737
  (auto-created `FilteringNotifier`), which is on master and fixed by #5726. It also happens with
  `notification-filters-enabled=false`, since the `FilteringNotifier` bean is still created.
  `notifier-filtering-bean` uses a user defined `@Primary FilteringNotifier`, which avoids the cycle
  ([FilteringNotifierBeanScenario](apps/spring-boot-admin/src/main/java/de/codecentric/springbootadmin/scenario/FilteringNotifierBeanScenario.java)).
- With property notifiers the `*-enabled` flags for notifications have no effect, because the Hazelcast notifier
  configuration does not apply at all (#5726).

#### Results with #5739 rebased on master including #5726 (`921fedef1`)

Local rebase of the PR onto `c2ecfa470` (the squashed PR commit, `pr-5739-rebased` in the spring-boot-admin checkout).
The flags are applied in the `sentNotificationsMap` (`null` if disabled -> plain trigger) and
`notificationFiltersMap` (local map if disabled) beans. The PR's own tests, adapted to #5726, pass (21 tests).

| Scenario | Event store | Notification trigger | Hazelcast maps | Mails |
|---|---|---|---|---|
| `default` | Hazelcast | `HazelcastNotificationTrigger` | event-store, sent-notifications | 1 |
| `notifier-bean` | Hazelcast | `HazelcastNotificationTrigger` | event-store, sent-notifications | 1 (starts, no cycle) |
| `selective-caches` (property notifier) | **InMemory** | `HazelcastNotificationTrigger` | sent-notifications | **1** |
| `selective-caches` + `notifier-bean` | **InMemory** | `HazelcastNotificationTrigger` | sent-notifications | 1 |
| `aot` + `selective-caches` | **InMemory** | `HazelcastNotificationTrigger` | sent-notifications | 1 |
| `sent-notifications-disabled` | Hazelcast | `NotificationTrigger` | event-store | 3 (every node notifies, as documented) |
| `hazelcast-disabled` | InMemory | `NotificationTrigger` | none | one per node (4 seen, one node sent twice, not investigated) |

`selective-caches` is now the setup requested in #4773: local events, clustered notification deduplication, also with
property notifiers.

#### Results with the PR #5726 build (head `4c5b3eed6`)

Built the same way (`-Drevision=4.1.5-pr5726-SNAPSHOT`, from a separate worktree).

| Scenario | Notification trigger | Mails per status change |
|---|---|---|
| `default` (property notifier) | `HazelcastNotificationTrigger` | 1 (3 on 4.1.4) |
| `notifier-bean` | `HazelcastNotificationTrigger` | 1 (context failed to start before) |
| `notifier-filtering-bean` | `HazelcastNotificationTrigger` | 1 |
| `aot` | `HazelcastNotificationTrigger` | 1 |
| `default`, sba-1 restarted before the change | `HazelcastNotificationTrigger` | 1 |

The PR's own tests pass (`AdminServerHazelcastAutoConfigurationTest`, `AdminServerAutoConfigurationTest`,
`HazelcastNotificationTriggerTest`, `AdminApplicationHazelcastTest`: 17 tests).
Merging #5739 (`cf35dd37b`) on top of #5726 conflicts in `AdminServerHazelcastAutoConfiguration`, so #5739 needs a rebase.

**AOT:** the review on #5739 says `@ConditionalOnProperty` is evaluated at build time when AOT is used. The images run
**without** AOT by default, so the playground switches work at runtime. `./run.sh aot` enables AOT, and then only the
values present at `mvn package` time apply. To test this, build with the property set, for example
`(cd apps/spring-boot-admin && SPRING_BOOT_ADMIN_HAZELCAST_EVENT_STORE_ENABLED=false ./build.sh)`, and then switch it at runtime.

### Issue #5743: splitting the work across nodes

```bash
./run.sh sharding
curl -X DELETE localhost:8281/pollers; sleep 30; curl localhost:8281/pollers
```
By default every node polls every instance (`/pollers` lists sba-1, sba-2 and sba-3). In the prototype
([ShardingScenario](apps/spring-boot-admin/src/main/java/de/codecentric/springbootadmin/scenario/ShardingScenario.java))
only the node that owns the Hazelcast partition of the instance id calls the instance. Results are shared through
the Hazelcast event store.

Unlike the proposal in the issue, it wraps the updaters (`StatusUpdater`, `InfoUpdater`, `EndpointDetector`) and not
the triggers or the registry. Every node keeps all instances in its interval checks, so another node takes over on its
next tick when a node leaves: `docker stop sba-1`, wait, and check `/pollers`. Open points to look at: what happens
during partition migration (a short window with two pollers or none), and the notification trigger, which still runs
on all nodes.

With few instances the distribution is uneven (271 partitions, few keys), so both simulators can end up on one node.

### PR #5266: server side response cache

The other scenarios set `spring.boot.admin.endpoint-cache.enabled=false` (the PR enables it by default), so a PR build behaves like before. `./run.sh endpoint-cache` sets it to `true`. It needs a build that contains the PR.
Open "Mappings" or "Beans" in the UI via the load balancer and check `/pollers`: with the Hazelcast backed cache only
one node should fetch `/actuator/mappings` per TTL. To see the stale data concerns from the review, restart a
simulator (`./redeploy.sh`) or change its config, and compare the UI with the actuator response.

## Notes

- The node name is sent as `X-SBA-Node` header on every request to the instances (`HttpHeadersProvider`) and used as
  mail sender name and UI title.
- Hazelcast join is TCP/IP (`HAZELCAST_MEMBERS`). Multicast is used if it is empty, e.g. when running locally.
- Logging: `de.codecentric.boot.admin.server.notify` is on DEBUG, so you see "Notifications already sent" in the logs
  (`docker compose logs -f sba-1 sba-2 sba-3`).
