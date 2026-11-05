# Cataclysmic-Effects

Fabric mod that adds the mob effects used in Heather events.

## Using it as a dependency

Add JitPack to your repositories and the mod to your dependencies. The version
is `<mod version>-<Minecraft version>`.

```groovy
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    modImplementation 'com.github.TeamHeather:CataclysmicEffects:1.0-1.21.5'
}
```

The effects are available as holders in `CataclysmicMobEffects`:

```java
player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 600));
```
