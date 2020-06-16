package com.github.bartimaeusnek.modmixins.mixins.galacticraft.entity

import com.github.bartimaeusnek.modmixins.core.LoadingConfig
import gregtech.common.GT_Pollution
import micdoodle8.mods.galacticraft.api.entity.IRocketType
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(EntityTieredRocket::class)
abstract class RocketPollutionAdder(world: World) : EntityAutoRocket(world), IRocketType {

    @Inject(method = ["onUpdate"],at =  [At("HEAD")])
    fun addRocketStartPollution(x: CallbackInfo) {
        if (!this.worldObj.isRemote){
             GT_Pollution.addPollution(this.worldObj.getChunkFromBlockCoords(this.posX.toInt(),this.posZ.toInt()),
                     when (this.launchPhase){
                         EnumLaunchPhase.LAUNCHED.ordinal -> (LoadingConfig.rocketPollution shl this.rocketTier) / 20
                         EnumLaunchPhase.IGNITED.ordinal -> (LoadingConfig.rocketPollution shl this.rocketTier) / 20 / 100
                         else -> 0
                     }

             )
        }
    }
}