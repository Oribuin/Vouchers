package xyz.oribuin.vouchers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.oribuin.vouchers.manager.CommandManager;
import xyz.oribuin.vouchers.manager.ConfigurationManager;
import xyz.oribuin.vouchers.manager.LocaleManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import xyz.oribuin.vouchers.manager.VoucherManager;

import java.util.List;

public class VoucherPlugin extends RosePlugin {

    private static VoucherPlugin instance;

    public static VoucherPlugin getInstance() {
        return instance;
    }

    public VoucherPlugin() {
        super(-1, -1,
                ConfigurationManager.class,
                null,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(VoucherManager.class);
    }

}
