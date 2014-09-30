To Do
=====

 * Implement while
    while exp {
      ...
    }
 * Implement for
    for elem in iterable {
      ...
    }
 * Finish support for boolean operations
 * Finish support for integer operations
 * Replace dummy implementation of method invocation
 * Make sure that Kernel methods are usable for basic input/output
 * Add support for executing code like this:
    $ lang run myscript.lang

Nice to haves
=============

 * Add LINENUMBER bytecode instructions (useful for debugging)
 * Add support for printing the AST like this
    $ lang ast myscript.lang
 * Add support for defining methods
 * Implement loop forever
    loop {
      ...
    }
 * Implement `break` and `continue` instructions
 * Add support for const blocks similar to Go
 * Optimize constant expressions (ones that always evaluate to same value)
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
 * Add support for some cool concurrency pattern (actors?)
 * Add syntax for exception handling
 * Add support for floating point numbers and operations over them
 * Add support for custom data structures (structs or classes)
