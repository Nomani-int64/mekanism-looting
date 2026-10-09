package com.nomani.mekanismlooting.item;

import com.nomani.mekanismlooting.MekanismLooting;
import mekanism.common.item.ItemModule;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismCreativeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class ModItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MekanismLooting.MODID);

    public static final ItemRegistryObject<ItemModule> MODULE_LOOTING = ITEMS.registerModule(ModModules.LOOTING_UNIT, Rarity.UNCOMMON);
    public static final ItemRegistryObject<ItemModule> MODULE_SEVERING = ITEMS.registerModule(ModModules.SEVERING_UNIT, Rarity.RARE);

}
