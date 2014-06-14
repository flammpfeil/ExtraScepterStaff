package mods.flammpfeil.extrascepterstaff.common.lib.crafting;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.items.wands.ItemWandCasting;

/**
 * Created by Furia on 14/06/12.
 */
public class InfusionExtraScepterStaffRecipe extends InfusionRecipe {
    public InfusionExtraScepterStaffRecipe(String research, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe) {
        super(research, output, inst, aspects2, input, recipe);
    }

    @Override
    protected boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1, boolean fuzzy) {

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
