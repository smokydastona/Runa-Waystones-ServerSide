package com.example.waystoneinjectorserver;

import com.example.waystoneinjectorserver.vault.VaultCommands;
import com.example.waystoneinjectorserver.vault.VaultEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WaystoneInjectorServerMod.MODID)
public class WaystoneInjectorServerMod {

    public static final String MODID = "waystoneinjector_server";

    public WaystoneInjectorServerMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Capability attach + clone events are Forge bus.
        MinecraftForge.EVENT_BUS.register(VaultEvents.class);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        VaultCommands.register(event.getDispatcher());
    }
}
