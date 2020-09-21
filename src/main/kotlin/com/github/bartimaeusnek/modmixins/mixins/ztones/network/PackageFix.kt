package com.github.bartimaeusnek.modmixins.mixins.ztones.network

import com.riciJak.Ztones.network.ToggleMetaData
import io.netty.buffer.ByteBufInputStream
import net.minecraft.entity.player.EntityPlayer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.io.IOException

@Mixin(value = [ToggleMetaData::class])
class PackageFix {

    @Throws(IOException::class)
    @Inject(method = ["processData"], at = [At(value = "HEAD")], remap = false)
    fun processData(c: CallbackInfo, entityPlayer: EntityPlayer, bbis: ByteBufInputStream) {
        val itemStack = entityPlayer.heldItem
        if (itemStack != null && itemStack.item?.javaClass?.name?.contains("com.riciJak.Ztones.block") == true) {
            val incDmg = bbis.readBoolean()
            val maxDmg = 15
            var itemDmg = itemStack.itemDamage
            itemDmg += if (incDmg) 1 else -1
            if (itemDmg > maxDmg)
                itemDmg = 0
            else if (itemDmg < 0)
                itemDmg = maxDmg
            itemStack.itemDamage = itemDmg
        }
        c.cancel()
    }
}