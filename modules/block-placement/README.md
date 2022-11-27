# Placement Rules
A (wip) set of block placement rules for Minestom based on [Moulberry's PR](https://github.com/Minestom/Minestom/pull/554) to Minestom.

> ⚠️ **Requires using the [HollowCube Minestom fork](https://github.com/hollow-cube/Minestom)**

Minestom has not yet implemented the required behavior (click location) to implement these rules, nor accepted a PR 
which implements it and/or these rules. Once that happens, this module will no longer be required.

## Install

Artifacts are published on Jitpack for now. Add the following to your `build.gradle(.kts)`:

> Note: {VERSION} should be replaced with the latest version on Jitpack, you can find
> this [here](https://jitpack.io/#hollow-cube/common).

```kotlin
dependencies {
    implementation("com.github.hollow-cube.common:block-placement:{VERSION}")
}
```

## Usage

To use, simply call `HCPlacementRules.init()` during server initialization.

## Contributing

Issues and PRs are welcome! Please refer to [CONTRIBUTING.md](../../CONTRIBUTING.md) for more information.

## License

This project is licensed under the [MIT License](../../LICENSE).
