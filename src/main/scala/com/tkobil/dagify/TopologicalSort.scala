package com.tkobil.dagify


object Colors {
  val white = 0 // unprocessed
  val gray = 1 // processing
  val black = 2 // processed
}


object TopologicalSort {

  // Topological Sort Dag (of Tasks) based on task dependencies
  // raise exception based on invalid Dag (i.e. cycle exists)
  // TODO - enable generic types
  def apply(dependencyGraph: Map[Task, List[Task]]): Array[Task] = {
    // topological sorting algorithm
    // TODO - implement in functional paradigm
    // currently in imperative state


    // initialize all colors to white
    var colors = dependencyGraph
      .flatMap(
        {case (k,v) => v.flatMap(subV => Map(k->Colors.white, subV->Colors.white))}
      )

    var topologicalSortedOutput = Array[Task]()

    def dfs(currentNode: Task): Unit = {
      // mark current node as processing
      colors = colors.updated(currentNode, Colors.gray)

      // visit all neighbors
      val neighbors = dependencyGraph.getOrElse(currentNode, List())
      neighbors.foreach(neighborNode => {
        val neighborColor = colors(neighborNode)
        neighborColor match {
          case Colors.white => dfs(neighborNode)
          case Colors.black => // do nothing - node already discovered
          case Colors.gray => throw CyclicGraphException("Cycle detected in Dag!")
        }
      })

      // mark current node as processed
      colors = colors.updated(currentNode, Colors.black)
      // add processed node to topological sorted output
      topologicalSortedOutput = topologicalSortedOutput :+ currentNode

    }
    colors.foreach(
      {case (k,v) => if (colors(k) == Colors.white) dfs(k)}
    )
    topologicalSortedOutput.reverse
  }
}

object TopologicalSortRunner extends App {
  val a1 = new RunnableDataTask[Int](
    "a1",
    println,
    30
  )

  val a2 = new RunnableDataTask[Int](
    "a2",
    x => println(x+12),
    24
  )

  val b1 = new RunnableDataTask[String](
    "b1",
    x => println(s"hello world from $x"),
    "task b1"
  )

  val b2 = new RunnableDataTask[Double](
    "b2",
    x => println("this is a double test!"),
    32.2
  )
  val dependencyGraph: Map[Task, List[Task]] = Map(a1 -> List(a2, b1), b1 -> List(b2))
  TopologicalSort(dependencyGraph).foreach(x => println(x.taskName))





}
