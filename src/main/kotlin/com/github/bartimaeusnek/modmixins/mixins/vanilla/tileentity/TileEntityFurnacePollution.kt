package com.github.bartimaeusnek.modmixins.mixins.vanilla.tileentity

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityFurnace
import org.spongepowered.asm.lib.Opcodes.PUTFIELD
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [TileEntityFurnace::class])
class TileEntityFurnacePollution : TileEntity() {

    @Inject(method = ["updateEntity"], at = [At(value = "FIELD", target = "net/minecraft/tileentity/TileEntityFurnace.furnaceBurnTime:I", opcode = PUTFIELD)])
    fun addPollution(c: CallbackInfo){
        GT_Pollution.addPollution(this.worldObj!!.getChunkFromBlockCoords(this.xCoord, this.zCoord), LoadingConfig.furnacePullution)
    }
}