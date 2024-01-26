package infusioncore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GiveEffects implements Listener {
    private InfusionCore plugin;

    public GiveEffects(InfusionCore plugin) {
        this.plugin = plugin;
    }

    private Map<UUID, List<PotionEffect>> playerEffects = new HashMap<>();
    private List<PotionEffectType> positiveEffects = new ArrayList<>();
    private List<PotionEffectType> negativeEffects = new ArrayList<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        // Populate positiveEffects and negativeEffects if they're empty
        if (positiveEffects.isEmpty() || negativeEffects.isEmpty()) {
            initializeEffects();
        }

        // Save effects of the killed player
        saveEffects(victim);

        if (victim.getKiller() != null) {
            Player killer = victim.getKiller();

            // Check if the player already has a negative effect
            for (PotionEffect effect : killer.getActivePotionEffects()) {
                if (negativeEffects.contains(effect.getType())) {
                    negativeEffects.remove(effect.getType());
                }
            }

            // Apply a random positive effect if available
            if (!positiveEffects.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(positiveEffects.size());
                PotionEffectType selectedEffect = positiveEffects.get(randomIndex);

                // Apply the selected effect
                int duration = -1; // 1 minute (20 ticks/second * 60 seconds)
                int amplifier = 0; // Effect strength
                killer.addPotionEffect(new PotionEffect(selectedEffect, duration, amplifier));

                // Remove the applied effect from the list to avoid duplication
                positiveEffects.remove(selectedEffect);
            } else {
                // If there are no positive effects, add a random negative effect
                applyRandomNegativeEffect(killer);
            }
        }
    }

    private void applyRandomNegativeEffect(Player player) {
        if (!negativeEffects.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(negativeEffects.size());
            PotionEffectType selectedEffect = negativeEffects.get(randomIndex);

            // Apply the selected effect
            int duration = -1; // 1 minute (20 ticks/second * 60 seconds)
            int amplifier = 0; // Effect strength
            player.addPotionEffect(new PotionEffect(selectedEffect, duration, amplifier));

            // Remove the applied effect from the list to avoid duplication
            negativeEffects.remove(selectedEffect);
        }
    }

    private void initializeEffects() {
        positiveEffects.add(PotionEffectType.WATER_BREATHING);
        positiveEffects.add(PotionEffectType.CONDUIT_POWER);
        positiveEffects.add(PotionEffectType.DOLPHINS_GRACE);
        positiveEffects.add(PotionEffectType.FIRE_RESISTANCE);
        positiveEffects.add(PotionEffectType.FAST_DIGGING);
        positiveEffects.add(PotionEffectType.HEALTH_BOOST);
        positiveEffects.add(PotionEffectType.HERO_OF_THE_VILLAGE);
        positiveEffects.add(PotionEffectType.INVISIBILITY);
        positiveEffects.add(PotionEffectType.LUCK);
        positiveEffects.add(PotionEffectType.NIGHT_VISION);
        positiveEffects.add(PotionEffectType.REGENERATION);
        positiveEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
        positiveEffects.add(PotionEffectType.SATURATION);


        negativeEffects.add(PotionEffectType.SLOW);
        negativeEffects.add(PotionEffectType.SLOW_DIGGING);
        negativeEffects.add(PotionEffectType.JUMP);
        negativeEffects.add(PotionEffectType.CONFUSION);
        negativeEffects.add(PotionEffectType.BLINDNESS);
        negativeEffects.add(PotionEffectType.HUNGER);
        negativeEffects.add(PotionEffectType.WEAKNESS);
        negativeEffects.add(PotionEffectType.POISON);
        negativeEffects.add(PotionEffectType.GLOWING);
        negativeEffects.add(PotionEffectType.LEVITATION);
        negativeEffects.add(PotionEffectType.UNLUCK);
        negativeEffects.add(PotionEffectType.SLOW_FALLING);
        negativeEffects.add(PotionEffectType.BAD_OMEN);
    }

    private void saveEffects(Player player) {
        List<PotionEffect> savedEffects = new ArrayList<>();

        // Save effects of the killed player
        for (PotionEffect effect : player.getActivePotionEffects()) {
            PotionEffectType type = effect.getType();
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();
            savedEffects.add(new PotionEffect(type, duration, amplifier));
        }

        // Save the effects for the player
        playerEffects.put(player.getUniqueId(), savedEffects);
    }

    private void applyRandomPositiveEffect(Player player) {
        // Check if the player already has a negative effect
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (negativeEffects.contains(effect.getType())) {
                negativeEffects.remove(effect.getType());
            }
        }

        // Exclude effects the player already has
        positiveEffects.removeAll(player.getActivePotionEffects());

        // Apply a random positive effect if available
        if (!positiveEffects.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(positiveEffects.size());
            PotionEffectType selectedEffect = positiveEffects.get(randomIndex);

            // Apply the selected effect
            int duration = -1; // 1 minute (20 ticks/second * 60 seconds)
            int amplifier = 0; // Effect strength
            player.addPotionEffect(new PotionEffect(selectedEffect, duration, amplifier));

            // Remove the applied effect from the list to avoid duplication
            positiveEffects.remove(selectedEffect);
        }
    }



    private void reapplySavedEffects(Player player) {
        // Remove all existing effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Check if the player has saved effects
        if (playerEffects.containsKey(player.getUniqueId())) {
            List<PotionEffect> savedEffects = playerEffects.get(player.getUniqueId());

            // Reapply saved effects
            for (PotionEffect effect : savedEffects) {
                player.addPotionEffect(effect);
                Bukkit.getLogger().info(effect.getType()+": "+effect.getDuration()+"s "+effect.getAmplifier() +"a");
            }
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player respawnedPlayer = event.getPlayer();

        // Reapply saved effects immediately
        respawnedPlayer.getServer().getScheduler().runTaskLater(plugin, () -> reapplySavedEffects(respawnedPlayer), 1L);
    }
}
