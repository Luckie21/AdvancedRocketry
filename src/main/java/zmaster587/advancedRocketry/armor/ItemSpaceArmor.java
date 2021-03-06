package zmaster587.advancedRocketry.armor;

import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import zmaster587.advancedRocketry.achievements.ARAchivements;
import zmaster587.advancedRocketry.api.AdvancedRocketryItems;
import zmaster587.advancedRocketry.api.Configuration;
import zmaster587.advancedRocketry.api.IAtmosphere;
import zmaster587.advancedRocketry.api.armor.IFillableArmor;
import zmaster587.advancedRocketry.api.armor.IProtectiveArmor;
import zmaster587.advancedRocketry.atmosphere.AtmosphereType;
import zmaster587.advancedRocketry.client.render.armor.RenderJetPack;
import zmaster587.libVulpes.api.IArmorComponent;
import zmaster587.libVulpes.api.IModularArmor;
import zmaster587.libVulpes.util.EmbeddedInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.Constants.NBT;
/**
 * Space Armor
 * Any class that extends this will gain the ability to store oxygen and will protect players from the vacuum atmosphere type
 *
 */
public class ItemSpaceArmor extends ItemArmor implements ISpecialArmor, IFillableArmor, IProtectiveArmor, IModularArmor {

	private final static String componentNBTName = "componentName";

