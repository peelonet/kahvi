# Kahvi

**Work in progress**

Java compiler partially based on [Janino](http://www.janino.net), but mostly
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

## Non-nullable variables

Parameters and local variables can be declared as *non-nullable*, which means
that they do not accept values which can be potentially `null`.

~~~~
String! message = some_value;
~~~~

Above example declares *message* as non-nullable. If *some_value* could contain
`null`, result would be compile error. Any expression which could be `null`
must be explicitly tested like this:

~~~~
String! message = some_value != null ? some_value : "default value";
~~~~

Method parameters can also be declared as non-nullable:

~~~~
greet(String! name)
{
    System.out.println("Hello, " + name + "!");
}
~~~~

Above example translates into this:

~~~~
public void greet(@NotNull String name)
{
    if (name == null)
    {
        throw new NullPointerException("name");
    }
    System.out.println("Hello, " + name + "!");
}
~~~~

The `@NotNull` annotation is from [Java Validation API](http://docs.oracle.com/javaee/6/api/javax/validation/package-summary.html)
but since JVM ignores annotations which it cannot resolve, the resulting
`.class` file does not depend on any extra jar-files in order to run.

## New literals

As proposed in [Project Coin](http://mail.openjdk.java.net/pipermail/coin-dev/2009-March/001193.html);
lists, sets and maps have literal syntax.

~~~~
[1, 2, 3]
~~~~

Creates an immutable list of `java.lang.Integer` objects.

~~~~
{"foo", "bar", "baz"}
~~~~

Creates in immutable set of `java.lang.String` objects.

~~~~
{"foo": 1, "bar": 2, "baz": 3}
~~~~

Creates an immutable map of `java.lang.Integer` objects with `java.lang.String`
objects as keys.

Type of the collections can be explicitly declared.

~~~~
<Number> [1, 2, 3]
~~~~

Or with maps:

~~~~
<Object, Number> {"foo": 1, "bar": 2, "baz": 3}
~~~~

`java.net.URI` objects also have a literal syntax like this:
~~~~
`https://github.com/peelonet/kahvi.git/`
~~~~

Components of the URI are parsed during compilation, not runtime.

## Shorthand property access syntax

Properties can be read and modified with `@` operator.

~~~~
System.out.println(foo.@bar);
~~~~

Which would be equivalent of:
~~~~
System.out.println(foo.getBar());
~~~~

Assignment is also possible:
~~~~
foo.@bar = baz;
foo.@bar += baz;
~~~~

Which would be equivalent of:
~~~~
foo.setBar(baz);
foo.setBar(foo.getBar() + baz);
~~~~

## Subscript operator

The subscript operator `[...]` can also be used on lists and maps.

~~~~
var list = [1, 2, 3];
assert list[1] == 2;

var map = {"one": 1, "two": 2, "three": 3};
assert map["three"] == 3;
~~~~

## Else operator

The `else` operator is used for testing `null` values.

~~~~
foo else bar
~~~~

It is similiar to `foo != null ? foo : bar`, but *foo* is evaluated only once.
It is used for producing `null` free code.

~~~~
someMethod(Map<String, Integer> values)
{
    Integer! two = map["two"] else 2;

    System.out.println(two * 4);
}
~~~~

## Enhanced `for` statement

The `for` statement which is used for iteration can now iterate arrays,
`java.lang.Iterable` objects, `java.util.Map` objects, `java.util.Iterator`
objects and `java.util.Enumeration` objects. With type inference, type of the
iterated value can be omitted.

~~~~
var list = ["foo", "bar", "baz"];

for (var value : list)
{
    System.out.println(value.toUpperCase());
}
~~~~

## Shorthand return statement

Method body can be replaced with a single return statement by using the `=>`
operator.

~~~~
int getValue() => 5;
~~~~

Above method would be equivalent of:
~~~~
public int getValue()
{
    return 5;
}
~~~~

Return value can be replaced with a `throw` statement to create shorthand
`throw` statements.

~~~~
void remove() => throw new UnsupportedOperationException();
~~~~
