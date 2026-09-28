# 秒杀模组 · 1.16.5 版

Fabric + Java 16 秒杀模组 for Minecraft 1.16.5

## 依赖
- Minecraft 1.16.5
- Fabric Loader >= 0.15.11
- Fabric API 0.42.0+1.16
- Java 16+ (推荐 JDK 17)

## 构建
```bash
./gradlew build
```
产物: `build/libs/miaosha-mod-1.0.0.jar`

## 使用
把 jar 放进 `.minecraft/mods/`，用 Fabric 启动。

## 功能
- ⚔️ 一击必杀：玩家攻击任意生物瞬间清空血条
- 🐉 Boss 也照杀：末影龙、凋灵
- 👤 PVP 秒杀：默认开启，可关
- 💎 掉落归属正常：掉落物 / 经验 / 击杀计数都归攻击玩家
- 🛡️ 防自伤：攻击自己不会触发

## 可配置
编辑 `src/main/java/com/example/MiaoShaMod.java`:
```java
public static final boolean KILL_PLAYER = true;  // PVP 秒杀开关
public static final boolean KILL_BOSS   = true;  // Boss 秒杀开关
```

## 实现原理
Mixin 注入到 `LivingEntity.damage(DamageSource, float)` HEAD:
1. 判断 `source.getAttacker() instanceof PlayerEntity`
2. 调用 `self.setHealth(0f)` 清空血条
3. `cir.setReturnValue(true); cir.cancel();` 让原版返回"已击倒"

由于血条已被清空，原版随后走正常死亡流程，掉落 / 经验 / 计分板都归攻击玩家。

## License
CC0-1.0
