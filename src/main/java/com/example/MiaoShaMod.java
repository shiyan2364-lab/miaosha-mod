package com.example;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 秒杀模组 - Minecraft 1.16.5 Fabric
 *
 * 核心实现：使用 Mixin 注入到 LivingEntity.damage()，
 * 玩家攻击命中后直接清空血条，触发完整死亡流程
 * （掉落物 / 经验 / 计分板都会正确归属到攻击玩家）。
 *
 * 详见: com.example.mixin.LivingEntityDamageMixin
 */
public class MiaoShaMod implements ModInitializer {

    public static final String MOD_ID = "miaosha-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // ===== 可配置开关（改这里的常量再重新编译） =====

    /** 是否允许秒杀其他玩家（PVP 秒杀） */
    public static final boolean KILL_PLAYER = true;

    /** 是否允许秒杀 Boss（末影龙、凋灵等） */
    public static final boolean KILL_BOSS = true;

    @Override
    public void onInitialize() {
        // 逻辑全部在 Mixin 里，见 LivingEntityDamageMixin
        LOGGER.info("💀 秒杀模组已加载 [MC 1.16.5] —— 玩家攻击将一击必杀!");
        LOGGER.info("   PVP 秒杀: {}, Boss 秒杀: {}", KILL_PLAYER, KILL_BOSS);
    }
}
