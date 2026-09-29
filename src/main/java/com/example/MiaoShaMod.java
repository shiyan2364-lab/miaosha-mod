package com.example;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 秒杀模组 - Minecraft 1.16.5 Fabric
 */
public class MiaoShaMod implements ModInitializer {

    public static final String MOD_ID = "miaosha-mod";
    public static final Logger LOGGER = LogManager.getLogger("miaosha-mod");

    /** 是否允许秒杀其他玩家（PVP 秒杀） */
    public static final boolean KILL_PLAYER = true;

    @Override
    public void onInitialize() {
        LOGGER.info("MiaoSha Mod loaded [MC 1.16.5] - player attacks will one-shot kill!");
    }
}
