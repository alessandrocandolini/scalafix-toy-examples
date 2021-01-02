# scalafix-toy-examples

Toy playground for scalafix, adapting few examples found in tutorials and talks. 

Scala version: Using scala 2.12 for now, because one of the examples (the cats upgrade from 0.9 to 1.0) cannot be written in scala 2.13 (because obviously 0.9 is not published for scala 2.13) 

The project is generated using the official Scalafix [giter8](https://github.com/foundweekends/giter8) template: https://github.com/scalacenter/scalafix.g8 

Github actions are automatically generated from the sbt project structure using the following sbt plugin: https://github.com/djspiewak/sbt-github-actions

References:

[1] Gabriele Petronella, Scalafix workshop, Scala World https://www.youtube.com/watch?v=uaMWvkCJM_E&t=3212s

[2] DevInsideYou's "Write your own #Scalafix rules from scratch!"  https://www.youtube.com/watch?v=uh7VFcOpu20&t=1261s