	public ItemSpaceArmor(ArmorMaterial material, int component) {
		super(material, 0, component);
	}
	
	
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_,
			List list, boolean p_77624_4_) {
		super.addInformation(stack, p_77624_2_, list, p_77624_4_);
		
		list.add("Modules:");
		
		for(ItemStack componentStack : getComponents(stack)) {
			list.add(EnumChatFormatting.DARK_GRAY + componentStack.getDisplayName());
		}
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,
			ItemStack armor, DamageSource source, double damage, int slot) {
		if(!source.isUnblockable())
			return new ArmorProperties(0, 5, 1);
		return new ArmorProperties(0, 0, 0);
	}

	private EmbeddedInventory loadEmbeddedInventory(ItemStack stack) {
		if(stack.hasTagCompound()) {
			EmbeddedInventory inv = new EmbeddedInventory(4);
			inv.readFromNBT(stack.getTagCompound());
			return inv;
		}
		return new EmbeddedInventory(4);
	}

	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving,
			ItemStack itemStack, int armorSlot) {
		//if(armorSlot == 1)
		//return new RenderJetPack();

		return super.getArmorModel(entityLiving, itemStack, armorSlot);
	}

	private void saveEmbeddedInventory(ItemStack stack, EmbeddedInventory inv) {
		if(stack.hasTagCompound()) {
			inv.writeToNBT(stack.getTagCompound());
		}
		else {
			NBTTagCompound nbt = new NBTTagCompound();
			inv.writeToNBT(nbt);
			stack.setTagCompound(nbt);
		}
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player,
			ItemStack armor) {
		super.onArmorTick(world, player, armor);

		if(armor.hasTagCompound()) {
			
			//Some upgrades modify player capabilities
			
			EmbeddedInventory inv = loadEmbeddedInventory(armor);
			for(int i = 0; i < inv.getSizeInventory(); i++ ) {
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null) {
					IArmorComponent component = (IArmorComponent)stack.getItem();
					component.onTick(world, player, armor, inv, stack);
				}
			}

			saveEmbeddedInventory(armor, inv);
		}
		               ItemStack feet = player.getCurrentArmor(0);
		               ItemStack leg = player.getCurrentArmor(1);
		               ItemStack chest = player.getCurrentArmor(2);
		               ItemStack helm = player.getCurrentArmor(3);
		               if(feet != null && feet.getItem() instanceof ItemSpaceArmor && leg != null && leg.getItem() instanceof ItemSpaceArmor && chest != null && chest.getItem() instanceof ItemSpaceArmor && helm != null && helm.getItem() instanceof ItemSpaceArmor)
		                       player.triggerAchievement(ARAchivements.suitedUp);

	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot,
			String type) {
		if(stack.getItem() == AdvancedRocketryItems.itemSpaceSuit_Leggings)
			return "advancedRocketry:textures/armor/spaceSuit_layer1.png";//super.getArmorTexture(stack, entity, slot, type);
		return "advancedRocketry:textures/armor/spaceSuit_layer2.png";
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 1;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack armor,
			DamageSource source, int damage, int slot) {
		// TODO Handle armor damage

		if(armor.hasTagCompound()) {

			EmbeddedInventory inv = loadEmbeddedInventory(armor);
			for(int i = 0; i < inv.getSizeInventory(); i++ ) {
				ItemStack stack = inv.getStackInSlot(i);
				if(stack != null) {
					IArmorComponent component = (IArmorComponent)stack.getItem();
					component.onArmorDamaged(entity, armor, stack, source, damage);
				}
			}

			saveEmbeddedInventory(armor, inv);
		}
	}

	@Override
	public void addArmorComponent(World world, ItemStack armor, ItemStack component, int slot) {

		EmbeddedInventory inv = loadEmbeddedInventory(armor);

		if(((IArmorComponent)component.getItem()).onComponentAdded(world, armor)) {
			inv.setInventorySlotContents(slot, component);

			saveEmbeddedInventory(armor, inv);
		}
	}

	public ItemStack removeComponent(World world, ItemStack armor, int index) {
		NBTTagCompound nbt;
		NBTTagList componentList;

		if(armor.hasTagCompound()) {
			nbt = armor.getTagCompound();
			componentList = nbt.getTagList(componentNBTName, NBT.TAG_COMPOUND);
		}
		else {
			return null;
		}

		EmbeddedInventory inv = loadEmbeddedInventory(armor);
		ItemStack stack = inv.getStackInSlot(index);
		inv.setInventorySlotContents(index, null);

		if(stack != null) {
			IArmorComponent component = (IArmorComponent) stack.getItem();
			component.onComponentRemoved(world, armor);
			saveEmbeddedInventory(armor, inv);
		}

		

		return stack;
	}

	public List<ItemStack> getComponents(ItemStack armor) {

		List<ItemStack> list = new LinkedList<ItemStack>();
		NBTTagCompound nbt;
		NBTTagList componentList;

		if(armor.hasTagCompound()) {
			EmbeddedInventory inv = loadEmbeddedInventory(armor);

			for(int i = 0; i < inv.getSizeInventory(); i++) {
				if(inv.getStackInSlot(i) != null)
					list.add(inv.getStackInSlot(i));
			}
		}

		return list;
	}

	/**
	 * gets the amount of air remaining in the suit.
	 * @param stack stack from which to get an amount of air
	 * @return the amount of air in the stack
	 */
	@Override
	public int getAirRemaining(ItemStack stack) {
		if(stack.hasTagCompound()) {
			return stack.getTagCompound().getInteger("air");
		}
		else {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("air", 0);
			stack.setTagCompound(nbt);
			return getMaxAir();
		}
	}

	/**
	 * Sets the amount of air remaining in the suit (WARNING: DOES NOT BOUNDS CHECK!)
	 * @param stack the stack to operate on
	 * @param amt amount of air to set the suit to
	 */
	@Override
	public void setAirRemaining(ItemStack stack, int amt) {
		NBTTagCompound nbt;
		if(stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		}
		else {
			nbt = new NBTTagCompound();
		}
		nbt.setInteger("air", amt);
		stack.setTagCompound(nbt);
	}

	/**
	 * Decrements air in the suit by amt
	 * @param stack the item stack to operate on
	 * @param amt amount of air by which to decrement
	 * @return The amount of air extracted from the suit
	 */
	@Override
	public int decrementAir(ItemStack stack, int amt) {
		NBTTagCompound nbt;
		if(stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		}
		else {
			nbt = new NBTTagCompound();
		}

		int prevAmt = nbt.getInteger("air");
		int newAmt = Math.max(prevAmt - amt,0);
		nbt.setInteger("air", newAmt);
		stack.setTagCompound(nbt);

		return prevAmt - newAmt;
	}

	/**
	 * Increments air in the suit by amt
	 * @param stack the item stack to operate on
	 * @param amt amount of air by which to decrement
	 * @return The amount of air inserted into the suit
	 */
	@Override
	public int increment(ItemStack stack, int amt) {
		NBTTagCompound nbt;
		if(stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
		}
		else {
			nbt = new NBTTagCompound();
		}

		int prevAmt = nbt.getInteger("air");
		int newAmt = Math.min(prevAmt + amt, getMaxAir());
		nbt.setInteger("air", newAmt);
		stack.setTagCompound(nbt);

		return newAmt - prevAmt;
	}

	/**
	 * @return the maximum amount of air allowed in this suit
	 */
	@Override
	public int getMaxAir() {
		return Configuration.spaceSuitOxygenTime*1200; //30 minutes;
	}

	@Override
	public boolean protectsFromSubstance(IAtmosphere atmosphere) {
		return atmosphere == AtmosphereType.VACUUM;
	}

	@Override
	public int getNumSlots(ItemStack stack) {
		return loadEmbeddedInventory(stack).getSizeInventory();
	}

	@Override
	public ItemStack getComponentInSlot(ItemStack stack, int slot) {
		return loadEmbeddedInventory(stack).getStackInSlot(slot);
	}

	@Override
	public IInventory loadModuleInventory(ItemStack stack) {
		return loadEmbeddedInventory(stack);
	}

	@Override
	public void saveModuleInventory(ItemStack stack, IInventory inv) {
		saveEmbeddedInventory(stack, (EmbeddedInventory)inv);
	}

}
