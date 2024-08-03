package sakura.kooi.BridgingAnalyzer.api.event;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerFallVoidEvent extends Event {

    @Getter
    static HandlerList handlerList = new HandlerList();

    Player player;
    Player damager;
    Long lastHitTime;
    public PlayerFallVoidEvent(Player p, Player damager,Long time){
        player = p;
        this.damager = damager;
        lastHitTime = time;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
