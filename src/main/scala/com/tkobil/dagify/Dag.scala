package com.tkobil.dagify


// dag extends task
// this way, tasks can depend on dags, dags can depend on each other, etc.
class Dag(override val taskName: String, val dependencyGraph: Map[Task, List[Task]] = Map()) extends Task(taskName) with PrintableDag {

  def addDependency(a: Task, b: Task): Dag = {
    if (dependencyGraph.contains(a)) new Dag(taskName, dependencyGraph.updated(a, dependencyGraph(a) :+ b))
    else new Dag(taskName, dependencyGraph ++ Map(a -> List(b)))
  }

  // overloaded method
  def addDependency(dependency: Dependency): Dag = {
    addDependency(dependency.parent, dependency.child)
  }

  def hasDependency(dependency: Dependency): Dag = {
    addDependency(dependency)
  }

  def getSorted(): Array[Task] = TopologicalSort(dependencyGraph)
  def show(): Unit = { // TODO - pretty-print dependencies w/o topological sorted order as well
    printStartDag()
    getSorted().foreach(x => {
      printArrow()
      println(s"TASK: ${x.taskName}")
    })
    printEndDag()
  }
  def run(): Unit = getSorted().foreach(x => x.run())

}

abstract class DagCreator {
  def define(): Dag = ???

}

object DagExample extends App {

  val dag = new DagCreator {
    override def define() = {
      val dagInstance = new Dag("sample dag")
      val task = new RunnableSideEffectTask[Int]("print x", x => println(x), 10)
      val anotherTask = new RunnableSideEffectTask[String]("print hello world", x => println(x), "hello world")
      val oneMoreTask = new RunnableSideEffectTask[Int]("print 100", x => println(x), 100)
      val lastTask = new RunnableSideEffectTask[Double]("print 30.2", x => println(x*50), 30.2)
      val oneLastTask = new RunnableSideEffectTask[Double]("print 30.8", x => println(x*50), 30.8)
      val newDagInstance = dagInstance.addDependency(task, anotherTask).addDependency(task, oneMoreTask).addDependency(anotherTask, lastTask)

      // using plain-english feature
      newDagInstance hasDependency Dependency(oneMoreTask, lastTask) hasDependency Dependency(lastTask, oneLastTask)
    }
  }.define()
  dag.show()
  dag.run()
}