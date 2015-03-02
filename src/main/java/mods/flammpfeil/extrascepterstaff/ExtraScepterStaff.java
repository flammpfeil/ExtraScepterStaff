package mods.flammpfeil.extrascepterstaff;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.flammpfeil.extrascepterstaff.common.InitProxy;
import mods.flammpfeil.extrascepterstaff.common.item.ClarissaWnadRodOnUpdate;
import mods.flammpfeil.extrascepterstaff.common.item.ItemResource;
import mods.flammpfeil.extrascepterstaff.common.item.WandRodOnUpdate;
import mods.flammpfeil.extrascepterstaff.common.lib.crafting.InfusionExtraScepterStaffRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import thaumcraft.api.IRepairable;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandRodPrimalOnUpdate;

import java.util.Iterator;
import java.util.Map;

@Mod(name= ExtraScepterStaff.modname, modid= ExtraScepterStaff.modid, version=ExtraScepterStaff.version, dependencies = "required-after:Thaumcraft")
public class ExtraScepterStaff {

	public static final String modname = "ExtraScepterStaff";
    public static final String modid = "flammpfeil.extrascepterstaff";
    public static final String version = "@VERSION@";

    public static Item itemResource;

	public static Configuration mainConfiguration;

    public static final CreativeTabs tab = new CreativeTabs(modid)
    {
        @SideOnly(Side.CLIENT)
        @Override
        public Item getTabIconItem()
        {
            return itemResource;
        }
    };

    Map<Integer,WandCap> WandCaps = Maps.newHashMap();
    Map<Integer,WandRod> WandCores = Maps.newHashMap();
    Map<Integer,StaffRod> StaffCores = Maps.newHashMap();

    public enum ExtraMaterials{
        Alicorn(1,"alicorn"),
        Clarissa(2,"clarissa"),
        Clarissa2(3,"clarissa2"),

        ;
        public final String name;
        public final int index;

        ExtraMaterials(int index, String name){
            this.name = name;
            this.index = index;
        }
    }

    public static String getCustomItemStackName(ExtraMaterials mat){
        return String.format("materials.%s",mat.name);
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){

		/*
            mainConfiguration = new Configuration(evt.getSuggestedConfigurationFile());

            try{
                mainConfiguration.load();
            }
            finally
            {
                mainConfiguration.save();
            }
        */

        itemResource = new ItemResource()
                .setUnlocalizedName("flammpfeil.extrascepterstaff.resource")
                .setTextureName("flammpfeil.extrascepterstaff:logo");
        GameRegistry.registerItem(itemResource, "resource");

        {
            ExtraMaterials mat = ExtraMaterials.Alicorn;

            int index = mat.index;
            String name = mat.name;
            IWandRodOnUpdate update = new WandRodOnUpdate(Aspect.AIR,Aspect.ORDER,Aspect.ENTROPY);

            ItemResource.registerMaterialName(index, name);

            ResourceLocation loc = new ResourceLocation(modid,"models/wand_rod_" + name + ".png");

            ItemStack material = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.Material));
            GameRegistry.registerCustomItemStack("material." + name,material);

