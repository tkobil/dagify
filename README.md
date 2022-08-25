# Dagify
A lightweight framework for creating and scheduling Directed Acyclic Graphs. Inspiration is taken from Apache Airflow and other Data Engineering DAG-based workflow managers

## Yet Another DAG Scheduler
Why create another DAG scheduler? The open-source community has already heavily adopted mature tools like Apache Airflow, Luigi, etc.
Dagify is different. Dagify aims to simplify the life of the developer working on side projects, the one who doesn't need a distributed computing environment for their tasks, and the one who just wants a nice api that can be used to chain together idempotent tasks - without having to worrying about the intricacies of dependency order management.

Dagify is a tool that can be used for resolving package installation ordering, compute task scheduling, etc - all while abstracting away the scheduling logic and complex topological sort algorithm.


#### Dagify is currently undergoing development and is not ready for production use