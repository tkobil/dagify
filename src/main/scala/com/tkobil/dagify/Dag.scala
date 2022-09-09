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

abstract class DagCreator(val dagName: String) {
  def define(): Dag = ???

}

object DagExample extends App {

  val dag = new DagCreator("Sample Dag") {
    override def define(): Dag = {
      val dagInstance = new Dag(this.dagName)
      val task = new RunnableDataTask[Int]("print x", x => println(x), 10)
      val anotherTask = new RunnableDataTask[String]("print hello world", x => println(x), "hello world")
      val oneMoreTask = new RunnableDataTask[Int]("print 100", x => println(x), 100)
      val lastTask = new RunnableDataTask[Double]("print 30.2", x => println(x*50), 30.2)
      val oneLastTask = new RunnableDataTask[Double]("print 30.8", x => println(x*50), 30.8)

      // chaining dependencies
      val newDagInstance = dagInstance.addDependency(task, anotherTask).addDependency(task, oneMoreTask).addDependency(anotherTask, lastTask)

      val dataLessTask = new RunnableDataLessTask("data-less task", println("data-less"))
      // using plain-english feature
      newDagInstance hasDependency Dependency(oneMoreTask, lastTask) hasDependency Dependency(lastTask, oneLastTask) hasDependency Dependency(task, dataLessTask)
    }
  }.define()
  dag.show()
  dag.run()
}