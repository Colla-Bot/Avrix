package bot.colla.soop;

import com.avrix.events.*;
import com.avrix.plugin.Metadata;
import com.avrix.plugin.Plugin;
import com.avrix.ui.WidgetManager;
import com.avrix.ui.notify.Notify;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaManager;
import zombie.core.Core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Main entry point of the example plugin
 */
public class Soop extends Plugin {
    public static SoopWindow WINDOW;

    public static final Queue<ChatPacket> QUEUE = new ConcurrentLinkedQueue<>();
    
    /**
     * Constructs a new {@link Plugin} with the specified metadata.
     * Metadata is transferred when the plugin is loaded into the game context.
     *
     * @param metadata The {@link Metadata} associated with this plugin.
     */
    public Soop(Metadata metadata) {
        super(metadata);
    }

    /**
     * Called when the plugin is initialized.
     * <p>
     * Implementing classes should override this method to provide the initialization logic.
     */
    @Override
    public void onInitialize() {
        loadDefaultConfig();

        EventManager.addListener(new GameBootHandler());
        EventManager.addListener(new TickHandler());
        EventManager.addListener(new KeyPressedHandler());

        WINDOW = new SoopWindow(10, 100, getDefaultConfig().getBoolean("debug"));
    }

    public static class GameBootHandler extends OnGameBootEvent {
        @Override
        public void handleEvent() {
            if (LuaManager.env.rawget("ApiEffect") instanceof KahluaTable table) {
                // SoopDecoder.ACCEPTED_BALLOONS.clear();
                for (KahluaTableIterator it = table.iterator(); it.advance(); ) {
                    if (it.getKey() instanceof Double n) {
                        // SoopDecoder.ACCEPTED_BALLOONS.add((int) Math.round(n));
                    } else {
                        Notify.error("Not supported ApiEffect key", 5);
                    }
                }
            } else {
                Notify.error("Not supported ApiEffect type", 5);
            }
        }
    }

    public static class TickHandler extends OnTickEvent {
        @Override
        public void handleEvent(Double numberTicks) {
            if (!QUEUE.isEmpty()) {
                ChatPacket packet = QUEUE.poll();
                LuaEventManager.triggerEvent("OnSoopBalloon", packet.type().ordinal(), packet.nickname(), packet.count());
            }
        }
    }

    public static class KeyPressedHandler extends OnKeyPressedEvent {
        @Override
        public void handleEvent(Integer key) {
            if (key == Core.getInstance().getKey("TOGGLE_SOOP_WINDOW")) {
                if (WINDOW != null) {
                    if (!WidgetManager.getWidgetList().contains(WINDOW)) {
                        WINDOW.addToScreen();
                    } else {
                        WINDOW.removeFromScreen();
                    }
                }
            }
        }
    }
}
