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
        fixTimeCommandGc = config["fixes", "fixTimeCommandGc", true, "Fixes GC Time Command without creating lag"].boolean
        rocketPollution = config["options","rocketPollution", 10000, "Pollution when starting per second, min 1!", 1 , Int.MAX_VALUE].int
        furnacePollution = config["options","furnacePollution", 20, "Furnace pollution per second, min 1!", 1 , Int.MAX_VALUE].int
        explosionPollution = config["options","explosionPollution", 333.34, "Explosion one time pollution"].double.toFloat()
        cokeOvenPollution = config["options", "cokeOvenPollution", 3, "Coke oven pollution per second, min 1!", 1, Int.MAX_VALUE].int
        advancedCokeOvenPollution = config["options", "advancedCokeOvenPollution", 80, "Advanced coke oven pollution per second, min 1!", 1, Int.MAX_VALUE].int
        fireboxPollution = config["options", "fireboxPollution", 15, "Railcraft boiler firebox pollution per second, min 1!", 1, Int.MAX_VALUE].int
        hobbyistEnginePollution = config ["options", "hobbyistEnginePollution", 20, "Hobbyist's steam engine pollution per second, min 1!", 1, Int.MAX_VALUE].int

        if (config.hasChanged())
            config.save()
    }

    private lateinit var config: Configuration
    var fixTimeCommandGc : Boolean = false
    var fixRocketPollution : Boolean = false
    var fixRailcraftBoilerPollution : Boolean = false
    var fixVanillaFurnacePollution : Boolean = false
    var fixThaumcraftFurnacePollution : Boolean = false
    var fixExplosionPollution : Boolean = false
    var fixZtonesNetworkVulnerability : Boolean = false
    var furnacePollution : Int = 20
    var rocketPollution : Int = 1
    var explosionPollution : Float = 333.34f
    var cokeOvenPollution : Int = 3;
    var fireboxPollution : Int = 15;
    var advancedCokeOvenPollution: Int = 80;
    var hobbyistEnginePollution: Int = 20;
}