package com.tkobil.dagify

class CustomExceptions {

  case class CyclicGraphException(msg: String) extends Exception(msg)

}
