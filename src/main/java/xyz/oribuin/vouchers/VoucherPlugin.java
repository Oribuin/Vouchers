package xyz.oribuin.vouchers;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Bukkit;
import xyz.oribuin.vouchers.listener.VoucherListener;
import xyz.oribuin.vouchers.manager.CommandManager;
import xyz.oribuin.vouchers.manager.ConfigurationManager;
import xyz.oribuin.vouchers.manager.LocaleManager;
import xyz.oribuin.vouchers.manager.VoucherManager;

import java.util.List;

public class VoucherPlugin extends RosePlugin {

    private static VoucherPlugin instance;

    public VoucherPlugin() {
        super(-1, -1,
                ConfigurationManager.class,
                null,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    public static VoucherPlugin getInstance() {
        return instance;
    }

    @Override
    public void enable() {
        Bukkit.getPluginManager().registerEvents(new VoucherListener(this), this);
    }

    @Override
    public void disable() {

    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(VoucherManager.class);
    }

}
