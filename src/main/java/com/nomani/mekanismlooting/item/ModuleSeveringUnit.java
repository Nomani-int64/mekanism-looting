package com.nomani.mekanismlooting.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IModule;
import mekanism.common.content.gear.ModuleHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import static com.nomani.mekanismlooting.MekanismLooting.MODID;
import static net.minecraft.util.Mth.floor;

@EventBusSubscriber(modid = MODID)
public class ModuleSeveringUnit implements ICustomModule<ModuleSeveringUnit> {

    public ModuleSeveringUnit(IModule<ModuleSeveringUnit> module) {
        // does nothing
    }

    private static ItemStack getPlayerHead(GameProfile profile, int count) {
        ItemStack playerHeadStack = new ItemStack(Items.PLAYER_HEAD, count);
        playerHeadStack.set(DataComponents.PROFILE, new ResolvableProfile(profile)); // very cursed but works perfectly
        return playerHeadStack;
    }

    private static int getRandom(float value, float levelRandom, boolean overflow) {
        // Turns a float value into an integer random value of [floor(value), ceil(value)]
        // If overflow == false, the bounds will be capped to 1
        if (value >= 1.0f && !overflow) return 1;
        return floor(value) + ((value - floor(value)) > levelRandom? 1 : 0);
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        var source = event.getSource();
        var target = event.getEntity();
        if (source == null || target == null) return;
        var attacker = source.getEntity();
        if (!(attacker instanceof Player player)) return;

        ItemStack stack = player.getMainHandItem(); // when attacking, weapon is always main handed

        if (ModuleHelper.INSTANCE.isEnabled(stack, ModModules.SEVERING_UNIT)) {
            var module = ModuleHelper.INSTANCE.getModule(stack, ModModules.SEVERING_UNIT);
            if (module != null && module.isEnabled()) {
                Item extraDrop = Items.AIR;
                float extraChanceBase = 0.0f;
                boolean extraChanceOverflow = false;
                EntityType<?> targetType = target.getType();
                if (target instanceof Player) {
                    // Drops a skull with player profile with 100% chance
                    // How to test this out in Single Player: /damage yourself by yourself
                    GameProfile profile = ((Player) target).getGameProfile();
                    event.getDrops().add(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(),
                            getPlayerHead(profile, 1)));
                }
                else {
                    // Skulls
                    if (targetType == EntityType.ZOMBIE) {
                        extraDrop = Items.ZOMBIE_HEAD;
                        extraChanceBase = 0.06f;
                    }
                    else if (targetType == EntityType.SKELETON) {
                        extraDrop = Items.SKELETON_SKULL;
                        extraChanceBase = 0.06f;
                    }
                    else if (targetType == EntityType.CREEPER) {
                        extraDrop = Items.CREEPER_HEAD;
                        if (((Creeper) target).isPowered()) extraChanceBase = 1.0f; // Charged creeper always drops head
                        else extraChanceBase = 0.06f;
                    }
                    else if (targetType == EntityType.PIGLIN) {
                        extraDrop = Items.PIGLIN_HEAD;
                        extraChanceBase = 0.06f;
                    }
                    else if (targetType == EntityType.WITHER_SKELETON) {
                        // Does not add an extra drop here because
                        // 1. Increased w.skeleton head will lead to increased nether star, and
                        // 2. Might cause a w.skeleton to drop 2 heads, which is obviously unacceptable
                        // 3. We already had a looting module (?)
                    }
                    else if (targetType == EntityType.ENDER_DRAGON) {
                        extraDrop = Items.DRAGON_HEAD;
                        extraChanceBase = 1.0f; // Always drops dragon head, you'll need it ;)
                    }
                    // Slime-like. Size does not affect drops, meaning that bigger ones can drop items too
                    else if (targetType == EntityType.SLIME) {
                        extraDrop = Items.SLIME_BALL;
                        extraChanceBase = 0.25f;
                    }
                    else if (targetType == EntityType.MAGMA_CUBE) {
                        extraDrop = Items.MAGMA_CREAM;
                        extraChanceBase = 0.25f;
                    }
                    // Other bodily products
                    else if (targetType == EntityType.CHICKEN || targetType == EntityType.PARROT) {
                        extraDrop = Items.FEATHER;
                        extraChanceBase = 0.5f;
                        extraChanceOverflow = true;
                    }
                    else if (targetType == EntityType.COW || targetType == EntityType.MOOSHROOM || targetType == EntityType.HORSE
                          || targetType == EntityType.DONKEY || targetType == EntityType.MULE || targetType == EntityType.LLAMA
                          || targetType == EntityType.HOGLIN || targetType == EntityType.TRADER_LLAMA) {
                        extraDrop = Items.LEATHER;
                        extraChanceBase = 0.5f;
                        extraChanceOverflow = true;
                    }
                    else if (targetType == EntityType.RABBIT) {
                        extraDrop = Items.RABBIT_FOOT;
                        extraChanceBase = 0.1f;
                    }
                    else if (targetType == EntityType.SALMON || targetType == EntityType.COD || targetType == EntityType.PUFFERFISH
                          || targetType == EntityType.TROPICAL_FISH) {
                        extraDrop = Items.BONE_MEAL;
                        extraChanceBase = 0.1f;
                    }
                    else if (targetType == EntityType.BLAZE) {
                        extraDrop = Items.BLAZE_ROD;
                        extraChanceBase = 0.25f;
                    }
                    else if (targetType == EntityType.BREEZE) {
                        extraDrop = Items.BREEZE_ROD;
                        extraChanceBase = 0.25f; // Breezes have dropped lots of rods already... Why not EVEN more!?
                    }
                    else if (targetType == EntityType.GHAST) {
                        extraDrop = Items.GHAST_TEAR;
                        extraChanceBase = 0.25f;
                    }
                    else if (targetType == EntityType.PHANTOM) {
                        extraDrop = Items.PHANTOM_MEMBRANE;
                        extraChanceBase = 0.25f;
                    }
                    else if (targetType == EntityType.SPIDER || targetType == EntityType.CAVE_SPIDER) {
                        extraDrop = Items.SPIDER_EYE;
                        extraChanceBase = 0.25f;
                    }
                    else if (targetType == EntityType.ENDERMAN) {
                        extraDrop = Items.ENDER_PEARL;
                        extraChanceBase = 0.25f;
                    }
                    int dropCount = getRandom(extraChanceBase * module.getInstalledCount(),
                            attacker.level().random.nextFloat(), extraChanceOverflow);
                    if (dropCount >= 1)
                        event.getDrops().add(new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(),
                                new ItemStack(extraDrop, dropCount)));
                }
            }
        }
    }
}
