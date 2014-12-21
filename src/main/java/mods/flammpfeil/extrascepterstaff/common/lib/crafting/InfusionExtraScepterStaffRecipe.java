package mods.flammpfeil.extrascepterstaff.common.lib.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.ArrayList;

/**
 * Created by Furia on 14/06/12.
 */
public class InfusionExtraScepterStaffRecipe extends InfusionRecipe {
    public InfusionExtraScepterStaffRecipe(String research, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe) {
        super(research, output, inst, aspects2, input, recipe);
    }

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        if (getRecipeInput()==null) return false;
        if (research.length()>0 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
            return false;
        }
        ItemStack i2 = central.copy();
        if (getRecipeInput().getItemDamage()== OreDictionary.WILDCARD_VALUE) {
            i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
        }
        if (!areItemStacksEqualLocal(i2, getRecipeInput(), true)) return false;
        ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
        for (ItemStack is:input) {
            ii.add(is.copy());
        }
        for (ItemStack comp:getComponents()) {
            boolean b=false;
            for (int a=0;a<ii.size();a++) {
                i2 = ii.get(a).copy();
                if (comp.getItemDamage()==OreDictionary.WILDCARD_VALUE) {
                    i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
                }
                if (areItemStacksEqualLocal(i2, comp, true)) {
                    ii.remove(a);
                    b=true;
                    break;
                }
            }
            if (!b) return false;
        }
        return ii.size()==0?true:false;
    } 
    public boolean areItemStacksEqualLocal(ItemStack stack0, ItemStack stack1, boolean fuzzy) {

        if(stack0 == null || stack1 == null)
            return false;

        if(stack0.getItem() instanceof ItemWandCasting && stack1.getItem() instanceof ItemWandCasting){
            ItemWandCasting wand = (ItemWandCasting)stack1.getItem();
            if(wand.getRod(stack1).getTag() == wand.getRod(stack0).getTag()){
                return true;
            }
        }

        return super.areItemStacksEqual(stack0, stack1, fuzzy);
    }
}
