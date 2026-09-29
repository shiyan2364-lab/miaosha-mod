package com.example.mixin;

import com.example.MiaoShaMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 秒杀注入点：LivingEntity.damage(DamageSource, float)
 * 玩家攻击命中时，把目标 blood 设为极小值，让原版走完整死亡流程。
 * 掉落物 / 经验 / 击杀计数都归属攻击玩家。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    @Shadow public abstract boolean isDead();
    @Shadow public abstract void setHealth(float health);

    @Inject(
        method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
        at = @At("HEAD")
    )
    private void miaosha$instantKill(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() == null) return;
        if (!(source.getAttacker() instanceof PlayerEntity)) return;

        PlayerEntity attacker = (PlayerEntity) source.getAttacker();
        LivingEntity self = (LivingEntity) (Object) this;

        if (self.isDead()) return;
        if (self == attacker) return;

        if (self instanceof PlayerEntity && !MiaoShaMod.KILL_PLAYER) return;

        // 关键：设为极小值，不 cancel，让原版走完 damage() → die() 完整流程
        self.setHealth(0.01f);

        MiaoShaMod.LOGGER.info(
            "{} was one-shot by {}",
            self.getDisplayName().getString(),
            attacker.getDisplayName().getString()
        );
    }
}
