package com.github.bartimaeusnek.modmixins.mixins.ic2.tileentity

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import ic2.core.block.machine.tileentity.TileEntityIronFurnace
import net.minecraft.tileentity.TileEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TileEntityIronFurnace::class, remap = false)
class IronFurnacePollution : TileEntity() {

    @Shadow
    fun isBurning(): Boolean = true

    @Inject(method = ["updateEntityServer"], at = [At(value = "TAIL")])
    fun updateEntityServer(c: CallbackInfo) {
        if (this.isBurning()) {
            GT_Pollution.addPollution(this.worldObj!!.getChunkFromBlockCoords(this.xCoord, this.zCoord), LoadingConfig.furnacePullution)
        }
    }
}