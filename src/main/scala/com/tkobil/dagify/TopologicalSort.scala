package com.tkobil.dagify


object Colors {
  val white = 0
  val gray = 1
  val black = 2
}


object TopologicalSort {

  // Topological Sort Dag (of Tasks) based on task dependencies
  // raise exception based on invalid Dag (i.e. cycle exists)
  // TODO - enable generic types
  def apply(dependecyGraph: Map[Task[Int], List[Task[Int]]]): Array[Task[Int]] = {
    // topological sorting algorithm
    // TODO - implement in functional paradigm
    // currently in imperative state


    // initialize all colors to white
    var colors = dependecyGraph
      .flatMap(
        {case (k,v) => v.flatMap(subV => Map(k->Colors.white, subV->Colors.white))}
      )

    val topologicalSortedOutput = Array[Task[Int]]()

    def dfs(currentNode: Task[Int]): Unit = {
      // mark current node as processing
      colors = colors.updated(currentNode, Colors.gray)

      // visit all neighbors
      val neighbors = dependecyGraph(currentNode)
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
      topologicalSortedOutput :+ currentNode

    }
    colors.foreach(
      {case (k,v) => if (colors(k) == Colors.white) dfs(k)}
    )
    topologicalSortedOutput.reverse
  }
}
