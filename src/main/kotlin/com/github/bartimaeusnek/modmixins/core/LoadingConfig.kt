package com.github.bartimaeusnek.modmixins.core

import net.minecraftforge.common.config.Configuration
import java.io.File

object LoadingConfig {

    fun loadConfig(file: File) {
        config = Configuration(file)
        fixRailcraftBoilerPollution = config["fixes", "fixRailcraftPollution", true, "Adds Pollution to the Railcraft Boilers, Machines, etc."].boolean
        fixVanillaFurnacePollution = config["fixes", "fixVanillaFurnacePollution", true, "Adds Pollution to the IC2 and Vanilla Furnaces"].boolean
        fixThaumcraftFurnacePollution = config["fixes", "fixThaumcraftFurnacePollution", true, "Adds Pollution to the Thaumcraft Furnaces"].boolean
        fixExplosionPollution = config["fixes", "fixExplosionPollution", true, "Adds Pollution to every Explosion"].boolean
        fixRocketPollution = config["fixes", "fixRocketPollution", true, "Adds Pollution to every rocket start"].boolean
        fixZtonesNetworkVulnerability = config["fixes", "fixZtonesNetworkVulnerability", true, "Fixes Ztones Network Vulnerability"].boolean

        RailcraftJarName = config["jars", "Railcraft Jar Name","Railcraft_1.7.10-9.12.2.1.jar", "Name of the Railcraft Jar"].string
        GalacticraftJarName = config["jars", "Galacticraft Jar Name","Galacticraft-1.7-3.0.14-GTNH-full.jar", "Name of the Galacticraft Jar"].string
        ZtonesJarName = config["jars", "Ztones Jar Name","Ztones-1.7.10-2.2.2.jar", "Name of the Ztones Jar"].string

        rocketPollution = config["options","rocketPollution", 10000, "Pollution when starting per tick, min 1!", 1 , Int.MAX_VALUE].int
        furnacePollution = config["options","furnacePollution", 1, "Pollution per tick, min 1!", 1 , Int.MAX_VALUE].int
        explosionPollution = config["options","explosionPollution", 333.34, "Explosion one time pollution"].double.toFloat()

        if (config.hasChanged())
            config.save()
    }

    private lateinit var config: Configuration
    var fixRocketPollution : Boolean = false
    var fixRailcraftBoilerPollution : Boolean = false
    var fixVanillaFurnacePollution : Boolean = false
    var fixThaumcraftFurnacePollution : Boolean = false
    var fixExplosionPollution : Boolean = false
    var fixZtonesNetworkVulnerability : Boolean = false
    var furnacePollution : Int = 1
    var rocketPollution : Int = 1
    var explosionPollution : Float = 333.34f
    lateinit var RailcraftJarName : String
    lateinit var GalacticraftJarName : String
    lateinit var ZtonesJarName : String
}