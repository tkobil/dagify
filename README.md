# Dagify
A lightweight framework for creating and scheduling Directed Acyclic Graphs. Inspiration is taken from Apache Airflow and other Data Engineering DAG-based workflow managers

## Yet Another DAG Scheduler
Why create another DAG scheduler? The open-source community has already heavily adopted mature tools like Apache Airflow, Luigi, etc.
Dagify is different. Dagify aims to simplify the life of the developer working on side projects, the one who doesn't need a distributed computing environment for their tasks, and the one who just wants a nice api that can be used to chain together idempotent tasks - without having to worrying about the intricacies of dependency order management.

Dagify is a tool that can be used for resolving package installation ordering, compute task scheduling, etc - all while abstracting away the scheduling logic and complex topological sort algorithm.

# Example Usage
```
import com.tkobil.dagify.{Dag, DagCreator, Dependency, RunnableSideEffectTask}

  # Define Anonymous class, overriding define() method.
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
```
### Pretty Print Dag in Topological Sorted Order
```
dag.show()

----------------------------------------------------------------------------------------------------
START DAG - sample dag
  |
  |
  v
TASK: print x
  |
  |
  v
TASK: print 100
  |
  |
  v
TASK: print hello world
  |
  |
  v
TASK: print 30.2
  |
  |
  v
TASK: print 30.8
  |
  |
  v
END DAG
----------------------------------------------------------------------------------------------------
```

### Run Dag in Topological sorted order
```
dag.run()

10
100
hello world
1510.0
1540.0
```

#### Dagify is currently undergoing development and is not ready for production use