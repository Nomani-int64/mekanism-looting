package com.nomani.mekanismlooting.item;

import com.nomani.mekanismlooting.MekanismLooting;
import mekanism.common.registration.impl.ModuleDeferredRegister;
import mekanism.common.registration.impl.ModuleRegistryObject;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModModules {
    public static final ModuleDeferredRegister MODULES = new ModuleDeferredRegister(MekanismLooting.MODID);

    public static final ModuleRegistryObject<ModuleDeferredRegister.SimpleEnchantmentAwareModule> LOOTING_UNIT =
            MODULES.registerEnchantBased("looting_unit", Enchantments.LOOTING, () -> ModItems.MODULE_LOOTING,
                    builder -> builder.maxStackSize(3));

    public static final ModuleRegistryObject<ModuleSeveringUnit> SEVERING_UNIT = MODULES.register( "severing_unit",
            ModuleSeveringUnit::new, () -> ModItems.MODULE_SEVERING, builder -> builder.maxStackSize(3)
    );
}
