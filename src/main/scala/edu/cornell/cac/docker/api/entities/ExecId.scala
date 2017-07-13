package edu.cornell.cac.docker.api.entities

import edu.cornell.cac.docker.api._

class ExecId private[ExecId](val id: String) extends DockerEntity {
	override def toString: String = id
	override def equals(o: Any) = o match {
	  case s: String => id.equalsIgnoreCase(s)
	  case _ => false
	}
}

object ExecId {
	val shortPattern = """^([a-z0-9A-Z])+$""".r
	val longPattern = """^([a-z0-9A-Z])+$""".r

	def apply(s: String): ExecId = s match {
		case shortPattern(id) => new ExecId(s)
		case longPattern(id) => new ExecId(s)
		case _ => throw InvalidExecIdFormatException(s"$s is an invalid container ID", s)
	}

	def unapply(id: ExecId): Option[String] = {
		Some(id.id)
	}

	def emptyId = new ExecId("")
}
