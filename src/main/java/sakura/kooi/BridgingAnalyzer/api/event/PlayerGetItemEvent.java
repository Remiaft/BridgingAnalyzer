package sakura.kooi.BridgingAnalyzer.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@Getter @AllArgsConstructor
public class PlayerGetItemEvent extends Event {

    @Getter
    static HandlerList handlerList = new HandlerList();

    Player player;
    @Setter
    ItemStack[] itemStacks;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
