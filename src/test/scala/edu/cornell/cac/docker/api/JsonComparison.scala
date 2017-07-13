package edu.cornell.cac.docker.api

import org.skyscreamer.jsonassert.JSONCompare.compareJSON
import org.skyscreamer.jsonassert.JSONCompareMode
/**
  * Created by Brandon on 10/25/2016.
  *
  * Add contributions to https://gist.github.com/bbarker/5b0baec6f0c149df245e03c24361d145
  *
  */
object JsonComparison {
  def compareJsonLenient(expected: String, actual: String): Boolean =
    compareJSON(expected, actual, JSONCompareMode.LENIENT)
      .passed()
}
