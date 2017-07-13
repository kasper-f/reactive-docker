//FIXME: add integration test properly

addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)

lazy val commonSettings = Seq(
  name := "reactive-docker",
  organization := "com.asergo",
  version := "0.1.1",
  scalaVersion := "2.12.2"
)

lazy val logbackVer = "1.2.3"

lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(commonSettings: _*).
  settings(Defaults.itSettings: _*).
  settings(
  /*  resolvers ++= Seq(
    pub   Resolver.mavenLocal,
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases"),
      Resolver.typesafeRepo("snapshots")
      //"Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      //"spray repo" at "http://repo.spray.io"
    ),*/
    libraryDependencies ++= Seq(
    //"org.scalaxb" % "scalaxb_2.11" % "1.4.1",
    // "co.fs2" %% "fs2-core" % "0.9.0-RC2",
    "com.chuusai" %% "shapeless" % "2.3.2",
    //"org.scalaz.stream" %% "scalaz-stream" % "0.3.1",
    "com.netaporter" %% "scala-uri" % "0.4.16",
    "com.typesafe.play" %% "play-json" % "2.6.2",
     "com.typesafe.play" %% "play-json-joda" % "2.6.2",
    "com.typesafe.play" %% "play-iteratees" % "2.6.1",
    // "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
    "net.databinder.dispatch" %% "dispatch-core" % "0.12.0",
    "ch.qos.logback" % "logback-core" % logbackVer,
    "ch.qos.logback" % "logback-classic" % logbackVer,
    "org.apache.commons" % "commons-compress" % "1.13",

    "org.specs2" %% "specs2" % "2.4.17" % "it,test",
    "org.skyscreamer" % "jsonassert" % "1.5.0" % "it,test"

  ),
  // other settings here
  //Note: Wart.Nothing is good in principle but too many false positives IMO

  //wartremoverWarnings += Wart.allBut(Wart.Nothing),

  //,wartremoverErrors ++= Warts.unsafe
  //,wartremoverErrors ++= Warts.allBut(Wart.Var, Wart.Nothing)

  publishMavenStyle := true,


  publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },

  parallelExecution in Test := false,

  publishArtifact in Test := false,

  pomIncludeRepository := { _ => false },

  pomExtra := (
    <url>http://github.com/almoehi/reactive-docker</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:almoehi/reactive-docker.git</url>
        <connection>scm:git:git@github.com:almoehi/reactive-docker.git</connection>
      </scm>
      <developers>
        <developer>
          <id>almoehi</id>
          <name>alm oehi</name>
          <url>http://github.com/almoehi/</url>
        </developer>
      </developers>),

  scalacOptions ++= Seq("-deprecation","-language:_"),


  javacOptions ++= Seq("-target", "1.8", "-source","1.8"),


  // see https://github.com/typesafehub/scalalogging/issues/23
  testOptions in Test += Tests.Setup(classLoader =>
    classLoader
      .loadClass("org.slf4j.LoggerFactory")
      .getMethod("getLogger", classLoader.loadClass("java.lang.String"))
      .invoke(null, "ROOT")
  ),
  testOptions in Test += Tests.Argument("-oDF")

)



