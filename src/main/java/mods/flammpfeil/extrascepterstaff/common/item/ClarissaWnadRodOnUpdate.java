package mods.flammpfeil.extrascepterstaff.common.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Furia on 15/03/02.
 */
public class ClarissaWnadRodOnUpdate implements IWandRodOnUpdate {
    boolean isTrue;
    boolean isExStaff;
    ArrayList<Aspect> primals;

    Set<Integer> ignorePotions = Sets.newHashSet();

    public ClarissaWnadRodOnUpdate(boolean isTrue, boolean isExStaff) {
        this.isTrue = isTrue;
        this.isExStaff = isExStaff;
        this.primals = Aspect.getPrimalAspects();
    }

    @Override
    public void onUpdate(ItemStack itemStack, EntityPlayer entityPlayer) {
        if(entityPlayer.ticksExisted % 50 == 0)
        {
            boolean beaconEffect = hasBeaconEffect(entityPlayer);

            int genVis = beaconEffect ? 10 : 1;

            int max = ((ItemWandCasting)itemStack.getItem()).getMaxVis(itemStack);

            int border = (isTrue | beaconEffect) ? max : max / 2;

            List<Aspect> aspects = Lists.newArrayList();

            for(Aspect as : primals){
                int current = ((ItemWandCasting)itemStack.getItem()).getVis(itemStack, as);
                if(current < border){
                    aspects.add(as);
                }
            }
            if(0 < aspects.size()){
                int index = entityPlayer.getRNG().nextInt(aspects.size());
                Aspect as = aspects.get(index);

                int current = ((ItemWandCasting)itemStack.getItem()).getVis(itemStack, as);
                int addMax = max - current;
                int addVis = Math.min(genVis,addMax);

                ((ItemWandCasting)itemStack.getItem()).addVis(itemStack, as, addVis, true);
            }

            if(isExStaff && (beaconEffect || isTrue)){
                entityPlayer.addPotionEffect(new PotionEffect(Config.potionWarpWardID,200,0,true));
            }

            if(isExStaff && isTrue && beaconEffect){
                PotionEffect[] effects = (PotionEffect[])entityPlayer.getActivePotionEffects().toArray(new PotionEffect[]{});
                for(PotionEffect effect : effects){
                    if(Potion.potionTypes[effect.getPotionID()].isBadEffect()){
                        if(entityPlayer.getRNG().nextFloat() < 0.8f){
                            entityPlayer.removePotionEffect(effect.getPotionID());
                            break;
                        }
                    }
                }
            }
        }

    }


    public boolean hasBeaconEffect(EntityLivingBase user){
        if(user != null){
            PotionEffect[] effects = (PotionEffect[])user.getActivePotionEffects().toArray(new PotionEffect[]{});

            if(ignorePotions.size() == 0 && 0 < effects.length)
            {
                this.ignorePotions.add(Config.potionVisExhaustID);
                this.ignorePotions.add(Config.potionInfVisExhaustID);
                this.ignorePotions.add(Config.potionBlurredID);
                this.ignorePotions.add(Config.potionThaumarhiaID);
                this.ignorePotions.add(Config.potionTaintPoisonID);
                this.ignorePotions.add(Config.potionUnHungerID);
                this.ignorePotions.add(Config.potionSunScornedID);
                this.ignorePotions.add(Config.potionWarpWardID);
                this.ignorePotions.add(Config.potionDeathGazeID);
            }

            for(PotionEffect effect : effects){
                if(!this.ignorePotions.contains(effect.getPotionID()) && effect.getIsAmbient()){
                    return true;
                }
            }
        }

        return false;
    }
}
