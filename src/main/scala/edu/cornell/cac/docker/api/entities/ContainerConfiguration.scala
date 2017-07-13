package edu.cornell.cac.docker.api.entities

import play.api.libs.json._

sealed case class ContainerConfiguration(
  Image: Option[String] = None,
  Cmd: Option[Seq[String]] = None,
  Hostname: Option[String] = None,
  User: Option[String] = None,
  Memory: Option[Long] = None,
  MemorySwap: Option[Long] = None,
  AttachStdin: Option[Boolean] = None,
  AttachStdout: Option[Boolean] = None,
  AttachStderr: Option[Boolean] = None,
  //portSpecs: Option[Seq[String]] = None, // deprec
  Tty: Option[Boolean] = None,
  OpenStdin: Option[Boolean] = None,
  StdinOnce: Option[Boolean] = None,
  Env: Option[Seq[String]] = None,
  Dns: Option[String] = None,
  Volumes: Option[Map[String, DockerVolume]] = None,
  VolumesFrom: Option[ContainerId] = None,
  WorkingDir: Option[String] = None,
  ExposedPorts: Option[Map[String, DockerPortBinding]] = None,
  HostConfig: Option[HostConfig] = None,
  Entrypoint: Option[Seq[String]] = None,
  NetworkDisabled: Option[Boolean] = Some(false),
  OnBuild: Option[Seq[String]] = None
) extends DockerEntity

object ContainerConfig {
  
  def apply(json: JsObject)(implicit fmt: Format[ContainerConfiguration]): ContainerConfiguration = {
    val res = Json.fromJson[ContainerConfiguration](json)(fmt)
    res.asOpt match {
      case Some(c) => c
      case _ => throw new RuntimeException(s"failed to deserialize container config from json: " + Json.prettyPrint(json))
    }
  }
  
  def apply(json: String)(implicit fmt: Format[ContainerConfiguration]): ContainerConfiguration = {
    val res = Json.fromJson[ContainerConfiguration](Json.parse(json))(fmt)
    res.asOpt match {
      case Some(c) => c
      case _ => throw new RuntimeException(s"failed to deserialize container config from json: " + json)
    }
  }
  
  def apply(image: String, cmd: Seq[String]): ContainerConfiguration = ContainerConfiguration(Some(image), Some(cmd))
}
