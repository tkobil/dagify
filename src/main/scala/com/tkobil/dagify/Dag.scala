package com.tkobil.dagify

class Dag(val dependencyGraph: Map[Task, List[Task]] = Map()) {

  def addDependency(a: Task, b: Task): Dag = {
    if (dependencyGraph.contains(a)) new Dag(dependencyGraph.updated(a, dependencyGraph(a) :+ b))
    else new Dag(dependencyGraph ++ Map(a -> List(b)))
  }

  def getSorted(): Array[Task] = TopologicalSort(dependencyGraph)
  def show(): Unit = { // TODO - pretty-print dependecies w/o topological sorted order as well
    printStartDag()
    getSorted().foreach(x => {
      printArrow()
      println(s"TASK: ${x.taskName}")
    })
    printEndDag()
  }
  def run(): Unit = getSorted().foreach(x => x.run())

  def printArrow(): Unit = {
    println("  |")
    println("  |")
    println("  v")
  }
  def printStartDag(): Unit = {
    println("-"*100)
    println("START DAG")
  }
  def printEndDag(): Unit = {
    printArrow()
    println("END DAG")
    println("-"*100)
  }

}

abstract class DagCreator {
  def define(): Dag = ???

}

object DagExample extends App {

  val dag = new DagCreator {
    override def define() = {
      val dagInstance = new Dag()
      val task = new RunnableSideEffectTask[Int]("print x", x => println(x), 10)
      val anotherTask = new RunnableSideEffectTask[String]("print hello world", x => println(x), "hello world")
      val oneMoreTask = new RunnableSideEffectTask[Int]("print 100", x => println(x), 100)
      val lastTask = new RunnableSideEffectTask[Double]("print 30.2", x => println(x*50), 30.2)
      dagInstance.addDependency(task, anotherTask).addDependency(task, oneMoreTask).addDependency(anotherTask, lastTask)
    }
  }.define()
  dag.show()
  dag.run()
}