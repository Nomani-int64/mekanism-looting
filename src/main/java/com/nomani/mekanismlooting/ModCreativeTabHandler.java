package com.nomani.mekanismlooting;

import com.nomani.mekanismlooting.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import static com.nomani.mekanismlooting.MekanismLooting.MODID;

@EventBusSubscriber(modid = MODID)
public class ModCreativeTabHandler {

    @SubscribeEvent
    public static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> parentTabKey = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
                ResourceLocation.fromNamespaceAndPath("mekanism", "mekanism"));

        if (event.getTabKey().equals(parentTabKey)) {
            event.accept(ModItems.MODULE_LOOTING.get());
            event.accept(ModItems.MODULE_SEVERING.get());
        }
    }
}
