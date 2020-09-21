package com.github.bartimaeusnek.modmixins.core

import net.minecraft.launchwrapper.Launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import java.io.File
import java.io.FileNotFoundException

class ModMixinsPlugin : IMixinConfigPlugin {

    companion object {
        const val name = "ModMixinsPlugin"
        val log: Logger = LogManager.getLogger(name)
        var thermosTainted : Boolean = false

        fun loadJar(jar : File?) {
            try {
                jar?.also{
                    log.info("Attempting to load $it")
                    if (!jar.exists())
                        throw FileNotFoundException()
                    ClassPreLoader.loadJar(it)
                }
            } catch (ex: Exception) {
                log.catching(ex)
            }
        }

        fun unloadJar(jar : File?) {
            try {
                jar?.also{
                    log.info("Attempting to unload $it")
                    if (!jar.exists())
                        throw FileNotFoundException()
                    ClassPreLoader.unloadJar(it)
                }
            } catch (ex: Exception) {
                log.catching(ex)
            }
        }

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
    override fun getRefMapperConfig() : String? = null

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String) : Boolean = true

    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) {}
    fun getJar(jarname: String) : File? =
         try {
            File(Launch.minecraftHome, "mods/").listFiles{ file ->
                file.nameWithoutExtension.contains(jarname) && file.extension == ("jar")
            }?.first()!!
        } catch (_: Throwable) {
            null
        }

    override fun getMixins(): List<String>? {
        val mixins: MutableList<String> = ArrayList()

        val gtjar = getJar("gregtech-5.09")

        loadJar(gtjar)

        MixinSets.values()
                .filter(MixinSets::shouldBeLoaded)
                .forEach {
                    it.loadJar()
                    mixins.addAll(listOf(*it.mixinClasses))
                    log.info("Loading modmixins plugin ${it.fixname} with mixins: {}", it.mixinClasses)
                    it.unloadJar()
                }
        unloadJar(gtjar)
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
                "Railcraft Pollution Fix",
                { LoadingConfig.fixRailcraftBoilerPollution },
                File(Launch.minecraftHome, "mods/${LoadingConfig.RailcraftJarName}"),
                arrayOf(
                    "railcraft.boiler.RailcraftBoilerPollution",
                    "railcraft.tileentity.MultiOfenPollution",
                    "railcraft.entity.RailcraftTunnleBorePollution"
                )
        ),
        ROCKET_ADD_POLLUTION (
                "Rocket Pollution Fix",
                { LoadingConfig.fixRocketPollution },
                File(Launch.minecraftHome, "mods/${LoadingConfig.GalacticraftJarName}"),
                arrayOf(
                        "galacticraft.entity.RocketPollutionAdder"
                )
        ),
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
        ),
        EXPLOSION_POLLUTION_ADDITION(
                "Explosion Pollution Fix",
                { LoadingConfig.fixExplosionPollution },
                arrayOf(
                        "vanilla.world.ExplosionPollutionAdder"
                )
        ),
        ZTONES_PACKAGE_FIX(
        "Ztones Network Vulnerability",
                { LoadingConfig.fixZtonesNetworkVulnerability },
                File(Launch.minecraftHome, "mods/${LoadingConfig.ZtonesJarName}"),
                arrayOf(
                    "ztones.network"
                )
        )
        ;
        constructor(fixname: String, applyIf: () -> Boolean, mixinClasses : Array<String>) : this(fixname, applyIf,null, mixinClasses)
        constructor(fixname: String, applyIf: () -> Boolean, jar: File?, mixinClass : String) : this(fixname, applyIf, jar, arrayOf(mixinClass))
        constructor(fixname: String, applyIf: () -> Boolean, mixinClass : String) : this(fixname, applyIf,null, mixinClass)

        fun shouldBeLoaded() : Boolean = applyIf()
        fun loadJar() = loadJar(jar)
        fun unloadJar() = unloadJar(jar)
    }

}