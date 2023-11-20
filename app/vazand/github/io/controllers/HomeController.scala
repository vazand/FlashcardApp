package vazand.github.io.controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents,
    mongoInj: MongoDbInjector,
    mongoDbEx: MongoDbExample
) extends BaseController {

  /** Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method will be
    * called when the application receives a `GET` request with a path of `/`.
    */

  def index() = Action { implicit request: Request[AnyContent] =>
    //println(mongoInj.insertResultFuture.toString)
    //println(insertOnebePerson.toString)
    Ok(views.html.index())
  }

  def insertOne() = Action { implicit request: Request[AnyContent] =>
    val person: Person = Person("Ada", "Lovelace")
    //val insertOnePerson = mongoDbEx.collectionPerson.insertOne(person)
    //println(insertOnebePerson.toString)
    val result = mongoDbEx.insertOnePerson.collect()
    Ok(result.toString())
  }

}
