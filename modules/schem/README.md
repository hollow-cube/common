> [!IMPORTANT]  
> This repository is not being updated and should not be used going forward.
>
> This module has been moved to its own repository [here](https://github.com/hollow-cube/schem).

# Schematic Loader

A simple schematic file loader.

# Install

Artifacts are published on Jitpack for now. Add the following to your `build.gradle(.kts)`:

> Note: {VERSION} should be replaced with the latest version on Jitpack, you can find
> this [here](https://jitpack.io/#hollow-cube/common).

```kotlin
dependencies {
    implementation("com.github.hollow-cube.common:schem:{VERSION}")
}
```

# Usage

The simplest usage is as follows, which will load a schematic file and place it at 0, 0, 0:

```
var schematic = SchematicReader.read(Path.of("path/to/schematic.schem"));
schem.build(Rotation.NONE, null).apply(instance, 0, 0, 0, null);
```
