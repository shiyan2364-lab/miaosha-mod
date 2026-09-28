package com.example.mixin;

import com.example.MiaoShaMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.wither.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 秒杀注入点：LivingEntity.damage(DamageSource, float)
 *
 * 在原版伤害流程开始之前拦截 —— 只要伤害来自玩家，
 * 直接清空血条并 cancel 原方法。
 *
 * 关键点：不取消 die() 判定，让原版自己走死亡流程。
 * 由于 setHealth(0f) 已把血条归零，原版 die() 会正常触发：
 *   - 掉落物品 → 归属攻击玩家
 *   - 经验球   → 归属攻击玩家
 *   - 计分板击杀数 → 归属攻击玩家
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    @Shadow public abstract boolean isDead();
    @Shadow public abstract void setHealth(float health);

    @Inject(
        method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void miaosha$instantKill(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // 1. 没有攻击者（比如摔落、火烧）直接放行
        if (source.getAttacker() == null) return;

        // 2. 攻击者必须是玩家
        if (!(source.getAttacker() instanceof PlayerEntity)) return;

        PlayerEntity attacker = (PlayerEntity) source.getAttacker();
        LivingEntity self = (LivingEntity) (Object) this;

        // 3. 目标已死 / 攻击者是自己 → 跳过
        if (self.isDead()) return;
        if (self == attacker) return;

        // 4. PVP 开关：不允许秒杀其他玩家
        if (self instanceof PlayerEntity && !MiaoShaMod.KILL_PLAYER) return;

        // 5. Boss 开关：末影龙、凋灵
        if (!MiaoShaMod.KILL_BOSS) {
            if (self instanceof EnderDragonEntity) return;
            if (self instanceof WitherEntity) return;
        }

        // ===== 秒杀：清空血条并取消原伤害流程 =====
        // 使用 isDead() 检查避免与原版死亡流程冲突
        self.setHealth(0f);
        cir.setReturnValue(true); // 标记为已击倒
        cir.cancel();             // 取消原 damage() 的后续逻辑

        MiaoShaMod.LOGGER.info(
            "☠ {} 被玩家 {} 一击秒杀!",
            self.getDisplayName().getString(),
            attacker.getDisplayName().getString()
        );
    }
}
