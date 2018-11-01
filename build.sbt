name := "FreeVsTagless"

version := "0.1"

scalaVersion := "2.12.7"

//Cats
scalacOptions += "-Ypartial-unification"
val catsGroup = "org.typelevel"
val catsVersion = "1.4.0"
libraryDependencies ++= Seq(
  catsGroup %% "cats-core" % catsVersion,
  catsGroup %% "cats-free" % catsVersion
)  