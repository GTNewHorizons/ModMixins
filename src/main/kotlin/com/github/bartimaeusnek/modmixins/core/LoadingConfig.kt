package com.github.bartimaeusnek.modmixins.core

import net.minecraftforge.common.config.Configuration
import java.io.File

object LoadingConfig {

    fun loadConfig(file: File) {
        config = Configuration(file)
        fixRailcraftBoilerPollution = config["fixes", "fixRailcraftBoilerPollution", true, "Adds Pollution to the Railcraft Boilers"].boolean
        fixVanillaFurnacePollution = config["fixes", "fixVanillaFurnacePollution", true, "Adds Pollution to the IC2 and Vanilla Furnaces"].boolean
        fixThaumcraftFurnacePollution = config["fixes", "fixThaumcraftFurnacePollution", true, "Adds Pollution to the Thaumcraft Furnaces"].boolean
        RailcraftJarName = config["jars", "Railcraft Jar Name","Railcraft_1.7.10-9.12.2.1.jar", "Name of the Railcraft Jar"].string
        furnacePullution = config["options","furnacePullution",20, "Pollution per second, min 20!",20, Int.MAX_VALUE].int / 20
        if (config.hasChanged())
            config.save()
    }

    private lateinit var config: Configuration
    var fixRailcraftBoilerPollution : Boolean = false
    var fixVanillaFurnacePollution : Boolean = false
    var fixThaumcraftFurnacePollution : Boolean = false
    var furnacePullution : Int = 20
    lateinit var RailcraftJarName : String
}