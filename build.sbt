organization in ThisBuild := "net.tixxit"

licenses in ThisBuild += ("BSD-style" -> url("http://opensource.org/licenses/MIT"))

scalaVersion in ThisBuild := "2.11.11"

crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.11", "2.12.2")

scalacOptions in ThisBuild ++= Seq("-deprecation", "-feature", "-unchecked", "-language:higherKinds", "-optimize")

maxErrors in ThisBuild := 5

addCommandAlias("publishDocs", ";unidoc;ghpagesPushSite")

import com.typesafe.sbt.site.SitePlugin.autoImport._

lazy val commonJsSettings = Seq(
  scalaJSStage in Global := FastOptStage
)

lazy val commonTestSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest"  %%% "scalatest"  % Deps.V.scalaTest  % "test",
    "org.scalacheck" %%% "scalacheck" % Deps.V.scalaCheck % "test"
  )
)

lazy val root = project.
  in(file(".")).
  aggregate(delimitedCore, delimitedCoreJS, delimitedIteratee, delimitedIterateeJS).
  enablePlugins(GhpagesPlugin, ScalaUnidocPlugin, SiteScaladocPlugin).
  settings(Publish.skip: _*).
  settings(
    name := "delimited",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in SiteScaladoc),
    autoAPIMappings := true,
    git.remoteRepo := "git@github.com:tixxit/delimited.git"
  )

lazy val delimitedCoreBase = crossProject.crossType(CrossType.Pure).
  in(file("delimited-core")).
  settings(Publish.settings: _*).
  settings(commonTestSettings: _*).
  jsSettings(commonJsSettings: _*).
  jvmConfigure(_.copy(id = "delimitedCore")).
  jsConfigure(_.copy(id = "delimitedCoreJS"))

lazy val delimitedCore = delimitedCoreBase.jvm
lazy val delimitedCoreJS = delimitedCoreBase.js

lazy val delimitedIterateeBase = crossProject.crossType(CrossType.Pure).
  in(file("delimited-iteratee")).
  dependsOn(delimitedCoreBase).
  settings(commonTestSettings: _*).
  settings(Publish.settings: _*).
  settings(
    libraryDependencies += "io.iteratee" %%% "iteratee-core" % Deps.V.iteratee
  ).
  jvmConfigure(_.copy(id = "delimitedIteratee")).
  jsConfigure(_.copy(id = "delimitedIterateeJS"))

lazy val delimitedIteratee = delimitedIterateeBase.jvm
lazy val delimitedIterateeJS = delimitedIterateeBase.js

lazy val delimitedBenchmark = project.
  in(file("delimited-benchmark")).
  dependsOn(delimitedCore).
  settings(Publish.skip: _*)
