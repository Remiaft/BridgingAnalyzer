package sakura.kooi.BridgingAnalyzer.api.event;

import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class PlayerToggleOptionEvent extends Event implements Cancellable {

    @Getter
    static HandlerList handlerList = new HandlerList();

    boolean cancelled;
    @NonNull
    Player player;
    @NonNull
    String option;
    @NonNull
    boolean to;
    @Setter
    String denyMessage;

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
