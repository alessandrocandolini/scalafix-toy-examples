![Continuous Integration](https://github.com/alessandrocandolini/scalafix-toy-examples/workflows/Continuous%20Integration/badge.svg)

# scalafix-toy-examples

Toy playground project to experiment with scalafix APIs, adapting few examples found in online tutorials and talks.

All examples are handled within the same rule, by assembling different `Patches`, because this is just toy project; in a more production-grade setting, they would be different rules of course, or at least we would need to be careful to avoid conflicts. 

Examples include:
* replace 42 with 43 (for fun)
* replace `cats.data.Coproduct` (cats < 1.0) to `cats.data.EitherK` using `renameSymbol` 
* make all the case classes final by default 
* have sealed traits and sealed abstract classes always extend `Product with serializable` automatically (the rule also sorts them, and supports cases where the original classes/traits were already extending either one or the other but not both). 

Scala version: the project uses scala `2.12` for now, because one of the examples (the cats upgrade from 0.9 to 1.0) cannot be written in scala 2.13 (because obviously cats 0.9 is not published for scala 2.13) 

The project is generated using the official Scalafix [giter8](https://github.com/foundweekends/giter8) template: https://github.com/scalacenter/scalafix.g8 

Github actions are automatically generated from the sbt project structure using the following sbt plugin: https://github.com/djspiewak/sbt-github-actions

Main references:

[1] Gabriele Petronella, Scalafix workshop, Scala World https://www.youtube.com/watch?v=uaMWvkCJM_E&t=3212s

[2] DevInsideYou's "Write your own #Scalafix rules from scratch!"  https://www.youtube.com/watch?v=uh7VFcOpu20&t=1261s
