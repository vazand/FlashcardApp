package models

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._
import org.apache.pekko.stream.impl.Completed

import org.mongodb.scala.bson.ObjectId

object Person {
  def apply(firstName: String, lastName: String): Person =
    Person(new ObjectId(), firstName, lastName)
}
case class Person(_id: ObjectId, firstName: String, lastName: String)

import org.bson.codecs.configuration.CodecRegistries.{
  fromRegistries,
  fromProviders
}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

class MongoDbExample @Inject() (implicit ec: ExecutionContext) {
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = mongoClient.getDatabase("myexampledb")
  val collection: MongoCollection[Document] = database.getCollection("sample")
  val codecRegistry =
    fromRegistries(fromProviders(classOf[Person]), DEFAULT_CODEC_REGISTRY)

  val personCollections = database.withCodecRegistry(codecRegistry)

  val person: Person = Person("Ada", "Lovelace")
  
  //collection.insertOne(person).results()


}

class MongoDbInjector @Inject() (implicit ec: ExecutionContext) {
  val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = mongoClient.getDatabase("myexampledb")
  val collection: MongoCollection[Document] = database.getCollection("sample")

  val doc: Document = Document(
    "_id" -> 0,
    "name" -> "MongoDB",
    "type" -> "database",
    "count" -> 1,
    "info" -> Document("x" -> 203, "y" -> 102)
  )

  // Insert the document asynchronously
  val insertResultFuture = collection.insertOne(doc).toFuture()

  // Handle the result asynchronously
  insertResultFuture.onComplete {
    case scala.util.Success(result) => println(s"Inserted: $result")
    case scala.util.Failure(error) =>
      println(s"Failed to insert document: $error")
  }
}
