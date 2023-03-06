# testcontainers-scala-singleton-demo

[Singleton containers][1] is a pattern when using [Testcontainers][2] to start a container once for several test
classes.  
This repo contains a minimal example how to implement the original Java pattern in Scala
using [Testcontainers-scala][3].

## Usage

The example is contained in [SingletonContainerSuite](src/test/scala/SingletonContainerSuite.scala).  
If you like, have two terminals side by side. Run

- `watch -n 1 docker ps` in the first terminal and
- `sbt test` in the second terminal

You should see a [Redis][4] container spawned, which will last for the whole test suite duration (containing two test
classes).

For the sake of this example and as a proof we are using with the same container for both test classes, the first test
class writes a value to Redis, while the second test class reads that value from Redis.

## Comparison

The original pattern in Java uses an abstract class with static fields:

```java
abstract class AbstractContainerBaseTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer();
        MY_SQL_CONTAINER.start();
    }
}
```

The same behavior can be achieved in Scala with a companion object:

````scala
object AbstractContainerBaseSpec {
  private val containerDef =
    GenericContainer.Def(
      "redis:5.0.3-alpine",
      exposedPorts = Seq(6379),
    ).start()
}

abstract class AbstractContainerBaseSpec {}
````

[1]: https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers

[2]: https://www.testcontainers.org/

[3]: https://github.com/testcontainers/testcontainers-scala

[4]: https://redis.io/