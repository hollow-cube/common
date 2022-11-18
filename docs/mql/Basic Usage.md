MQL may be used either in an interpreted mode, which may support features not present in compiled mode.

### Interpreted
Interpreted mode uses a tree-walk interpreter to evaluate the script. The API consists of `MqlScript` and `MqlScope`s.
A script is created using `MqlScript#parse(String)`, and then evaluated using `MqlScript#evaluate(MqlScope)double`.

The simplest example of interpreting a script is as follows:
```
var script = MqlScript.parse("1.234");
var result = script.evaluate(new MqlScopeImpl());
System.out.println(result); // 1.234
```

The scope may be used to provide variables and functions to the script. `MqlMath.INSTANCE` itself is a scope, which
provides the language spec math queries. In interpreted mode, math is not available unless provided explicitly.

```
var script = MqlScript.parse("math.pi + 1");
var result = script.evaluate(new MqlScopeImpl(){{
    put("math", MqlMath.INSTANCE);
}});
System.out.println(result); // 4.141592653589793
```

`MqlForeignFunctions` provides an interface for calling Java functions from MQL. The main function is
`MqlForeignFunctions.create(Class<T>, T)MqlScope`, which creates a scope from the provided class,
exposing all `@Query` methods to the script. See the example below:

```
public class MyQuery {
    @Query
    public double add(double a, double b) {
        return a + b;
    }
}

var script = MqlScript.parse("my.add(1, 2)");
var result = script.evaluate(new MqlScopeImpl(){{
    data.put("my", MqlForeignFunctions.create(MyQuery.class, new MyQuery()));
}});
System.out.println(result); // 3.0
```

### Compiled
Compiled (JIT) mode compiles the script to JVM bytecode at runtime. Compiled mode is significantly faster than
interpreted mode (solid benchmarks wip), however it is not as flexible.

Due to Java security, the following vm argument must be provided to allow dynamic class loading:
- `--add-opens java.base/java.lang=ALL-UNNAMED`

The simple interpreted example can be rewritten as follows:
```
public interface MyScript {
    double evaluate();
}

var compiler = new MqlCompiler<>(MyScript.class);
var scriptClass = compiler.compile("1.234"); // Class<MyScript>
var script = scriptClass.newInstance(); // Deprecated and unsafe, handle errors in real usage
var result = script.evaluate();
System.out.println(result); // 1.234
```

Unlike interpreted mode, compiled mode provides math functions implicitly (for now):

```
// ...
var script = compiler.compile("math.pi + 1").newInstance();
var result = script.evaluate();
System.out.println(result); // 4.141592653589793
```

Compiled mode requires all types to be known at compile time, particularly, they must be present in the
script interface given to the compiler. The third example can be rewritten as follows:

```
public class MyQuery {
    @Query
    public double add(double a, double b) {
        return a + b;
    }
}

public interface MyScript {
    double evaluate(@MqlEnv("my") MyQuery my);
}

var compiler = new MqlCompiler<>(MyScript.class);
var script = compiler.compile("my.add(1, 2)").newInstance();
var result = script.evaluate(new MyQuery());
System.out.println(result); // 3.0
```
