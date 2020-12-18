package com.github.bartimaeusnek.modmixins.mixins.vanilla.command

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace
import net.minecraft.command.CommandTime
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(CommandTime::class)
open class TimeCommandGCFix {

    @Inject(method = ["setTime"], at = [At(value = "HEAD")], cancellable = true)
    protected fun setTime(p_71552_1_: ICommandSender?, p_71552_2_: Int, x :CallbackInfo) {
        MinecraftServer.getServer().worldServers.forEach {
            if (it.provider is WorldProviderSpace)
                (it.provider as WorldProviderSpace).worldTimeCommand = p_71552_2_.toLong()
            else
                it.worldTime = p_71552_2_.toLong()
        }

        x.cancel()
    }

    @Inject(method = ["addTime"], at = [At(value = "HEAD")], cancellable = true)
    protected fun addTime(p_71553_1_: ICommandSender?, p_71553_2_: Int, x :CallbackInfo) {
        MinecraftServer.getServer().worldServers.forEach {
            if (it.provider is WorldProviderSpace)
                (it.provider as WorldProviderSpace).worldTimeCommand += p_71553_2_.toLong()
            else
                it.worldTime += p_71553_2_.toLong()
        }

        x.cancel()
    }
}