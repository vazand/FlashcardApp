package vazand.github.io.controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._
import models.Person
import models.MongoDbExample
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.json.Json
import scala.util.parsing.json.JSONType
import scala.util.parsing.json.JSONObject
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import play.api.libs.json.JsArray

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents,
    mongoDbEx: MongoDbExample
) extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method will be
    * called when the application receives a `GET` request with a path of `/`.
    */

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  /*example for /insert
  {
  "_id":"655b4eb1aba2b8320e84dfef",
  "firstName":"Vazand",
  "lastName":"vel"
  }
   */
  def createData() = Action(parse.json) { request =>
    val receivedJsonAsPerson = request.body.as[Person]
    val insertOnePerson =
      mongoDbEx.collectionPerson.insertOne(receivedJsonAsPerson).toFuture()
    Ok(insertOnePerson.toString())
  }

  def findOne() = Action { implicit request: Request[AnyContent] =>
    // val person: Person = Person("vazand1", "kuzhandaivel")
    val getThePerson =
      mongoDbEx.collectionPerson.find().first().toFuture()
    val result = Await.result(getThePerson, Duration.Inf)
    val asJson = Json.toJson(result)
    Ok(asJson).as("application/json")
  }

  /*
  {
  "_id":"655b4eb1aba2b8320e84dfef",
  "firstName":"",
  "lastName":""
  }
   */
  def deleteOne() = Action(parse.json) { implicit request =>
    // val person: Person = Person("vazand1", "kuzhandaivel")
    val bodyJsonAsPerson = request.body.as[Person]

    val filter = equal("_id", bodyJsonAsPerson._id)

    val deleteThePerson =
      mongoDbEx.collectionPerson.deleteOne(filter).toFuture()
    val delResult = Await.result(deleteThePerson, Duration.Inf)
    Ok(delResult.toString()).as("text/plain")
  }

  /*
  {
  "_id":"655b4eb1aba2b8320e84dfef",
  "firstName":"aNewName",
  "lastName":"Hahaha"
  }
   */
  def updateOne() = Action(parse.json) { implicit request =>
    // val person: Person = Person("vazand1", "kuzhandaivel")
    val bodyJsonAsPerson = request.body.as[Person]

    // here we're just using the id to update the firstname and last name
    val filter = equal("_id", bodyJsonAsPerson._id)
    val updateData1 = set("firstName", bodyJsonAsPerson.firstName)
    val updateData2 = set("lastName", bodyJsonAsPerson.lastName)
    val updateSet = combine(updateData1, updateData2)

    val updateThePerson =
      mongoDbEx.collectionPerson.updateOne(filter, updateSet).toFuture()
    val updateRes = Await.result(updateThePerson, Duration.Inf)
    Ok(updateRes.toString()).as("text/plain")
  }

   def findAll() = Action { implicit request: Request[AnyContent] =>
    val getThePerson = mongoDbEx.collectionPerson.find().toFuture()
    val result = Await.result(getThePerson, Duration.Inf)
    val asJsonValues = result.map( x => Json.toJson(x))
    val asJsArray = JsArray(asJsonValues)
    Ok(asJsArray).as("application/json")
  }

}
