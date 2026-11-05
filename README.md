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

Use `cataclysmic_effects_version=1.1.1-1.21.5` in `gradle.properties`, and drop the
`include` line if your mod should not bundle this one.

The effects are available as holders in `CataclysmicMobEffects`:

```java
player.addEffect(new MobEffectInstance(CataclysmicMobEffects.MORTEM, 600));
```

## Callbacks

`team.heather.cataclysm.effect.api.callback` holds Fabric events for reacting to
effects. They run on the server.

```java
MobEffectAddedCallback.EVENT.register((entity, instance, source) ->
        LOGGER.info("{} received {}", entity, instance));
```

`AllowMobEffectCallback`, `MobEffectAddedCallback`, `MobEffectUpdatedCallback`,
`MobEffectRemovedCallback` and `MobEffectTickCallback` listen to every effect.
Use `only` to listen to a single one:

```java
MobEffectRemovedCallback.EVENT.register(MobEffectRemovedCallback.only(CataclysmicMobEffects.MORTEM, (entity, instance, expired) -> {
    // Mortem ended
}));
```

The remaining callbacks belong to a specific effect:

| Effect | Callbacks |
| --- | --- |
| Mortem | `AllowMortemTotemBlockCallback`, `MortemTotemBlockedCallback` |
| Pale Corrosion | `AllowPaleCorrosionCallback`, `PaleCorrosionHeartDisabledCallback`, `PaleCorrosionHeartsRestoredCallback` |
| Vulnerability | `ModifyVulnerabilityDamageCallback` |
| Disper | `AllowDisperCallback`, `DisperBlockDestroyedCallback` |
