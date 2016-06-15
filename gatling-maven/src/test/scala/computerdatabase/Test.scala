package Test

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.core.validation._

class Test extends Simulation {

  val httpConf = http
    .baseURL("http://192.168.1.188:8080") // Here is the root for all relative URLs

  val headers = Map("Content-Type" -> "application/json") // Note the headers specific to a given request

  //  1 发送手机验证码

  
  object Test1{
    def display(v: Validation[String]) = v match {
      case Success(string) => println("success: " + string)
      case Failure(error)  => println("failure: " + error)
    }
    val test1 = scenario("Merchant_getProfile")
    .exec(http("merchant1").post("/xw3/AdminService").body(StringBody("""{"jsonrpc": "2.0","method": "admin_getCerificateList","params":{"sessionId":"89afd9f5-a64b-4ba1-9b67-95375bb4c9812","pageNo":0,"pageSize":10},"id":10000}""")).asJSON.check(status.is(200),regex(""""id":[0-9]*""").findAll.saveAs("counts"))).pause(2) 
    .exec(http("merchant2").post("/xw3/AdminService").body(StringBody("""{"jsonrpc": "2.0","method": "admin_getCerificateList","params":{"sessionId":"89afd9f5-a64b-4ba1-9b67-95375bb4c9812","pageNo":0,"pageSize":10},"id":10000}""")).asJSON.check(status.is(200),regex(""""id":([0-9]*)""").find(2).saveAs("id"))).pause(2)
    .exec(http("merchant3").post("/xw3/AdminService").body(StringBody("""{"jsonrpc": "2.0","method": "admin_auditCerificate","params":{"sessionId":"def4ad01-6199-4db4-a93b-3100380e21471","id":${id},"action":1},"id":10000}""")).asJSON.check(status.is(200),regex("""error""").notExists)).pause(2) 
  }

  val test1 = scenario("test1").exec(Test1.test1)

  setUp(
    test1.inject(constantUsersPerSec(10) during (10))).protocols(httpConf)
}
