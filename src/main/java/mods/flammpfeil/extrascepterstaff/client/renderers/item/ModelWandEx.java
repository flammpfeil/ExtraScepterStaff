package mods.flammpfeil.extrascepterstaff.client.renderers.item;

import com.google.common.collect.Maps;
import mods.flammpfeil.extrascepterstaff.ExtraScepterStaff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.block.BlockRenderer;
import thaumcraft.client.renderers.item.ModelWand;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.awt.*;
import java.util.Map;

/**
 * Created by Furia on 14/06/11.
 */
public class ModelWandEx extends ModelWand {
    private final RenderBlocks renderBlocks = new RenderBlocks();

    public static Map<String,IModelCustom> CustomModels = Maps.newHashMap();

    public IItemRenderer.ItemRenderType type;
    public boolean using;

    @Override
    public void render(ItemStack wandStack) {
        if(RenderManager.instance.renderEngine == null)
            return;
        ItemWandCasting wand = (ItemWandCasting)wandStack.getItem();

        String tag = wand.getRod(wandStack).getTag();
        if(CustomModels.containsKey(tag)){
            IModelCustom model = CustomModels.get(tag);

            render(wandStack,model);
        }else
            super.render(wandStack);
    }

    private static final void setLightmapTextureCoords(int factor){
        float k = factor % 65536;
        float l = factor / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k, l);
    }

    public void render(ItemStack wandStack,IModelCustom model){
        ItemWandCasting wand = (ItemWandCasting)wandStack.getItem();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        //boolean staff = wand.isStaff(wandStack);
        //boolean runes = wand.hasRunes(wandStack);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        {
            GL11.glTranslatef(0, 3.0f, 0);
            float scale = 0.020f;
            if(type == IItemRenderer.ItemRenderType.EQUIPPED)
            {
                scale = 0.025f;
            }
            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(180.0f, 0, 0, 1);
            GL11.glRotatef(130.0f, 0, 1, 0);

            if(using && type != IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            {
                GL11.glRotatef(30.0f, 1, 0, 0);
                GL11.glRotatef(-30.0f, 0, 0, 1);

                GL11.glRotatef(-20.0f, 0, 1, 0);
                GL11.glTranslatef(-50f, -50f, -40.0f);
            }

            if(type == IItemRenderer.ItemRenderType.INVENTORY){
                GL11.glRotatef(160.0f, 0, 1, 0);
                GL11.glTranslatef(0, 5f, 0);
                GL11.glScalef(2.5f, 0.7f, 2.0f);
            }

            {//Rod
                boolean isGlowing = wand.getRod(wandStack).isGlowing();
                if(isGlowing) setLightmapTextureCoords((int)(200F + MathHelper.sin(player.ticksExisted) * 5F + 5F));

                Minecraft.getMinecraft().renderEngine.bindTexture(wand.getRod(wandStack).getTexture());
                model.renderPart("Rod");

                if(isGlowing) setLightmapTextureCoords(player.getBrightnessForRender(0.0F));
            }

            {//Cap
                Minecraft.getMinecraft().renderEngine.bindTexture(wand.getCap(wandStack).getTexture());
                model.renderPart("Cap");
            }


            {//Focus
                Color c = Color.cyan;
                float alpha = 0.95F;
                if(wand.getFocus(wandStack) != null)
                {
                    /*
                    if(wand.getFocus(wandStack).getOrnament() != null)
                    {
                        RenderManager.instance.renderEngine.bindTexture(TextureMap.locationItemsTexture);
                    }
                    */

                    if(wand.getFocus(wandStack).getFocusDepthLayerIcon() != null)
                    {
                        setLightmapTextureCoords((int)(200F + MathHelper.sin(player.ticksExisted) * 5F + 5F));

                        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

                        IIcon icon = wand.getFocus(wandStack).getFocusDepthLayerIcon();
                        float uSize = icon.getMaxU() - icon.getMinU();
                        float vSize = icon.getMaxV() - icon.getMinV();

                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glPushMatrix();
                        {
                            GL11.glTranslatef(icon.getMinU(), icon.getMinV(), 0.0F);
                            GL11.glScalef(uSize, vSize, 1.0f);
                            model.renderPart("FocusDepthLayer");
                        }
                        GL11.glPopMatrix();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);

                        setLightmapTextureCoords(player.getBrightnessForRender(0.0F));

                        alpha = 0.6F;
                    }

                    c = new Color(wand.getFocus(wandStack).getFocusColor());
                }

                Minecraft.getMinecraft().renderEngine.bindTexture(wand.getRod(wandStack).getTexture());


                if(alpha < 0.7f){
                    GL11.glColor4f((float)c.getRed() / 255F, (float)c.getGreen() / 255F, (float)c.getBlue() / 255F, alpha);
                    model.renderPart("FocusCover");

                }else{
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    model.renderPart("FocusDefaultCover");
                }

                setLightmapTextureCoords((int) (195F + MathHelper.sin((float) player.ticksExisted / 3F) * 10F + 10F));

                GL11.glColor4f((float)c.getRed() / 255F, (float)c.getGreen() / 255F, (float)c.getBlue() / 255F, alpha);

                model.renderPart("Focus");
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_ONE,GL11.GL_ONE,1,0);
                model.renderPart("Focus");
            }

            /*if(runes){}*/
        }
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
