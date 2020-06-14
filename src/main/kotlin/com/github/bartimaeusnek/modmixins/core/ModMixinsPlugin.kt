package com.github.bartimaeusnek.modmixins.core

import net.minecraft.launchwrapper.Launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import java.io.File
import kotlin.collections.ArrayList

class ModMixinsPlugin : IMixinConfigPlugin {

    companion object {
        const val name = "ModMixinsPlugin"
        val log: Logger = LogManager.getLogger(name)
        var thermosTainted : Boolean = false
    }

    init {
        LoadingConfig.loadConfig(File(Launch.minecraftHome, "config/${name}.cfg"))
        try {
            Class.forName("org.bukkit.World")
            thermosTainted = true
            log.warn("Thermos/Bukkit detected; This is an unsupported configuration -- Things may not function properly.")
        } catch (e: ClassNotFoundException) {
            thermosTainted = false
            log.info("Thermos/Bukkit not detected")
        }
    }

    override fun onLoad(mixinPackage: String) {}
    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {}
    override fun getMixins(): List<String>? {
        val mixins: MutableList<String> = ArrayList()
        MixinSets.values()
                .filter(MixinSets::shouldBeLoaded)
                .forEach {
                    it.loadJar()
                    mixins.addAll(listOf(*it.mixinClasses))
                    log.info("Loading modmixins plugin ${it.fixname} with mixins: {}", it.mixinClasses)
                    it.unloadJar()
                }
        return mixins
    }

    override fun preApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) {}
    override fun postApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) {}

    /**
     * @param fixname = name of the fix, i.e. My Awesome addition
     * @param applyIf = condition to apply this fix, i.e. ThermosLoaded = false && config == true
     * @param jar = the jar of the mod if the jar doesn't contain a core-mod
     * @param mixinClasses = the mixins classes to be applied for this patch
     */
    enum class MixinSets(val fixname: String, private val applyIf: () -> Boolean, private val jar: File?, val mixinClasses: Array<String>)
    {
        RAILCRAFT_BOILER_POLLUTION_FIX (
                "Railcraft Boiler Pollution addition",
                { LoadingConfig.fixRailcraftBoilerPollution },
                File(Launch.minecraftHome, "mods/${LoadingConfig.RailcraftJarName}"),
                "railcraft.boiler.RailcraftBuilderPollution"),
        FURNACE_ADD_POLLUTION (
                "Furnace Pollution Fix",
                { LoadingConfig.fixVanillaFurnacePollution },
                arrayOf(
                        "vanilla.tileentity.TileEntityFurnacePollution",
                        "ic2.tileentity.IronFurnacePollution"
                )
        ),
        TC_FURNACE_ADD_POLLUTION (
                "Thaumcraft Furnace Pollution Fix",
                { LoadingConfig.fixThaumcraftFurnacePollution },
                arrayOf(
                        "thaumcraft.tileentity.AlchemicalConstructPollutionAdder"
                )
        );
        constructor(fixname: String, applyIf: () -> Boolean, mixinClasses : Array<String>) : this(fixname, applyIf,null, mixinClasses)
        constructor(fixname: String, applyIf: () -> Boolean, jar: File?, mixinClasses : String) : this(fixname, applyIf, jar, arrayOf(mixinClasses))
        constructor(fixname: String, applyIf: () -> Boolean, mixinClasses : String) : this(fixname, applyIf,null, mixinClasses)

        fun shouldBeLoaded() : Boolean {
            return applyIf.invoke()
        }

        fun loadJar() {
            try {
                jar?.also{
                    log.info("Attempting to load $it")
                    ClassPreLoader.loadJar(it)
                }
            } catch (ignored: Exception) {
            }
        }

        fun unloadJar() {
            try {
                jar?.also{
                    log.info("Attempting to unload $it")
                    ClassPreLoader.unloadJar(it)
                }
            } catch (ignored: Exception) {
            }
        }
    }

}