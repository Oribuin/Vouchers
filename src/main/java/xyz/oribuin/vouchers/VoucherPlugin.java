package xyz.oribuin.vouchers;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.SettingHolder;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.vouchers.config.Settings;
import xyz.oribuin.vouchers.gui.MenuProvider;
import xyz.oribuin.vouchers.listener.VoucherListener;
import xyz.oribuin.vouchers.manager.CommandManager;
import xyz.oribuin.vouchers.manager.LocaleManager;
import xyz.oribuin.vouchers.manager.VoucherManager;

import java.util.List;

public class VoucherPlugin extends RosePlugin {

    private static VoucherPlugin instance;

    public VoucherPlugin() {
        super(114633, 20798,
                null,
                LocaleManager.class,
                CommandManager.class
        );

        instance = this;
    }

    public static VoucherPlugin get() {
        return instance;
    }

    @Override
    public void enable() {
        Bukkit.getPluginManager().registerEvents(new VoucherListener(this), this);
    }

    @Override
    public void reload() {
        super.reload();

        MenuProvider.reload();
    }

    @Override
    public void disable() {

    }

    @Override
    protected @NotNull List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(VoucherManager.class);
    }

    @Override
    protected @Nullable SettingHolder getRoseConfigSettingHolder() {
        return new Settings();
    }
}
