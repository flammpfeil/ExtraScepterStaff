package mods.flammpfeil.extrascepterstaff.client.renderers.item;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import thaumcraft.client.renderers.item.ItemWandRenderer;
import thaumcraft.client.renderers.models.ModelWand;
import thaumcraft.common.items.wands.ItemWandCasting;

/**
 * Created by Furia on 14/06/12.
 */
public class ItemWandRendererEx implements IItemRenderer {

    public ItemWandRenderer owner;
    public ModelWandEx modelex;

    public ItemWandRendererEx(ItemWandRenderer owner) {
        this.owner = owner;

        modelex = new ModelWandEx();

        ObfuscationReflectionHelper.<ItemWandRenderer,ModelWand>setPrivateValue(ItemWandRenderer.class, this.owner, modelex, "model");
    }

    @Override
    public boolean handleRenderType(ItemStack item, net.minecraftforge.client.IItemRenderer.ItemRenderType type) {
        return this.owner.handleRenderType(item,type);
    }

    @Override
    public boolean shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType type, ItemStack item, net.minecraftforge.client.IItemRenderer.ItemRendererHelper helper) {
        return this.owner.shouldUseRenderHelper(type, item, helper);
    }

    @Override
    public void renderItem(net.minecraftforge.client.IItemRenderer.ItemRenderType type, ItemStack item, Object data[]) {
        modelex.type = type;

        Minecraft mc = Minecraft.getMinecraft();
        if (item == null || !(item.getItem() instanceof ItemWandCasting))
            return;

        EntityLivingBase wielder = null;
        if (type == net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED || type == net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            wielder = (EntityLivingBase) data[1];

        modelex.using =  (wielder != null && (wielder instanceof EntityPlayer) && ((EntityPlayer) wielder).getItemInUse() != null) ;

        this.owner.renderItem(type, item, data);

    }
}