
To Do
=====

 * Implement variable assignment (Kevin)
 * Implement bin/lang compile myscript.lang
 * Finish Negate implementation (using NegateNode)
 * Implement `LESS_OR_EQUAL_THAN`, `GREATER_OR_EQUAL_THAN`, `NOT_EQUAL` (Fer)
 * Implement modulo operator `%`
 * Replace dummy implementation of method invocation
 * Make sure that Kernel methods are usable for basic input/output

Nice to haves
=============

 * Emit warnings for unused variables and functions
 * Implement for
    for elem in iterable {
      ...
    }
 * Add support for string interpolation
    "Your result is: ${2 + 2}"
 * Simplify code generation with GeneratorAdapter
 * Use CheckClassAdapter to check that ASMVisitor is using ClassWriter properly
 * Add LINENUMBER bytecode instructions (useful for debugging)
 * Add recursion (should be easy I guess, but depends on how method invocation is implemented)
 * Add support for defining methods
 * Implement loop forever
    loop {
      ...
    }
 * Add support for const blocks similar to Go
 * Add support for mutable and inmutable references
    `let|val|const x = 10` vs `var x = 10` or just `x = 10`
 * Add Java interoperability
 * Add literals for lists, maps, sets, ranges, regexes
      [1, 2, 3, 4]
      { "foo": "bar", "baz": "qux" }
      { "foo", "bar", "baz" }
      1..2
      /(foo)?/
 * Add support for lambda expressions
      (x) => x + 2
 * Add support for some cool concurrency pattern (channels? actors?)
 * Add syntax for exception handling
 * Add support for floating point numbers and operations over them
 * Implement pattern matching (?)