            ItemStack wandcap = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.WandCap));
            WandCap cap = new WandCap("ESS_"+name,0.95F, Lists.newArrayList(Aspect.AIR,Aspect.ORDER,Aspect.ENTROPY),0.8F,wandcap,7);
            ResourceLocation caploc = new ResourceLocation(modid,"models/wand_cap_" + name + ".png");
            cap.setTexture(caploc);
            WandCaps.put(index,cap);

            ItemStack wandcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.WandRod));
            WandCores.put(index,new WandRod("ESS_"+name, 130, wandcore, 12, update, loc));

            ItemStack staffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.StaffRod));
            StaffCores.put(index,new StaffRod("ESS_"+name, 250, staffcore, 37, update, loc));

            ResourceLocation locEx = new ResourceLocation(modid,"models/ex_wand_rod_" + name + ".png");
            ItemStack exstaffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.ExtraStaffRod));
            StaffCores.put(index,new StaffRod("ESS_Ex_"+name, 300, exstaffcore, 50, update, locEx));
        }

        {
            ExtraMaterials mat = ExtraMaterials.Clarissa;

            int index = mat.index;
            String name = mat.name;

            ItemResource.registerMaterialName(index, name);

            ResourceLocation loc = new ResourceLocation(modid,"models/wand_rod_" + name + ".png");

            ItemStack wandcap = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.WandCap));
            WandCap cap = new WandCap("ESS_"+name,2.0F,wandcap,7);
            ResourceLocation caploc = new ResourceLocation(modid,"models/wand_cap_" + name + ".png");
            cap.setTexture(caploc);
            WandCaps.put(index,cap);


            //ItemStack staffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.StaffRod));

            IWandRodOnUpdate updateex = new ClarissaWnadRodOnUpdate(true,true);
            ResourceLocation locEx = new ResourceLocation(modid,"models/ex_wand_rod_" + name + ".png");
            ItemStack exstaffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.ExtraStaffRod));
            StaffRod rod = new StaffRod("ESS_Ex_"+name, 500, exstaffcore, 9999, updateex, locEx);
            rod.setRunes(true);
            rod.setGlowing(true);
            StaffCores.put(index,rod);
        }

        {
            ExtraMaterials mat = ExtraMaterials.Clarissa2;

            int index = mat.index;
            String name = mat.name;
            IWandRodOnUpdate update = new ClarissaWnadRodOnUpdate(false,false);

            ItemResource.registerMaterialName(index, name);

            ResourceLocation loc = new ResourceLocation(modid,"models/wand_rod_" + name + ".png");

            ItemStack wandcap = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.WandCap));
            WandCap cap = new WandCap("ESS_"+name,2.0F,wandcap,7);
            ResourceLocation caploc = new ResourceLocation(modid,"models/wand_cap_" + name + ".png");
            cap.setTexture(caploc);
            WandCaps.put(index,cap);

            ItemStack wandcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.WandRod));
            WandCores.put(index,new WandRod("ESS_"+name, 150, wandcore, 26, update, loc));

            ItemStack staffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.StaffRod));
            StaffRod staffRod = new StaffRod("ESS_"+name, 350, staffcore, 37, update, loc);
            staffRod.setRunes(true);
            StaffCores.put(index,staffRod);

            IWandRodOnUpdate updateex = new ClarissaWnadRodOnUpdate(false,true);
            ResourceLocation locEx = new ResourceLocation(modid,"models/ex_wand_rod_" + name + ".png");
            ItemStack exstaffcore = new ItemStack(itemResource,1,ItemResource.getItemDamage(index, ItemResource.ResourceType.ExtraStaffRod));
            StaffRod rod = new StaffRod("ESS_Ex_"+name, 400, exstaffcore, 9999, updateex, locEx);
            rod.setRunes(true);
            rod.setGlowing(true);
            StaffCores.put(index,rod);
        }
    }
    @EventHandler
    public void init(FMLInitializationEvent evt){

        InitProxy.proxy.initializeItemRenderer();
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent evt)
    {
/*
        ItemStack soul = GameRegistry.findItemStack(SlashBlade.modid,SlashBlade.ProudSoulStr,1);
        ThaumcraftApi.registerObjectTag(soul, new AspectList()
                .add(Aspect.METAL, 1)
                .add(Aspect.SOUL, 2)
                .add(Aspect.FIRE, 2)
        );

        ItemStack tinySoul = GameRegistry.findItemStack(SlashBlade.modid,SlashBlade.TinyBladeSoulStr,1);
        ThaumcraftApi.registerObjectTag(tinySoul, new AspectList()
                .add(Aspect.METAL, 1)
                .add(Aspect.SOUL, 1)
                .add(Aspect.FIRE, 1)
        );

        ItemStack ingot = GameRegistry.findItemStack(SlashBlade.modid,SlashBlade.IngotBladeSoulStr,1);
        ThaumcraftApi.registerObjectTag(ingot, new AspectList()
                .add(Aspect.METAL, 3)
                .add(Aspect.SOUL, 8)
        );

        ItemStack sphere = GameRegistry.findItemStack(SlashBlade.modid,SlashBlade.SphereBladeSoulStr,1);
        ThaumcraftApi.registerObjectTag(sphere, new AspectList()
                .add(Aspect.METAL, 2)
                .add(Aspect.SOUL, 8)
                .add(Aspect.CRYSTAL, 1)
        );

        {
            //================================================================================================================
            ItemStack blade = SlashBlade.getCustomBlade(modid,"flammpfeil.slashblade.windeater");

            Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");
                    //"ElementalSword",

            InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe("BLADEOFTHEZEPHYR",
                blade,
                1,
                new AspectList().add(Aspect.AIR, 8).add(Aspect.WEAPON, 8).add(Aspect.ENERGY, 8),
                new ItemStack(swordOfZephyr),
                new ItemStack[]{
                          new ItemStack(crystal, 1, 0)
                        , soul
                        , new ItemStack(Blocks.diamond_ore)
                        , soul
                        , sphere
                        , soul
                        , new ItemStack(Items.bone, 1, 0)
                        , soul });

            //================================================================================================================
            ItemStack reqiredBlade = blade.copy();
            {
                reqiredBlade.setItemDamage(OreDictionary.WILDCARD_VALUE);
                NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(reqiredBlade);
                ItemSlashBlade.KillCount.set(tag,100);
                String name="flammpfeil.slashblade.zephyr.reqired";
                GameRegistry.registerCustomItemStack(name,reqiredBlade);
                ItemSlashBladeNamed.NamedBlades.add(String.format("%s:%s",modid,name));
            }

            ItemStack trueBlade = SlashBlade.getCustomBlade(modid,"flammpfeil.slashblade.zephyr");
            IRecipe wakeup = new RecipeAwakeBlade(trueBlade,
                    reqiredBlade,
                    " X ",
                    "XBX",
                    " X ",
                    'X',soul,
                    'B',reqiredBlade);
            GameRegistry.addRecipe(wakeup);//================================================================================================================
            ItemStack reqiredBladeBroken = reqiredBlade.copy();
            {
                NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(reqiredBladeBroken);
                ItemSlashBlade.IsBroken.set(tag,true);
            }

            IRecipe wakeup2 = new RecipeAwakeBlade(trueBlade,
                    reqiredBladeBroken,
                    " X ",
                    "XBX",
                    " X ",
                    'X',soul,
                    'B',reqiredBladeBroken);
            GameRegistry.addRecipe(wakeup2);
            //================================================================================================================

            new ResearchItem("BLADEOFTHEZEPHYR", "ARTIFICE",
                    new AspectList().add(Aspect.WEAPON, 5).add(Aspect.AIR, 5).add(Aspect.ENERGY, 3),
                    -10, 5, 1, blade)
                .setPages(new ResearchPage("tc.research_page.BLADEOFTHEZEPHYR.1"),
                        new ResearchPage(recipe),
                        new ResearchPage(wakeup),
                        new ResearchPage(wakeup2))
                .setParents(new String[]{"ELEMENTALSWORD"})
                .setHidden()
                .setItemTriggers(soul, tinySoul, ingot, sphere)
                .registerResearchItem();


        }
*/

        String categoryKey = "EXTRASCEPTERSTAFF";
        ResearchCategories.registerCategory(categoryKey, new ResourceLocation("flammpfeil.extrascepterstaff", "textures/items/logo.png"), new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));

        {
            int offsetCol = 0;
            int offsetRow = 0;


            ExtraMaterials materialEx = ExtraMaterials.Clarissa;
            String nameEx = materialEx.name;
            int indexEx = materialEx.index;

            ItemStack wandcapEx = new ItemStack(itemResource,1, ItemResource.getItemDamage(indexEx, ItemResource.ResourceType.WandCap));
            ItemStack staffcoreEx = new ItemStack(itemResource,1, ItemResource.getItemDamage(indexEx, ItemResource.ResourceType.StaffRod));

            ExtraMaterials material = ExtraMaterials.Clarissa2;
            String name = material.name;
            int index = material.index;

            ItemStack wandcap = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.WandCap));
            ItemStack wandrod = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.WandRod));
            ItemStack staffcore = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.StaffRod));


            {
                ThaumcraftApi.registerObjectTag(staffcoreEx, new AspectList()
                        .add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2)
                );

                ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST,new WeightedRandomChestContent(staffcoreEx,1,1,1));
                ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST,new WeightedRandomChestContent(staffcoreEx,1,1,1));
                ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(staffcoreEx,1,1,1));
            }

            {
                String researchKey = String.format("ROD_ESS_%s",name);


                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, wandrod.copy(), 10,
                        new AspectList()
                                .add(Aspect.ORDER, 32)
                                .add(Aspect.ENTROPY, 32)
                                .add(Aspect.LIGHT, 64)
                                .add(Aspect.MAGIC, 64),
                        new ItemStack(ConfigItems.itemWandRod, 1, 100),
                        new ItemStack[]{
                                new ItemStack(Blocks.quartz_block,1,1),
                                new ItemStack(crystal, 1, 4),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(Blocks.quartz_block,1,1),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 4)
                        });

                int localOffsetX = 2;
                int localOffsetY = 2;

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.LIGHT, 5).add(Aspect.ENTROPY, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5)
                        ,offsetCol + localOffsetX, offsetRow + localOffsetY, 2, wandrod)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"), new ResearchPage(recipe))
                        .setHidden()
                        .setItemTriggers(staffcoreEx)
                        .registerResearchItem();

                ThaumcraftApi.addWarpToResearch(researchKey, 1);
            }

            {
                String researchKey = String.format("ROD_ESS_%s_staff",name);

                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, staffcore.copy(), 10,
                        new AspectList()
                                .add(Aspect.ORDER, 32)
                                .add(Aspect.ENTROPY, 64)
                                .add(Aspect.LIGHT, 64)
                                .add(Aspect.MAGIC, 32),
                        wandrod.copy(),
                        new ItemStack[]{
                                new ItemStack(ConfigItems.itemResource, 1, 15),
                                new ItemStack(crystal, 1, 4),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(Blocks.quartz_block,1,1),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 4)
                        });

                int localOffsetX = 2;
                int localOffsetY = 4;

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.LIGHT, 5).add(Aspect.ENTROPY, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.AURA, 5)
                        ,offsetCol + localOffsetX, offsetRow + localOffsetY, 2, staffcore)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),new ResearchPage(recipe))
                                //.setHidden()
                                //.setVirtual()
                                //.setConcealed()
                                //.setItemTriggers(staffcore)
                        .setConcealed()
                        .setParents(String.format("ROD_ESS_%s",name))
                        .registerResearchItem();

                ThaumcraftApi.addWarpToResearch(researchKey, 3);
            }

            {
                String researchKey = String.format("ROD_ESS_Ex_%s_staff",name);


                ItemStack wandCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);

                {
                    WandCap cap = WandCap.caps.get(String.format("ESS_%s",name));
                    ((ItemWandCasting) wandCasting.getItem()).setCap(wandCasting, cap);

                    WandRod rod = WandRod.rods.get(String.format("ESS_Ex_%s_staff",name));
                    ((ItemWandCasting) wandCasting.getItem()).setRod(wandCasting, rod);

                    Aspect aspect;
                    for (Iterator i$ = Aspect.getPrimalAspects().iterator();i$.hasNext();) {
                        aspect = (Aspect) i$.next();

                        ((ItemWandCasting) wandCasting.getItem()).addVis(wandCasting, aspect, ((ItemWandCasting) wandCasting.getItem()).getMaxVis(wandCasting), true);
                    }
                }


                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, wandCasting.copy(), 10,
                        new AspectList()
                                .add(Aspect.ORDER, 64)
                                .add(Aspect.ENTROPY, 64)
                                .add(Aspect.LIGHT, 128)
                                .add(Aspect.MAGIC, 64),
                        staffcore.copy(),
                        new ItemStack[]{
                                new ItemStack(Items.nether_star),
                                new ItemStack(Blocks.quartz_block, 1, 1),
                                new ItemStack(crystal, 1, 6),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 5),
                                new ItemStack(Blocks.quartz_block, 1, 1),
                                new ItemStack(ConfigItems.itemShard, 1, 5),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 6),
                                new ItemStack(Blocks.quartz_block, 1, 1),
                        });

                int localOffsetX = 2;
                int localOffsetY = 6;

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.LIGHT, 5).add(Aspect.ENTROPY, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.AURA, 5).add(Aspect.FIRE, 5)
                        ,offsetCol + localOffsetX, offsetRow + localOffsetY, 2, wandCasting)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),new ResearchPage(recipe))
                                //.setHidden()
                                //.setVirtual()
                                //.setConcealed()
                                //.setItemTriggers(staffcore)
                        .setSpecial()
                        .setConcealed()
                        .setParents(String.format("ROD_ESS_%s_staff",name))
                        .registerResearchItem();

                ThaumcraftApi.addWarpToResearch(researchKey, 5);
            }


            {
                String researchKey = String.format("ROD_ESS_Ex_%s_staff",nameEx);


                ItemStack wandCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);

                {
                    WandCap cap = WandCap.caps.get(String.format("ESS_%s",nameEx));
                    ((ItemWandCasting) wandCasting.getItem()).setCap(wandCasting, cap);

                    WandRod rod = WandRod.rods.get(String.format("ESS_Ex_%s_staff",nameEx));
                    ((ItemWandCasting) wandCasting.getItem()).setRod(wandCasting, rod);

                    Aspect aspect;
                    for (Iterator i$ = Aspect.getPrimalAspects().iterator();i$.hasNext();) {
                        aspect = (Aspect) i$.next();

                        ((ItemWandCasting) wandCasting.getItem()).addVis(wandCasting, aspect, ((ItemWandCasting) wandCasting.getItem()).getMaxVis(wandCasting), true);
                    }
                }


                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, wandCasting.copy(), 10,
                        new AspectList()
                                .add(Aspect.ORDER, 64)
                                .add(Aspect.ENTROPY, 64)
                                .add(Aspect.FIRE, 64)
                                .add(Aspect.EARTH, 64)
                                .add(Aspect.WATER, 64)
                                .add(Aspect.AIR, 64)
                                .add(Aspect.LIGHT, 128)
                                .add(Aspect.MAGIC, 64),
                        staffcoreEx.copy(),
                        new ItemStack[]{
                                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                                new ItemStack(Items.nether_star),
                                new ItemStack(crystal, 1,4),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(Items.nether_star),
                                new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(crystal, 1, 4),
                                new ItemStack(Items.nether_star),
                        });

                int localOffsetX = 2;
                int localOffsetY = 8;

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.LIGHT, 5).add(Aspect.ENTROPY, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.AURA, 5).add(Aspect.SENSES, 5)
                        ,offsetCol + localOffsetX, offsetRow + localOffsetY, 2, wandCasting)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),new ResearchPage(recipe))
                                //.setHidden()
                                //.setVirtual()
                                //.setConcealed()
                                //.setItemTriggers(staffcore)
                        .setLost()
                        .setSpecial()
                        .setRound()
                        .setConcealed()
                        .setParents(String.format("ROD_ESS_%s_staff", name))
                        .registerResearchItem();
            }
        }



        {
            int offsetCol = 10;
            int offsetRow = 0;

            ExtraMaterials mat = ExtraMaterials.Alicorn;
            String name = mat.name;
            int index = mat.index;


            ItemStack material = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.Material));
            ItemStack fragment = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.Fragment));
            ItemStack wandcap = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.WandCap));
            ItemStack wandrod = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.WandRod));
            ItemStack staffcore = new ItemStack(itemResource,1, ItemResource.getItemDamage(index, ItemResource.ResourceType.StaffRod));

            {
                ThaumcraftApi.registerObjectTag(material, new AspectList()
                        .add(Aspect.BEAST, 2)
                        .add(Aspect.FLIGHT, 2)
                        .add(Aspect.ORDER, 2)
                        .add(Aspect.MAGIC, 3)
                        .add(Aspect.HEAL, 3)
                );

                ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST,new WeightedRandomChestContent(material,1,1,3));
                ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST,new WeightedRandomChestContent(fragment,1,2,3));
                ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST,new WeightedRandomChestContent(fragment,1,1,1));
            }


            {
                String researchKey = String.format("ESS_%s_fragment",name);
                ItemStack resultFragment = fragment.copy();
                resultFragment.stackSize = 3;
                CrucibleRecipe fragmentRecipe = ThaumcraftApi.addCrucibleRecipe(researchKey , resultFragment,
                        material,
                        new AspectList()
                                .add(Aspect.ENTROPY,2));

                CrucibleRecipe crucibleRecipe = ThaumcraftApi.addCrucibleRecipe(researchKey , material,
                        fragment,
                        new AspectList()
                                .add(Aspect.BEAST, 16)
                                .add(Aspect.FLIGHT, 16)
                                .add(Aspect.ORDER, 24)
                                .add(Aspect.MAGIC, 8)
                                .add(Aspect.SENSES, 4)
                                .add(Aspect.HEAL, 24));

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.BEAST, 1).add(Aspect.FLIGHT, 1).add(Aspect.ORDER, 1).add(Aspect.MAGIC, 1)
                        , offsetCol, offsetRow, 1, material)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),
                                new ResearchPage(fragmentRecipe),
                                new ResearchPage(crucibleRecipe))
                        .setHidden()
                        .setItemTriggers(material,fragment)
                        .registerResearchItem();
            }

            {
                String researchKey = String.format("ROD_ESS_%s",name);


                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, wandrod.copy(), 3,
                        new AspectList()
                                .add(Aspect.BEAST, 8)
                                .add(Aspect.FLIGHT, 8)
                                .add(Aspect.ORDER, 16)
                                .add(Aspect.MAGIC, 8)
                                .add(Aspect.HEAL, 8),
                        material.copy(),
                        new ItemStack[]{
                                new ItemStack(ConfigItems.itemResource, 1, 14), new ItemStack(crystal, 1, 4)
                            });


                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.TOOL, 2).add(Aspect.BEAST, 2).add(Aspect.FLIGHT, 2).add(Aspect.ORDER, 4).add(Aspect.MAGIC, 2).add(Aspect.HEAL, 2)
                        , offsetCol + 1, offsetRow + 1, 2, wandrod)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),
                                new ResearchPage(recipe))
                        .setSecondary()
                        .setParents(String.format("ESS_%s_fragment", name))
                        .setParentsHidden("ROD_greatwood", "INFUSION")
                        .setConcealed()
                        .registerResearchItem();
            }


            {
                String researchKey = String.format("ROD_ESS_%s_staff",name);

                IArcaneRecipe recipe = ThaumcraftApi.addArcaneCraftingRecipe(researchKey, staffcore,
                        (new AspectList()).add(Aspect.ORDER, 16)
                        , new Object[]{
                        "  S",
                        " G ",
                        "F  ",
                        'S', new ItemStack(ConfigItems.itemResource, 1, 15),
                        'G', wandrod.copy(),
                        'F', fragment.copy()
                });

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.TOOL, 5).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.HEAL, 5)
                        ,offsetCol + 2, offsetRow + 2, 2, staffcore)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),
                                new ResearchPage(recipe))
                        .setSecondary()
                        .setParents(String.format("ROD_ESS_%s",name))
                        .setConcealed()
                        .registerResearchItem();
            }

            {
                String researchKey = String.format("CAP_ESS_%s",name);

                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionRecipe recipe = ThaumcraftApi.addInfusionCraftingRecipe(researchKey, wandcap.copy(), 3,
                        new AspectList()
                                .add(Aspect.BEAST, 8)
                                .add(Aspect.FLIGHT, 8)
                                .add(Aspect.ORDER, 16)
                                .add(Aspect.HEAL, 8),
                        new ItemStack(ConfigItems.itemWandCap, 1, 1),
                        new ItemStack[]{
                                new ItemStack(ConfigItems.itemResource, 1, 14), fragment.copy()
                        });

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.TOOL, 5).add(Aspect.BEAST, 5).add(Aspect.FLIGHT, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.HEAL, 5)
                        ,offsetCol - 2, offsetRow + 2, 2, wandcap)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),
                                new ResearchPage(recipe))
                        .setSecondary()
                        .setParents(String.format("ESS_%s_fragment", name))
                        .setParentsHidden("CAP_gold")
                        .setConcealed()
                        .registerResearchItem();
            }



            {
                String researchKey = String.format("ROD_ESS_Ex_%s_staff",name);
                ItemStack wandCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);
                ((ItemWandCasting) wandCasting.getItem()).setCap(wandCasting, WandCap.caps.get(String.format("ESS_%s",name)));
                ((ItemWandCasting) wandCasting.getItem()).setRod(wandCasting, WandRod.rods.get(String.format("ESS_%s_staff",name)));

                Item crystal = GameRegistry.findItem("Thaumcraft","blockCrystal");

                InfusionExtraScepterStaffRecipe ra = new InfusionExtraScepterStaffRecipe(researchKey,
                        new Object[]{"rod",new NBTTagString("ESS_Ex_"+name+"_staff")},
                        8,
                        new AspectList().add(Aspect.FLIGHT, 16).add(Aspect.ORDER, 32).add(Aspect.BEAST, 16).add(Aspect.MAGIC, 32).add(Aspect.HEAL, 16),
                        wandCasting,
                        new ItemStack[]{
                                new ItemStack(crystal, 1, 0),
                                new ItemStack(Items.bone,1,0),
                                new ItemStack(crystal, 1, 4),
                                new ItemStack(ConfigItems.itemResource, 1, 15),
                                new ItemStack(crystal, 1, 5),
                                new ItemStack(Items.gold_ingot,1,0)
                            });
                ThaumcraftApi.getCraftingRecipes().add(ra);


                ItemStack wandExCasting = new ItemStack(ConfigItems.itemWandCasting, 1, 128);
                ((ItemWandCasting) wandCasting.getItem()).setCap(wandExCasting, WandCap.caps.get(String.format("ESS_%s",name)));
                ((ItemWandCasting) wandCasting.getItem()).setRod(wandExCasting, WandRod.rods.get(String.format("ESS_Ex_%s_staff",name)));

                new ResearchItem(researchKey, categoryKey,
                        new AspectList().add(Aspect.FLIGHT, 5).add(Aspect.ORDER, 8).add(Aspect.MAGIC, 5).add(Aspect.HEAL, 16)
                        ,offsetCol, offsetRow + 4, 2, wandExCasting)
                        .setPages(new ResearchPage("tc.research_page." + researchKey + ".1"),
                                new ResearchPage(ra))
                        .setParents(String.format("ROD_ESS_%s_staff", name), String.format("CAP_ESS_%s", name))
//                        .setParentsHidden("")
                        .setConcealed()
                        .registerResearchItem();
            }
        }
    }
}
