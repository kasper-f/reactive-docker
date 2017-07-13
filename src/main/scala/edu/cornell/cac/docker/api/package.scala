package edu.cornell.cac.docker


package object api {

  import edu.cornell.cac.docker.api.entities._
  import edu.cornell.cac.docker.api.json.Formats._

  type DockerMessage = Either[DockerErrorInfo,DockerStatusMessage]

  implicit val dockerJsonFormats = edu.cornell.cac.docker.api.json.Formats
  
  sealed trait DockerAttachable { }
    
  case object Stdin extends DockerAttachable
  case object Stdout extends DockerAttachable
  case object Stderr extends DockerAttachable
}