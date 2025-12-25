package com.example.waystoneinjectorserver;

import com.example.waystoneinjectorserver.config.ServerConfig;
import com.example.waystoneinjectorserver.network.NetworkHandler;
import com.example.waystoneinjectorserver.vault.VaultCommands;
import com.example.waystoneinjectorserver.vault.VaultEvents;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(WaystoneInjectorServerMod.MODID)
public class WaystoneInjectorServerMod {

    public static final String MODID = "waystoneinjector_server";

    public static final Logger LOGGER = LogUtils.getLogger();

    public WaystoneInjectorServerMod() {
        // Touch the mod event bus so Forge initializes the mod loading context.
        FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        NetworkHandler.init();

        // Capability attach + clone events are Forge bus.
        MinecraftForge.EVENT_BUS.register(VaultEvents.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        VaultCommands.register(event.getDispatcher());
    }
}
