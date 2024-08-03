package sakura.kooi.BridgingAnalyzer.api.event;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class VictoryEvent extends Event {

    @Getter
    static HandlerList handlerList = new HandlerList();

    Player player;
    Block block;
    public VictoryEvent(Player p, Block b){
        player = p;
        block = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
