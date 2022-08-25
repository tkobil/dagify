package com.tkobil.dagify

abstract class Task[T] {
  def add_dependency(dependentTask: Task[T]): Task[T]

  def >>>(dependentTask: Task[T]): Task[T] = {
    add_dependency(dependentTask)
  }

  def run(): Unit
}

// TODO - enable dependent tasks to have different generic type
class RunnableSideEffectTask[T](runnable: T => Unit, dataClass: T, val dependencies: List[Task[T]] = List()) extends Task[T] {
  override def run(): Unit = runnable(dataClass)

  // functionally add new dependency, returning
  // new copy of Task
  override def add_dependency(dependentTask: Task[T]): RunnableSideEffectTask[T] = {
    val newDependencies = dependencies :+ dependentTask
    new RunnableSideEffectTask[T](runnable, dataClass, newDependencies)
  }

  override def >>>(dependentTask: Task[T]): RunnableSideEffectTask[T] = {
    add_dependency(dependentTask)
  }

}



object TaskRunner extends App {
  // example task runner
  val task = new RunnableSideEffectTask[Int](x => println(x), 10)
  val anotherTask = new RunnableSideEffectTask[Int](x => println(x*20), 13)
  val fullTask = task >>> anotherTask
  fullTask.dependencies.foreach(x => x.run)


}