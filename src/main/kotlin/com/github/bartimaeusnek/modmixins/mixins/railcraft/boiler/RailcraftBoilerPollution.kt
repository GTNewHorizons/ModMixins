package com.github.bartimaeusnek.modmixins.mixins.railcraft.boiler

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import mods.railcraft.common.blocks.RailcraftTileEntity
import mods.railcraft.common.blocks.machine.TileMultiBlock
import mods.railcraft.common.blocks.machine.beta.TileEngineSteamHobby
import mods.railcraft.common.util.steam.SteamBoiler
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [SteamBoiler::class], remap = false)
class RailcraftBoilerPollution {

    @Shadow
    private var tile: RailcraftTileEntity? = null

    @Shadow
    private var isBurning: Boolean = false

    @Inject(method = ["tick"], at = [At(value = "HEAD")])
    fun tick(x: Int, c: CallbackInfo) {
        if (this.isBurning)
            tile?.also {
                if (!it.world.isRemote && (it.worldObj.totalWorldTime % 20).toInt() == 0) {
                    GT_Pollution.addPollution(it.world.getChunkFromBlockCoords(it.x, it.z), when (it) {
                        is TileMultiBlock -> (it.components.size - x) * LoadingConfig.fireboxPollution
                        is TileEngineSteamHobby -> LoadingConfig.hobbyistEnginePollution
                        else -> 40
                    })
                }
            }
    }
}