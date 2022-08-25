package com.tkobil.dagify

trait Runnable {
  def run(): Unit
}

// TODO - enable dependent tasks to have different generic type
class Task[T](runnable: T => Unit, dataClass: T, val dependencies: List[Task[T]] = List()) extends Runnable {
  override def run(): Unit = runnable(dataClass)

  // functionally add new dependency, returning
  // new copy of Task
  private def add_dependency(dependentTask: Task[T]): Task[T] = {
    val newDependencies = dependencies :+ dependentTask
    new Task[T](runnable, dataClass, newDependencies)
  }

  def >>>(dependentTask: Task[T]): Task[T] = {
    add_dependency(dependentTask)
  }
}

object TaskRunner extends App {
  // example task runner
  val task = new Task[Int](x => println(x), 10)
  //task.run()

  val anotherTask = new Task[Int](x => println(x*20), 13)
  //anotherTask.run()
  val fullTask = task >>> anotherTask
  fullTask.dependencies.foreach(x => x.run)


}