> [!IMPORTANT]  
> This repository is not being updated and should not be used going forward.
>
> This module has been moved to its own repository [here](https://github.com/hollow-cube/mql).

# Minecraft Query Language (mql)

A subset of MoLang (may eventually be a full implementation). Available as an interpreter or a JIT compiled mode.

## Background

## Install

Artifacts are published on Jitpack for now. Add the following to your `build.gradle(.kts)`:

> Note: {VERSION} should be replaced with the latest version on Jitpack, you can find
> this [here](https://jitpack.io/#hollow-cube/common).

```kotlin
dependencies {
    implementation("com.github.hollow-cube.common:mql:{VERSION}")
}
```

## Syntax

`mql` supports the following syntax

* Query functions
* Math & Comparison operators (`+`, `*`, `==`, etc)

## Usage

See the [docs](https://github.com/hollow-cube/common/wiki/Basic-Usage).

## Future Plans

* Unify the interpreter and compiler apis
    * Allows for fallback if using unsupported JIT features, permission issues, etc.
* Temp variables
* Public variables/querying other scripts
* Other data types & functions

## Contributing

Issues and PRs are welcome! Please refer to [CONTRIBUTING.md](../../CONTRIBUTING.md) for more information.

## License

This project is licensed under the [MIT License](../../LICENSE).
