package edu.cornell.cac.docker.api.entities

import edu.cornell.cac.docker.api._
import play.api.libs.json._

trait DockerVolume extends DockerEntity {
  def containerPath: String
  def hostPath: String
}

sealed case class ContainerVolume(containerPath: String, hostPath: String = "") extends DockerVolume {
  override def toString = s"$containerPath"
}

sealed case class BindMountVolume(containerPath: String, hostPath: String, `type`: String) extends DockerVolume{
  override def toString = s"$hostPath:$containerPath" +
    (if (`type`.isEmpty) "" else s":${`type`}")
}
		
object DockerVolume {
  
  private val pattern = """^([^:]+):(/[^:]+):(ro|rw)$""".r

  def apply(path: String): DockerVolume = ContainerVolume(path)

  def apply(path: String, hostPath: String): DockerVolume =
    BindMountVolume(path, hostPath, "")

  def unapply(v: DockerVolume): Option[(String, String)] = {
    v match {
      case bind: BindMountVolume => Some((bind.containerPath, bind.hostPath))
      case vol: ContainerVolume => Some((vol.containerPath, ""))
      case _ => None
    }
  }

  def fromString(s: String): Option[BindMountVolume] = s match {
    case pattern(containerPath, hostPath, rwType) => Some(BindMountVolume(containerPath, hostPath, rwType))
    case _ => None
  }

}

object BindMountVolume {
  def apply(path: String, hostPath: String): BindMountVolume =
    BindMountVolume(path, hostPath, "")
}