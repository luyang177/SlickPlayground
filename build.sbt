name := "SlickTest"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
  "com.typesafe.slick" %% "slick-codegen" % "3.2.3",
  "com.h2database" % "h2" % "1.4.197",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.2.1.jre8",
)