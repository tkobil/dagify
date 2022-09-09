package com.tkobil.dagify

abstract class Task(val taskName: String) {
  def run(): Unit
}

class RunnableDataTask[T](taskName: String, runnable: T => Unit, dataClass: T) extends Task(taskName) {
  override def run(): Unit = runnable(dataClass)
}

class RunnableDataLessTask(taskName: String, runnable: => Unit) extends Task(taskName) {
  override def run(): Unit = runnable
}