package edu.cornell.cac.docker.api.entities

/**
  * @author Brandon Barker
  *         9/19/2016
  */
sealed case class ExecCreate(
  Cmd: Seq[String],
  AttachStdin: Option[Boolean] = None,
  AttachStdout: Option[Boolean] = None,
  AttachStderr: Option[Boolean] = None,
  DetachKeys: String = "ctrl-q",
  Tty: Boolean = false
) extends DockerEntity
