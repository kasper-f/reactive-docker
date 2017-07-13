package edu.cornell.cac.docker.api

import edu.cornell.cac.docker.api._
import edu.cornell.cac.docker.api.entities._
import org.slf4j.LoggerFactory
import org.specs2.mutable.Specification
import play.api.libs.iteratee._

import scala.concurrent._
import scala.concurrent.duration._

class DockerBuildSpec extends Specification with DefaultDockerAuth {
  
  implicit def defaultAwaitTimeout: Duration = Duration.create(40, SECONDS)
  
  implicit val docker = Docker()
  
  private val log = LoggerFactory.getLogger(getClass())
  
  def await[T](f: Future[T]): T = {
    Await.result(f, defaultAwaitTimeout)
  }
  
  sequential
  
  "DockerApi" should {
    
   "be able to build from simple dockerfile given as String" in new DockerContext {
     val (it, en) = Concurrent.joined[Array[Byte]]
      val maybeRes = (en &> DockerEnumeratee.rawStream &> Enumeratee.map{el => 
        log.info(s"DockerBuildSpec: $el")
        el
      } |>>> Iteratee.head)
      
      await(docker.dockerBuildIterateeFrom("reactive-docker-build", true, false, true)(it){() =>
          Seq(
          """FROM dockerfile/java""",
          """RUN ["mkdir", "-p", "/opt/docker/logs"]""",
          """EXPOSE 9000 9443""",
          """WORKDIR /opt/docker""",
          """RUN ["chown", "-R", "root", "."]""",
          """USER root""",
          """ENTRYPOINT ["/bin/bash"]""",
          """CMD []"""
          )
        }.flatMap(_.run)
      )
      
      val res = await(maybeRes)
      res must beSome{raw:DockerRawStreamChunk => 
        raw.channel must be_>=(0)
      }
    }
    
   
    "be able to build from Dockerfile DSL" in new DockerContext {
      import edu.cornell.cac.docker.dsl._
      val dockerfile = Dockerfile from "ubuntu" by "me <me@somehost.de>" expose (80, 8080) starting withArgs("ls", "-lah", "/opt/src") add "src/" -> "/opt/src"
      val res = await(docker.dockerfileBuild(dockerfile, "dsl-container"))
      //println(res)
      val last = res.last
      
      last should beRight{msg:DockerStatusMessage => 
          msg.error should beNone
      }
    }
    
    
    "be able to build from Dockerfile DSL and fail on error" in new DockerContext {
      import edu.cornell.cac.docker.dsl._
      val dockerfile = Dockerfile from "ubuntu" by "me <me@somehost.de>" run "mkdir -p /opt" install "someNonExistPackage" expose (80, 8080) starting withArgs("ls", "-lah", "/opt/src") add "src/" -> "/opt/src"
      
      //log.info(dockerfile.toString)
      
      val res = await(docker.dockerfileBuild(dockerfile, "dsl-container"))
      //println(res)
      
      val last = res.last
      
      last should beLeft{msg:DockerErrorInfo => 
          msg.message should not beEmpty
      }
    }
  }
}