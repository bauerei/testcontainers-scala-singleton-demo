import RedisSpec.{containerDef, redisPort, someKey, someValue}
import com.dimafeng.testcontainers.GenericContainer
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{BeforeAndAfterAll, DoNotDiscover, Sequential}

class SingletonContainerSuite extends Sequential(new _1_WriteSpec, new _2_ReadSpec)

object RedisSpec {
  private val redisPort = 6379

  private val containerDef =
    GenericContainer.Def(
      "redis:5.0.3-alpine",
      exposedPorts = Seq(redisPort),
    ).start()

  val someKey = "foo"
  val someValue = "bar"
}

abstract class RedisSpec extends AnyFunSuite with BeforeAndAfterAll {
  private val client =
    RedisClient.create(String.format(s"redis://${containerDef.host}:${containerDef.mappedPort(redisPort)}"))
  private val connection: StatefulRedisConnection[String, String] =
    client.connect()

  protected val redis: RedisCommands[String, String] = connection.sync()

  override def afterAll(): Unit = {
    connection.close()
    client.shutdown()
  }
}

@DoNotDiscover
class _1_WriteSpec extends RedisSpec {
  test("Should write key") {
    redis.set(someKey, someValue)

    assert(redis.get(someKey) == someValue)
  }
}

@DoNotDiscover
class _2_ReadSpec extends RedisSpec {
  test("Should read key from _1_WriteSpec") {
    val actualValue = redis.get(someKey)

    assert(actualValue == someValue)
  }
}

