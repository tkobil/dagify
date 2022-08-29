package com.tkobil.dagify

trait PrintableDag {
  // TODO - enable pretty printing dag dependency graph!
  // TODO - consider html output
  val taskName: String
  def printArrow(): Unit = {
    println("  |")
    println("  |")
    println("  v")
  }
  def printStartDag(): Unit = {
    println("-"*100)
    println(s"START DAG - $taskName")
  }
  def printEndDag(): Unit = {
    printArrow()
    println("END DAG")
    println("-"*100)
  }
}