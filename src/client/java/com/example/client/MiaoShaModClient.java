package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端入口 —— 1.16.5
 * 客户端无需额外逻辑，特效（若有）以后再在此处注册。
 */
public class MiaoShaModClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("miaosha-mod-client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("MiaoSha Mod Client 初始化完成");
    }
}
