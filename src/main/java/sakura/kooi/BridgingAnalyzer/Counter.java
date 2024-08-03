package sakura.kooi.BridgingAnalyzer;

import sakura.kooi.BridgingAnalyzer.api.event.PlayerGetItemEvent;
import sakura.kooi.BridgingAnalyzer.utils.SoundMachine;
import sakura.kooi.BridgingAnalyzer.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import sakura.kooi.BridgingAnalyzer.api.event.PlayerToggleOptionEvent;

import java.util.ArrayList;
import java.util.HashSet;

public class Counter {
    public static HashSet<Block> scheduledBreakBlocks = new HashSet<>();
    private ArrayList<Long>
            counterCPS = new ArrayList<>();
    private int maxCPS = 0;
    private ArrayList<Long> counterBridge = new ArrayList<>();
    private double maxBridge = 0;
    private int currentLength = 0;
    private int maxLength = 0;
    private ArrayList<Block> allBlock = new ArrayList<>();
    private Block lastBlock;
    private Location checkPoint = Bukkit.getWorld("world").getSpawnLocation().add(0.5, 1, 0.5);
    private Player player;

    public boolean setSpeedCountEnabled(boolean b) {
        PlayerToggleOptionEvent event = new PlayerToggleOptionEvent(player, "speedCount",b);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            speedCountEnabled = b;
            return true;
        }
        else if (event.getDenyMessage() != null) player.sendMessage("§b§l搭路练习 §7» §f"+event.getDenyMessage());
        return false;
    }

    @Getter
    private boolean speedCountEnabled = true;

    public boolean setPvPEnabled(boolean b) {
        PlayerToggleOptionEvent event = new PlayerToggleOptionEvent(player, "pvp",b);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            PvPEnabled = b;
            return true;
        }
        else if (event.getDenyMessage() != null) player.sendMessage("§b§l搭路练习 §7» §f"+event.getDenyMessage());
        return false;
    }

    @Getter
    private boolean PvPEnabled = false;

    public boolean setHighlightEnabled(boolean b) {
        PlayerToggleOptionEvent event = new PlayerToggleOptionEvent(player, "highLight",b);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            highlightEnabled = b;
            return true;
        }
        else if (event.getDenyMessage() != null) player.sendMessage("§b§l搭路练习 §7» §f"+event.getDenyMessage());
        return false;
    }

    @Getter
    private boolean highlightEnabled = true;

    public boolean setStandBridgeMarkerEnabled(boolean b) {
        PlayerToggleOptionEvent event = new PlayerToggleOptionEvent(player, "standBridgeMarker",b);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            standBridgeMarkerEnabled = b;
            return true;
        }
        else if (event.getDenyMessage() != null) player.sendMessage("§b§l搭路练习 §7» §f"+event.getDenyMessage());
        return false;
    }

    @Getter
    private boolean standBridgeMarkerEnabled = false;
    public Counter(Player p) {
        player = p;
    }

    public void addLogBlock(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getState().getData());
    }

    public void breakBlock() {
        scheduledBreakBlocks.addAll(allBlock);
        new BreakRunnable(new ArrayList<>(allBlock));
        allBlock.clear();
    }

    public void countBridge(Block block) {
        allBlock.add(block);
        BridgingAnalyzer.getPlacedBlocks().put(block, block.getState().getData());
        if ((lastBlock != null) && ((lastBlock.getY() + 1) != block.getY())) {
            counterBridge.add(System.currentTimeMillis());
            currentLength++;
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        lastBlock = block;
        removeBridgeTimeout();
        getBridgeSpeed();
    }

    public void countCPS() {
        counterCPS.add(System.currentTimeMillis());
        removeCPSTimeout();
        if (counterCPS.size() > maxCPS) {
            maxCPS = counterCPS.size();
        }
    }

    public ArrayList<Block> getAllBlocks() {
        return allBlock;
    }

    public int getBridgeLength() {
        return currentLength;
    }

    public double getBridgeSpeed() {
        double result;
        if (counterBridge.isEmpty()) {
            result = 0.00;
        } else {
            long peri = counterBridge.get(counterBridge.size() - 1) - counterBridge.get(0);
            if (peri > 1000L) {
                result = counterBridge.size() / (peri / 1000.0);
                if (result > maxBridge) {
                    maxBridge = Utils.formatDouble(result);
                }
            } else {
                result = counterBridge.size();
            }
        }
        return Utils.formatDouble(result);
    }

    public int getCPS() {
        return counterCPS.size();
    }

    public int getMaxBridgeLength() {
        return maxLength;
    }

    public double getMaxBridgeSpeed() {
        return maxBridge;
    }

    public int getMaxCPS() {
        return maxCPS;
    }

    public void instantBreakBlock() {
        for (Block b : allBlock) {
            Utils.breakBlock(b);
            BridgingAnalyzer.getPlacedBlocks().remove(b);
        }
        allBlock.clear();
    }

    public void removeBlockRecord(Block b) {
        allBlock.remove(b);
        BridgingAnalyzer.getPlacedBlocks().remove(b);
    }

    private void removeBridgeTimeout() {
        while (!counterBridge.isEmpty() && ((System.currentTimeMillis() - counterBridge.get(0)) > 3000)) {
            counterBridge.remove(0);
        }
    }

    private void removeCPSTimeout() {
        while (!counterCPS.isEmpty() && ((System.currentTimeMillis() - counterCPS.get(0)) > 1000)) {
            counterCPS.remove(0);
        }
    }

    public void reset() {
        counterCPS.clear();
        maxCPS = 0;
        counterBridge.clear();
        maxBridge = 0;
        currentLength = 0;
        breakBlock();
    }

    public void resetMax() {
        maxCPS = 0;
        maxLength = 0;
    }

    public void resetMaxLength() {
        maxLength = 0;
    }

    public void setCheckPoint(Location loc) {
        checkPoint = loc;
        Block target = loc.add(0, -1, 0).getBlock().getRelative(BlockFace.DOWN, 3);
        giveItem(target);

    }
    public void giveItem(Block target){
        BridgingAnalyzer.clearInventory(player);
        if (target.getType() == Material.CHEST) {

            Chest chest = (Chest) target.getState();
            ItemStack[] itemStacks = chest.getBlockInventory().getContents().clone();
            PlayerGetItemEvent event = new PlayerGetItemEvent(player, itemStacks);
            Bukkit.getPluginManager().callEvent(event);
            for (ItemStack stack : event.getItemStacks())
                if (stack != null) {
                    Utils.addItem(player.getInventory(), stack.clone());
                }
            player.getWorld().playSound(player.getLocation(), SoundMachine.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1,
                    1);
        }else {
            player.getInventory().addItem(BridgingAnalyzer.getBlockSkinProvider().provide(player));
        }
    }

    public void teleportCheckPoint() {
        player.teleport(checkPoint);
        Block target = checkPoint.getBlock().getRelative(BlockFace.DOWN, 3);
        giveItem(target);

    }

    public void vectoryBreakBlock() {
        counterCPS.clear();
        counterBridge.clear();
        currentLength = 0;
        for (Block b : allBlock)
            if (b.getType() != Material.AIR) {
                b.setType(Material.SEA_LANTERN);
            }
        BridgingAnalyzer.teleportCheckPoint(player);
        breakBlock();
    }

    public class BreakRunnable implements Runnable {
        BukkitTask task;
        ArrayList<Block> blocks = new ArrayList<>();

        public BreakRunnable(ArrayList<Block> allBlocks) {
            blocks.addAll(allBlocks);
            scheduledBreakBlocks.addAll(blocks);
            if (blocks.isEmpty()) return;
            int tick = 1 + (60 / blocks.size());
            if (tick > 3) {
                tick = 3;
            }
            task = Bukkit.getScheduler().runTaskTimer(BridgingAnalyzer.getInstance(), this, 10, tick);
        }

        @Override
        public void run() {
            if (!blocks.isEmpty()) {
                Block b = null;
                while (!blocks.isEmpty() && ((b == null) || (b.getType() == Material.AIR))) {
                    b = blocks.get(0);
                    scheduledBreakBlocks.remove(b);
                    blocks.remove(0);
                    BridgingAnalyzer.getPlacedBlocks().remove(b);
                }
                if (b != null) {
                    Utils.breakBlock(b);
                    BridgingAnalyzer.getPlacedBlocks().remove(b);
                }
            } else {
                task.cancel();
            }
        }
    }

}
