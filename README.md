![Continuous Integration](https://github.com/alessandrocandolini/scalafix-toy-examples/workflows/Continuous%20Integration/badge.svg)

# scalafix-toy-examples

Toy playground project to experiment with [scalafix](https://scalacenter.github.io/scalafix/)'s APIs, adapting few examples found in online tutorials and talks.

This rule applies many changes at once. The changes are modular and implemented in different methods, but the final rule sums all these changes and the tests are testing the overall result of applying all the changes to the input project. This is not good practise obviously, but this is just a playground to try different things. In a more production-grade setting, the different changes would be different rules, or at least extra care would be needed to be sure to avoid conflicts. 

Changes applied include:
* replace literals, eg the `Lit.Int` with value `42` becomes `43` using `Patch.replaceTree` (warm up exercise to experiment with the literal types)
* replace `cats.data.Coproduct` (cats < 1.0) to `cats.data.EitherK` using `renameSymbol` 
* make all the case classes final by default, using `Patch.addLeft` 
* have sealed traits and sealed abstract classes always extend `Product with serializable` automatically using `Template` API (the rule also sorts them, and supports cases where the original classes/traits were already extending either one or the other but not both). 

The project is generated using the official Scalafix [giter8](https://github.com/foundweekends/giter8) template: https://github.com/scalacenter/scalafix.g8 
and it follows its standard structure, ie, a `sbt` project with four sub-projects: rules, where the code lives; tests, scaffolding for running the tests; input and output subprojects that are used as input and expected result in the tests. To run the tests use 

```
sbt tests/test
```

(if you want to do TDD, `sbt ~tests/test`) 

Github actions were automatically generated from the sbt project structure using the following sbt plugin: https://github.com/djspiewak/sbt-github-actions but i had to tweak them because the project cannot run on scala 2.13 (just because cats is not available). I will restore them when I'll implement a similar example that doesn't require old version of libraries not published for latest scala. 

Main references:

[1] Gabriele Petronella, Scalafix workshop, Scala World https://www.youtube.com/watch?v=uaMWvkCJM_E&t=3212s

[2] DevInsideYou's "Write your own #Scalafix rules from scratch!"  https://www.youtube.com/watch?v=uh7VFcOpu20&t=1261s
