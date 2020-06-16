package com.github.bartimaeusnek.modmixins.mixins.vanilla.world

import gregtech.common.GT_Pollution
import net.minecraft.world.Explosion
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import kotlin.math.ceil

@Mixin(Explosion::class)
class ExplosionPollutionAdder {

    @Shadow
    var explosionSize = 0f

    @Shadow
    private val worldObj: World? = null

    @Shadow
    var explosionX = 0.0

    @Shadow
    var explosionZ = 0.0

    @Inject(method = ["doExplosionA"], at = [At(value = "TAIL")])
    fun addExplosionPollution(x: CallbackInfo) {
        if (!this.worldObj!!.isRemote)
            GT_Pollution.addPollution(this.worldObj.getChunkFromBlockCoords(this.explosionX.toInt(), this.explosionZ.toInt()), ceil(explosionSize * 333.334f).toInt())
    }
}