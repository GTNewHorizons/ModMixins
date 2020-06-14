package com.github.bartimaeusnek.modmixins.main

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.MODID
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.NAME
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.VERSION
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.util.GT_Utility
import ic2.api.item.IC2Items
import mods.railcraft.common.blocks.RailcraftBlocks
import mods.railcraft.common.blocks.machine.beta.EnumMachineBeta
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import thaumcraft.api.ItemApi
import thaumcraft.api.ThaumcraftApi


@Mod(modid = MODID, version = VERSION, name = NAME, acceptableRemoteVersions = "*",
        dependencies = "required-after:spongemixins@[1.1.0; required-after: forgelin;", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object ModMixinsMod {
    const val MODID = "ModMixins"
    const val VERSION = "0.0.1"
    const val NAME = "ModMixins"

    @Mod.EventHandler
    fun preinit(init : FMLPreInitializationEvent) {
        if (init.side.isClient){
            MinecraftForge.EVENT_BUS.register(TooltipEventHandler)
        }
    }

    @SideOnly(Side.CLIENT)
    object TooltipEventHandler {

        @SideOnly(Side.CLIENT)
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun getTooltip(event : ItemTooltipEvent?)
        {
            event?.itemStack?.also{
                if (LoadingConfig.fixVanillaFurnacePollution){
                    when {
                        GT_Utility.areStacksEqual(event.itemStack, ItemStack(Blocks.furnace)) -> {
                            event.toolTip.add("Produces ${LoadingConfig.furnacePullution*20} Pollution/Second")
                        }
                        GT_Utility.areStacksEqual(event.itemStack, IC2Items.getItem("ironFurnace")) -> {
                            event.toolTip.add("Produces ${LoadingConfig.furnacePullution*20} Pollution/Second")
                        }
                    }
                    if (Loader.isModLoaded("thaumcraft"))
                        if (GT_Utility.areStacksEqual(event.itemStack, ItemApi.getBlock("blockArcaneFurnace",0)))
                            event.toolTip.add("Produces ${LoadingConfig.furnacePullution*20} Pollution/Second")
                }
                if (LoadingConfig.fixRailcraftBoilerPollution && Loader.isModLoaded("Railcraft")) {
                    when {
                        GT_Utility.areStacksEqual(event.itemStack, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.BOILER_FIREBOX_SOLID.ordinal)) -> {
                            event.toolTip.add("Produces 40 Pollution/Second")
                        }
                        GT_Utility.areStacksEqual(event.itemStack, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.BOILER_FIREBOX_FLUID.ordinal)) -> {
                            event.toolTip.add("Produces 40 Pollution/Second")
                        }
                        GT_Utility.areStacksEqual(event.itemStack, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.ENGINE_STEAM_HOBBY.ordinal)) -> {
                            event.toolTip.add("Produces 20 Pollution/Second")
                        }
                    }
                }
            }
        }
    }

}