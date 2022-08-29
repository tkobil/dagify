package com.tkobil.dagify

abstract class Task(val taskName: String) {
  def run(): Unit
}

class RunnableSideEffectTask[T](taskName: String, runnable: T => Unit, dataClass: T) extends Task(taskName) {
  override def run(): Unit = runnable(dataClass)
}