package com.github.bartimaeusnek.modmixins.mixins.railcraft.tileentity

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import mods.railcraft.common.blocks.machine.MultiBlockPattern
import mods.railcraft.common.blocks.machine.TileMultiBlock
import mods.railcraft.common.blocks.machine.TileMultiBlockOven
import mods.railcraft.common.blocks.machine.alpha.TileBlastFurnace
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TileMultiBlockOven::class)
abstract class MultiOfenPollution(patterns: MutableList<MultiBlockPattern>) : TileMultiBlock(patterns) {

    @Shadow(remap = false)
    var cooking: Boolean = false

    @Inject(method = ["updateEntity"], at = [At("HEAD")])
    fun addPollution(c: CallbackInfo) {
        if (!this.worldObj.isRemote && this.cooking && this.isMaster){
            GT_Pollution.addPollution(this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord), when (this) {
                is TileBlastFurnace -> LoadingConfig.furnacePollution * 4
                else -> LoadingConfig.furnacePollution
            })
        }
    }
}