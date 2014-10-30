
To Do
=====

 * Finish Negate implementation (using NegateNode)

Nice to haves
=============

 * Implement for
    for elem in iterable {
      ...
    }
 * Add support for string interpolation
    "Your result is: ${2 + 2}"
 * Use CheckClassAdapter to check that ASMVisitor is using ClassWriter properly
 * Add LINENUMBER bytecode instructions (useful for debugging)
 * Implement loop forever
    loop {
      ...
    }
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
