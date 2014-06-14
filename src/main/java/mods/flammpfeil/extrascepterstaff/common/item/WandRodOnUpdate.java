package mods.flammpfeil.extrascepterstaff.common.item;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.WandRodPrimalOnUpdate;

import java.util.List;

/**
 * Created by Furia on 14/06/12.
 */
public class WandRodOnUpdate implements IWandRodOnUpdate{

    private WandRodPrimalOnUpdate[] updater;

    public WandRodOnUpdate(Aspect... aspects){
        List<WandRodPrimalOnUpdate> updaterList = Lists.newArrayList();
        for(Aspect aspect : aspects){
            if(aspect.isPrimal())
                updaterList.add(new WandRodPrimalOnUpdate(aspect));
        }
        updater = updaterList.toArray(new WandRodPrimalOnUpdate[]{});
    }

    @Override
    public void onUpdate(ItemStack itemStack, EntityPlayer entityPlayer) {
        for(WandRodPrimalOnUpdate update : updater){
            update.onUpdate(itemStack,entityPlayer);
        }
    }
}
