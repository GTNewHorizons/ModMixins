package com.github.bartimaeusnek.modmixins.mixins.railcraft.entity

import gregtech.common.GT_Pollution
import mods.railcraft.common.carts.EntityTunnelBore
import net.minecraft.entity.item.EntityMinecart
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(EntityTunnelBore::class)
abstract class RailcraftTunnleBorePollution(world: World?) : EntityMinecart(world) {

    @Shadow
    var active: Boolean = false

    @Inject(method = ["onUpdate"], at = [At("HEAD")])
    fun addPollution(c: CallbackInfo) {
        if (!this.worldObj.isRemote && active) {
            GT_Pollution.addPollution(worldObj.getChunkFromBlockCoords(posX.toInt(), posZ.toInt()), 2)
        }
    }
}