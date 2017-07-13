package edu.cornell.cac.docker.api.entities

/**
  * @author Brandon Barker
  *         10/26/2016
  */
sealed case class HostConfig(
 binds: Option[Seq[BindMountVolume]] = None
)
