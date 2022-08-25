package com.tkobil.dagify

abstract class Task(val taskName: String) {

  def add_dependency(dependentTask: Task): Task

  def >>>(dependentTask: Task): Task = {
    add_dependency(dependentTask)
  }

  def run(): Unit
}

class RunnableSideEffectTask[T](taskName: String, runnable: T => Unit, dataClass: T, val dependencies: List[Task] = List()) extends Task(taskName) {

  override def run(): Unit = runnable(dataClass)

  // functionally add new dependency, returning new copy of Task
  override def add_dependency(dependentTask: Task): RunnableSideEffectTask[T] = {
    val newDependencies = dependencies :+ dependentTask
    new RunnableSideEffectTask[T](taskName, runnable, dataClass, newDependencies)
  }

  override def >>>(dependentTask: Task): RunnableSideEffectTask[T] = {
    add_dependency(dependentTask)
  }

}

object TaskRunner extends App {
  // example task runner
  val task = new RunnableSideEffectTask[Int]("print x", x => println(x), 10)
  val anotherTask = new RunnableSideEffectTask[String]("print hello world", x => println(x), "hello world")
  val oneMoreTask = new RunnableSideEffectTask[Int]("print 100", x => println(x), 100)
  val lastTask = new RunnableSideEffectTask[Double]("print 30.2", x => println(x*50), 30.2)
  val fullTask = task >>> anotherTask >>> oneMoreTask >>> lastTask
  fullTask.dependencies.foreach(x => x.run)


}