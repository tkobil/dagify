package com.tkobil.dagify

case class Dependency(parent: Task, child: Task)
// child must be completed before parent!
