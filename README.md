# Kahvi

Java compiler partly based on [Janino](http://www.janino.net), but mostly
written from scratch.

It's purpose is to create minor improvements for the Java language, not to
create a completely new programming language for JVM.

Here are some of the features:

## Default visibility

Since im tired of writing `public` everywhere, types, fields, constructors and
methods all have a default visibility. For types, constructors and methods,
it's `public` and for fields it's `private`.

~~~~
class Example
{
    String value;

    Example(String value)
    {
        this.value = value;
    }

    String getValue()
    {
        return this.value;
    }
}
~~~~

You can still use package level visibility, but it must be prefixed with the
`package` keyword:

~~~~
package void greet()
{
    System.out.println("Hello, World!");
}
~~~~

## Type inference

Java is statically compiled language, so the compiler already knows type of
each method and variable. With `var` keyword you can skip explicit type
declarations and let the compiler to determine type of variable or method.

### Local variables

~~~~
var foo = "bar";
~~~~

Translates into this:

~~~~
String foo = "bar";
~~~~

### Methods

If return type of a method is omitted, it is determined by the compiler from
return values inside the method body.

~~~~
foo()
{
    return 5;
}
~~~~

The return type of the method above is clearly `int`, so the compiler declares
it as `int foo()`.

If method has multiple return values, the *least generic type* from these
values is used as the return type of the method.

~~~~
foo(boolean someCondition)
{
    if (someCondition)
    {
        return new java.util.LinkedList<String>();
    } else {
        return new java.util.HashSet<String>();
    }
}
~~~~

In above example, two different kinds of objects are returned, but they have a
common super class *java.util.Collection* which is used as the return type.
