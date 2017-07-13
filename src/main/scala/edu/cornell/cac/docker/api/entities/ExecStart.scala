package edu.cornell.cac.docker.api.entities

/**
  * @author Brandon Barker
  *         9/19/2016
  */
sealed case class ExecStart (
  Detach: Boolean = true,
  Tty: Boolean = false
) extends DockerEntity