# Cataclysmic-Effects

Fabric mod that adds the mob effects used in Heather events.

## Using it as a dependency

Add the Maven repository and the mod to your dependencies. The version is
`<mod version>-<Minecraft version>`.

```groovy
repositories {
    maven { url = "https://teamheather.github.io/Cataclysmic-Effects/" }
}

dependencies {
    modImplementation "team.heather.cataclysm:cataclysmic-effects:${project.cataclysmic_effects_version}"
    include "team.heather.cataclysm:cataclysmic-effects:${project.cataclysmic_effects_version}"
}
```

Use `cataclysmic_effects_version=1.0-1.21.5` in `gradle.properties`, and drop the
`include` line if your mod should not bundle this one.

The effects are available as holders in `CataclysmicMobEffects`:

```java
player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 600));
```
