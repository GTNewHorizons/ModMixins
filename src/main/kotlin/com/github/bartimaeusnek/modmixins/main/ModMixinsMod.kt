package com.github.bartimaeusnek.modmixins.main

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.DEPENDENCIES
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.MODID
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.MODLANGUAGEADAPTER
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.NAME
import com.github.bartimaeusnek.modmixins.main.ModMixinsMod.VERSION
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import gregtech.api.util.GT_ModHandler
import gregtech.api.util.GT_Utility
import ic2.api.item.IC2Items
import mods.railcraft.common.blocks.RailcraftBlocks
import mods.railcraft.common.blocks.machine.alpha.EnumMachineAlpha
import mods.railcraft.common.blocks.machine.beta.EnumMachineBeta
import mods.railcraft.common.carts.EnumCart
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import thaumcraft.api.ItemApi
import java.text.NumberFormat

@Suppress("UNUSED")
@Mod(modid = MODID, version = VERSION, name = NAME, dependencies = DEPENDENCIES,
     acceptableRemoteVersions = "*", modLanguageAdapter = MODLANGUAGEADAPTER)
object ModMixinsMod {
    const val MODID = "ModMixins"
    const val VERSION = "0.0.1"
    const val NAME = MODID
    const val MODLANGUAGEADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
    const val DEPENDENCIES = "required-after:spongemixins;" +
                                    "required-after:forgelin;" +
                                    "required-after:gregtech;"


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
                    val furnacePollution = "Produces ${LoadingConfig.furnacePollution*20} Pollution/Second"
                    when {
                        GT_Utility.areStacksEqual(it, ItemStack(Blocks.furnace)) -> {
                            event.toolTip += furnacePollution
                        }
                        GT_Utility.areStacksEqual(it, IC2Items.getItem("ironFurnace")) -> {
                            event.toolTip += furnacePollution
                        }
                    }
                    if (Loader.isModLoaded("Thaumcraft"))
                        if (GT_Utility.areStacksEqual(it, ItemApi.getBlock("blockStoneDevice",0)))
                            event.toolTip += furnacePollution
                    if (Loader.isModLoaded("thaumicbases"))
                        if (GT_Utility.areStacksEqual(it, GT_ModHandler.getModItem("thaumicbases","advAlchFurnace",1,0)))
                            event.toolTip += furnacePollution
                }

                if (LoadingConfig.fixRailcraftBoilerPollution && Loader.isModLoaded("Railcraft")) {
                    val multi = "A complete Multiblock "
                    val boilerPollution = "Produces 15 Pollution/Second per firebox"
                    val steamEnginePollution = "Produces 20 Pollution/Second"
                    val blastFurnacePollution = multi+"produces ${LoadingConfig.furnacePollution * 4 * 20} Pollution/Second"
                    val cokeOfenPollution = multi+"produces 3 Pollution/Second"
                    when {
                        GT_Utility.areStacksEqual(it, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.BOILER_FIREBOX_SOLID.ordinal)) -> {
                            event.toolTip += boilerPollution
                        }
                        GT_Utility.areStacksEqual(it, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.BOILER_FIREBOX_FLUID.ordinal)) -> {
                            event.toolTip += boilerPollution
                        }
                        GT_Utility.areStacksEqual(it, ItemStack(RailcraftBlocks.getBlockMachineAlpha(), 1, EnumMachineAlpha.COKE_OVEN.ordinal)) -> {
                            event.toolTip += cokeOfenPollution
                        }
                        GT_Utility.areStacksEqual(it, ItemStack(RailcraftBlocks.getBlockMachineAlpha(), 1, EnumMachineAlpha.BLAST_FURNACE.ordinal)) -> {
                            event.toolTip += blastFurnacePollution
                        }
                        GT_Utility.areStacksEqual(it, EnumCart.BORE.cartItem) -> {
                            event.toolTip += boilerPollution
                        }
                        GT_Utility.areStacksEqual(it, ItemStack(RailcraftBlocks.getBlockMachineBeta(), 1, EnumMachineBeta.ENGINE_STEAM_HOBBY.ordinal)) -> {
                            event.toolTip += steamEnginePollution
                        }
                    }
                }

                if (LoadingConfig.fixRocketPollution && Loader.isModLoaded("GalacticraftCore")) {
                    it.item::class.simpleName?.also { spln ->
                        if (spln.contains("Rocket")){
                            spln.find{d -> d.isDigit()}?.also { num ->
                                event.toolTip += "Produces ${NumberFormat.getNumberInstance().format((LoadingConfig.rocketPollution shl Character.getNumericValue(num)) / 100)} Pollution/Second when ignited"
                                event.toolTip += "Produces ${NumberFormat.getNumberInstance().format((LoadingConfig.rocketPollution shl Character.getNumericValue(num)))} Pollution/Second when flying"
                            }
                        }
                    }
                }
            }
        }
    }
}