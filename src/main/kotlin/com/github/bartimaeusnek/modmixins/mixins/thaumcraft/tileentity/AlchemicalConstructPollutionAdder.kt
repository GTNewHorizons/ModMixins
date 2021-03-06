package com.github.bartimaeusnek.modmixins.mixins.thaumcraft.tileentity

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import net.minecraft.tileentity.TileEntity
import org.spongepowered.asm.lib.Opcodes
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import thaumcraft.common.tiles.TileAlchemyFurnace

@Mixin(value = [TileAlchemyFurnace::class])
class AlchemicalConstructPollutionAdder : TileEntity() {

    @Inject(method = ["updateEntity"], at = [At(value = "FIELD", target = "thaumcraft/common/tiles/TileAlchemyFurnace.furnaceBurnTime:I", opcode = Opcodes.PUTFIELD, remap = false)])
    fun addPollution(c: CallbackInfo) {
        if (!this.worldObj.isRemote && (this.worldObj.totalWorldTime % 20).toInt() == 0)
            GT_Pollution.addPollution(this.worldObj!!.getChunkFromBlockCoords(this.xCoord, this.zCoord), LoadingConfig.furnacePollution)
    }


}