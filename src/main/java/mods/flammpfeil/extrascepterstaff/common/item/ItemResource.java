package mods.flammpfeil.extrascepterstaff.common.item;

import com.google.common.collect.Maps;
import mods.flammpfeil.extrascepterstaff.ExtraScepterStaff;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.IIcon;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Furia on 14/06/10.
 */
public class ItemResource extends Item {

    public ItemResource(){
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(ExtraScepterStaff.tab);
    }

    public enum ResourceType{
        None,
        WandRod,
        StaffRod,
        ExtraStaffRod,
        WandCap,
        Fragment,
        Material,
    }

    public static final int WandRodFlag = 0x1000;
    public static final int StaffRodFlag = 0x2000;
    public static final int ExtraStaffRodFlag = 0x4000;
    public static final int WandCapFlag = 0x0800;
    public static final int FragmentFlag = 0x0400;
    public static final int MaterialMax = 0x00FF;

    public static ResourceType getResourceType(ItemStack stack){
        int damage = stack.getItemDamage();
        if(damage == 0)
            return ResourceType.None;

        if(0 < (damage & WandRodFlag)){
            if(0 < (damage & StaffRodFlag))
                if(0 < (damage & ExtraStaffRodFlag))
                    return ResourceType.ExtraStaffRod;
                else
                    return ResourceType.StaffRod;
            else
                return ResourceType.WandRod;
        }else{
            if(0 < (damage & WandCapFlag)){
                return ResourceType.WandCap;
            }
            if(0 < (damage & FragmentFlag)){
                return ResourceType.Fragment;
            }
            return ResourceType.Material;
        }
    }

    public static int getItemDamage(int materialIndex,ResourceType type){
        int result = materialIndex;
        switch (type){
            case ExtraStaffRod:
                result += ExtraStaffRodFlag;

            case StaffRod:
                result += StaffRodFlag;

            case WandRod:
                result += WandRodFlag;
                break;

            case WandCap:
                result += WandCapFlag;
                break;
            case Fragment:
                result += FragmentFlag;
                break;
        }

        return result;
    }

    public static final Map<Integer,String> MaterialNameMap = Maps.newHashMap();
    private static Map<Integer,IIcon> MaterialIconMap = Maps.newHashMap();

    public static void registerMaterialName(int index,String name){
        if(index < 0 || MaterialMax < index)
            throw new IllegalArgumentException("required (0 < index && index < 0xFFFF");

        MaterialNameMap.put(index,name);
    }

    public static String getMaterialName(ItemStack stack){
        int index = stack.getItemDamage() & MaterialMax;
        return MaterialNameMap.containsKey(index) ? MaterialNameMap.get(index) : "undefined";
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {

        if(par1ItemStack.getItemDamage() == 0)
            return super.getUnlocalizedName(par1ItemStack);

        ResourceType type = getResourceType(par1ItemStack);
        return super.getUnlocalizedName(par1ItemStack) + "." + type.toString() + "." + getMaterialName(par1ItemStack);
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        for(int index : MaterialNameMap.keySet()){
            ItemStack stack;
            stack = new ItemStack(p_150895_1_,1,getItemDamage(index,ResourceType.Material));
            p_150895_3_.add(stack);

            stack = new ItemStack(p_150895_1_,1,getItemDamage(index,ResourceType.Fragment));
            p_150895_3_.add(stack);

            stack = new ItemStack(p_150895_1_,1,getItemDamage(index,ResourceType.WandCap));
            p_150895_3_.add(stack);

            stack = new ItemStack(p_150895_1_,1,getItemDamage(index,ResourceType.WandRod));
            p_150895_3_.add(stack);

            stack = new ItemStack(p_150895_1_,1,getItemDamage(index,ResourceType.StaffRod));
            p_150895_3_.add(stack);

            String[] formats = {"ESS_%s","ESS_%s_staff","ESS_Ex_%s_staff"};

            String name = MaterialNameMap.get(index);
            for(String format : formats){
                ItemStack wandCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);

                WandCap cap = WandCap.caps.get(String.format("ESS_%s",name));
                if(cap == null) cap = WandCap.caps.get("thaumium");
                ((ItemWandCasting) wandCasting.getItem()).setCap(wandCasting, cap);

                WandRod rod = WandRod.rods.get(String.format(format,name));
                if(rod == null) continue;
                ((ItemWandCasting) wandCasting.getItem()).setRod(wandCasting, rod);
                //sceptre.setTagInfo("sceptre", new NBTTagByte((byte) 1));

                Aspect aspect;
                for (Iterator i$ = Aspect.getPrimalAspects().iterator();i$.hasNext();) {
                    aspect = (Aspect) i$.next();

                    ((ItemWandCasting) wandCasting.getItem()).addVis(wandCasting, aspect, ((ItemWandCasting) wandCasting.getItem()).getMaxVis(wandCasting), true);
                }
                p_150895_3_.add(wandCasting);
            }

            scepters:
            {

                ItemStack wandCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);

                WandCap cap = WandCap.caps.get(String.format("ESS_%s",name));
                //if(cap == null)
                cap = WandCap.caps.get("void");
                ((ItemWandCasting) wandCasting.getItem()).setCap(wandCasting, cap);

                WandRod rod = WandRod.rods.get(String.format("ESS_%s",name));
                if(rod == null) break scepters;
                ((ItemWandCasting) wandCasting.getItem()).setRod(wandCasting, rod);
                wandCasting.setTagInfo("sceptre", new NBTTagByte((byte) 1));

                Aspect aspect;
                for (Iterator i$ = Aspect.getPrimalAspects().iterator();i$.hasNext();) {
                    aspect = (Aspect) i$.next();

                    ((ItemWandCasting) wandCasting.getItem()).addVis(wandCasting, aspect, ((ItemWandCasting) wandCasting.getItem()).getMaxVis(wandCasting), true);
                }
                p_150895_3_.add(wandCasting);
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int par1) {
        if(MaterialIconMap.containsKey(par1))
            return MaterialIconMap.get(par1);
        else
            return super.getIconFromDamage(par1);
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);

        for(int index : MaterialNameMap.keySet()){
            String name = MaterialNameMap.get(index);
            IIcon icon;
            icon = par1IconRegister.registerIcon(String.format("%s:material_%s", ExtraScepterStaff.modid, name));
            MaterialIconMap.put(getItemDamage(index,ResourceType.Material),icon);

            icon = par1IconRegister.registerIcon(String.format("%s:fragment_%s", ExtraScepterStaff.modid, name));
            MaterialIconMap.put(getItemDamage(index,ResourceType.Fragment),icon);

            icon = par1IconRegister.registerIcon(String.format("%s:wand_cap_%s", ExtraScepterStaff.modid, name));
            MaterialIconMap.put(getItemDamage(index,ResourceType.WandCap),icon);

            icon = par1IconRegister.registerIcon(String.format("%s:wand_rod_%s", ExtraScepterStaff.modid, name));
            MaterialIconMap.put(getItemDamage(index,ResourceType.WandRod),icon);

            icon = par1IconRegister.registerIcon(String.format("%s:staff_rod_%s", ExtraScepterStaff.modid, name));
            MaterialIconMap.put(getItemDamage(index,ResourceType.StaffRod),icon);
        }
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        boolean result = false;

        ResourceType type = getResourceType(par1ItemStack);
        switch (type){
            case None:
            case ExtraStaffRod:
            case WandCap:
                result = true;
                break;
            default:
                break;
        }
        return result;
    }
}
