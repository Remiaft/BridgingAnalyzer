package sakura.kooi.BridgingAnalyzer;

import sakura.kooi.BridgingAnalyzer.utils.ActionBarUtils;
import sakura.kooi.BridgingAnalyzer.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class CounterListener implements Listener {
    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        if (e.getPlayer() != null) if (!BridgingAnalyzer.isPlacedByPlayer(e.getBlock())) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction().toString().contains("CLICK")) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) if (e.isCancelled()) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.countCPS();
            if (!c.isSpeedCountEnabled()) return;
            ActionBarUtils.sendActionBar(e.getPlayer(),
                    "§c§l最大CPS - " + c.getMaxCPS() + " §d§l当前CPS - " + c.getCPS() + " §a§l| §c§l最远距离 - " + c.getMaxBridgeLength() + " §d§l当前距离 - " + c.getBridgeLength());
        }
    }

    @EventHandler
    public void onFallDown(PlayerMoveEvent e) {
        if (e.getTo().getY() < 0) {
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            if (c.isSpeedCountEnabled() && !c.isPvPEnabled()) {
                TitleUtils.sendTitle(e.getPlayer(), "", "§cMax - " + c.getMaxBridgeSpeed() + " block/s", 1, 40, 1);
            }
            c.reset();
            //BridgingAnalyzer.teleportCheckPoint(e.getPlayer());
        }
    }

    @EventHandler
    public void onLiqudFlow(BlockFromToEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.countBridge(e.getBlock());
            if (c.isSpeedCountEnabled()) {
                TitleUtils.sendTitle(e.getPlayer(), "", "§b" + c.getBridgeSpeed() + " block/s", 1, 40, 1);
            }
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> e.getPlayer().getInventory().addItem(new ItemStack(e.getPlayer().getItemInHand().getType(), 1,  e.getPlayer().getItemInHand().getDurability())),1L);
        }
    }

    @EventHandler
    public void onPlaceLiqud(PlayerBucketEmptyEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() != null) {
            if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
            Counter c = BridgingAnalyzer.getCounter(e.getPlayer());
            c.addLogBlock(e.getBlockClicked().getRelative(e.getBlockFace()));
            Bukkit.getScheduler().runTaskLater(BridgingAnalyzer.getInstance(), () -> e.getPlayer().getInventory().remove(Material.BUCKET), 1);
        }
    }
}
