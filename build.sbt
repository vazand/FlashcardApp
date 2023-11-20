name := """FlashCardApp"""
organization := "io.github.vazand" 
//"com.vatakamate.me"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.11.1"


//libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "1.1.0-play28-RC11"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "me.vazand.github.io.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "me.vazand.github.io.binders._"
