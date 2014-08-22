package mods.flammpfeil.extrascepterstaff.client;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import mods.flammpfeil.extrascepterstaff.ExtraScepterStaff;
import mods.flammpfeil.extrascepterstaff.client.renderers.item.ItemWandRendererEx;
import mods.flammpfeil.extrascepterstaff.client.renderers.item.ModelWandEx;
import mods.flammpfeil.extrascepterstaff.common.InitProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import thaumcraft.client.renderers.item.ItemWandRenderer;
import thaumcraft.client.renderers.models.ModelWand;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class InitProxyClient extends InitProxy {
	@Override
	public void initializeItemRenderer() {
        /*
		ItemRendererBaseWeapon renderer = new ItemRendererBaseWeapon();
		MinecraftForgeClient.registerItemRenderer(ExtraScepterStaff.bladeOfZephyr, renderer);
		*/


        {
            String name = ExtraScepterStaff.ExtraMaterials.Alicorn.name;
            ResourceLocation loc = new ResourceLocation(ExtraScepterStaff.modid,"models/ClaritaVisas.obj");
            try{
                IModelCustom model = AdvancedModelLoader.loadModel(loc);
                ModelWandEx.CustomModels.put("ESS_Ex_"+name+"_staff",model);
            }catch(Throwable t){
                t.printStackTrace();
            }
        }

        IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(new ItemStack(ConfigItems.itemWandCasting), IItemRenderer.ItemRenderType.ENTITY);
        if(renderer instanceof ItemWandRenderer){
            IItemRenderer ex = new ItemWandRendererEx((ItemWandRenderer)renderer);
            MinecraftForgeClient.registerItemRenderer(ConfigItems.itemWandCasting,ex);
        }
    }
}
