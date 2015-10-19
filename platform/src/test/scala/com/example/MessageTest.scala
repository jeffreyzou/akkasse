package com.example

import org.scalatest.{Matchers, FlatSpec}

class MessageTest extends FlatSpec with Matchers {

  "a message" should "be emitted" in {
    info(MessageGenerator.oneMessage())
  }
}
