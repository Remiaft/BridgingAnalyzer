package sakura.kooi.BridgingAnalyzer;

import sakura.kooi.BridgingAnalyzer.api.BlockSkinProvider;
import org.bukkit.SandstoneType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sandstone;

public class DefaultBlockSkinProvider implements BlockSkinProvider {
    @Override
    public ItemStack provide(Player player) {
        return new Sandstone(SandstoneType.SMOOTH).toItemStack(64);
    }
}
