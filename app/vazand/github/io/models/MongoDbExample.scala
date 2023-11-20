package models

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._
import org.apache.pekko.stream.impl.Completed

import org.mongodb.scala.bson.ObjectId
import scala.util.Success
import scala.util.Failure
import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.libs.json.OFormat
import play.api.libs.json._
import org.bson.codecs.Codec
import org.mongodb.scala.bson.codecs.Macros
import org.bson.codecs.configuration.CodecProvider

import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

object Person {

  def apply(firstName: String, lastName: String): Person =
    Person(new ObjectId(), firstName, lastName)

  implicit val objectIdFormat: Format[ObjectId] = Format(
    Reads.of[String].map(new ObjectId(_)),
    Writes(objId => JsString(objId.toHexString))
  )
  implicit val personFormat: OFormat[Person] =
    Json.using[Json.WithDefaultValues].format[Person]
    
  //implicit val personCodec: CodecProvider = Macros.createCodecProvider[Person]


}
case class Person(_id: ObjectId = new ObjectId(), firstName: String, lastName: String)

import org.bson.codecs.configuration.CodecRegistries.{
  fromRegistries,
  fromProviders
}


class MongoDbExample @Inject() (implicit ec: ExecutionContext) {
  val codecRegistry =
    fromRegistries(fromProviders(classOf[Person]), DEFAULT_CODEC_REGISTRY)

  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")

  val database: MongoDatabase =
    mongoClient
      .getDatabase("practice-db-for-mongodb")
      .withCodecRegistry(codecRegistry)

  val collectionPerson: MongoCollection[Person] =
    database.getCollection("persons")

  //val person: Person = Person("vazand", "kuzhandaivel")
  //val insertOnePerson = collectionPerson.insertOne(person).toFuture()

  /* insertOnePerson.onComplete{
    case Success(result) =>
      println(s"Inserted ${result.getInsertedId} into collection persons")
    case Failure(exception) =>
      println(s"Failed to insert document: ${exception.getMessage}")
  } */
  // mongoClient.close()
}
