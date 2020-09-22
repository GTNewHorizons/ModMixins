package com.github.bartimaeusnek.modmixins.mixins.ztones.network

import com.riciJak.Ztones.network.ToggleMetaData
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.player.EntityPlayer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [ToggleMetaData::class])
class PackageFix {

    @Inject(method = ["processData"], at = [At(value = "HEAD")], remap = false, cancellable = true)
    fun processData(entityPlayer: EntityPlayer, bbis: ByteBufInputStream, c: CallbackInfo) {
        entityPlayer.heldItem?.let {
            if (it.item?.javaClass?.name?.contains("com.riciJak.Ztones.item.block") == true){
                val incDmg = bbis.readBoolean()
                val maxDmg = 15
                var itemDmg = it.itemDamage
                itemDmg += if (incDmg) 1 else -1
                when {
                    itemDmg > maxDmg -> itemDmg = 0
                    itemDmg < 0 -> itemDmg = maxDmg
                }
                it.itemDamage = itemDmg
            }
        }
        c.cancel()
    }
}