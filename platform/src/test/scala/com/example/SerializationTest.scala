package com.example

import org.joda.time.DateTime
import org.json4s.native._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{read,write}
import org.scalatest.{Matchers, FlatSpec}

class SerializationTest extends FlatSpec with Matchers {
  implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all

  "we" should "serialize correctly" in {

    val magic = 123L
    val ts = DateTime.now()
    val m = MagicEvent(magic,ts)
    val jstrm = write(m)
    val s1 = pretty(render( org.json4s.native.JsonMethods.parse(jstrm)))
    info(s1)
  }
}
