package zmaster587.advancedRocketry.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Stores config variables
 *
 */
public class Configuration {
	public static final String configFolder = "advRocketry";
	
	public static int orbit = 1000;
	public static int MoonId = -1;
	public static int spaceDimId = -2;
	public static int fuelPointsPer10Mb = 10;
	public static int stationSize = 1024;
	
	public static double rocketThrustMultiplier;
	public static double fuelCapacityMultiplier;
	
	public static int maxBiomes = 512;
	public static boolean rocketRequireFuel = true;
	public static boolean enableOxygen = true;
	public static float buildSpeedMultiplier = 1f;
	
	public static boolean generateCopper;
	public static int copperPerChunk; 
	public static int copperClumpSize;
	
	public static boolean generateTin;
	public static int tinPerChunk;
	public static int tinClumpSize;
	
	public static boolean generateDilithium;
	public static int dilithiumClumpSize;
	public static int dilithiumPerChunk;
	public static int dilithiumPerChunkMoon;
	
	public static int aluminumPerChunk;
	public static int aluminumClumpSize;
	public static boolean generateAluminum;
	
	public static boolean generateRutile;
	public static int rutilePerChunk;
	public static int rutileClumpSize;
	public static boolean allowMakingItemsForOtherMods;
	public static boolean scrubberRequiresCartrige;
	public static float EUMult;
	public static float RFMult;
	public static boolean overrideGCAir;
	public static int fuelPointsPerDilithium;
	public static boolean electricPlantsSpawnLightning;

	public static boolean allowSawmillVanillaWood;

	public static double asteroidMiningMult;

	public static String[] standardAsteroidOres;

	public static int atmosphereHandleBitMask;

	public static boolean automaticRetroRockets;

	public static boolean advancedVFX;

	public static boolean enableLaserDrill;

	public static int spaceSuitOxygenTime;

	public static float travelTimeMultiplier;

	public static int maxBiomesPerPlanet;

	public static boolean enableTerraforming;

	public static double gasCollectionMult;
	public static boolean allowTerraforming;

	public static int terraformingBlockSpeed;
	public static double terraformSpeed;
	public static boolean terraformRequiresFluid;
	public static float microwaveRecieverMulitplier;
	public static boolean blackListAllVanillaBiomes;
	public static boolean canPlayerRespawnInSpace;
	public static float spaceLaserPowerMult;
	public static List<Integer> laserBlackListDims = new LinkedList<Integer>();
	public static String[] standardLaserDrillOres;
	public static boolean laserDrillPlanet;
}
