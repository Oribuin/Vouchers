package xyz.oribuin.vouchers.config;

import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.SettingSerializer;
import dev.rosewood.rosegarden.config.SettingHolder;
import org.bukkit.configuration.ConfigurationSection;
import xyz.oribuin.vouchers.VoucherPlugin;

import java.util.ArrayList;
import java.util.List;

import static dev.rosewood.rosegarden.config.SettingSerializers.BOOLEAN;

public final class Settings implements SettingHolder {

    private static final List<RoseSetting<?>> KEYS = new ArrayList<>();

    public static final RoseSetting<Boolean> REDEEM_WHILE_CROUCHING = create(
            "redeem-while-crouching",
            BOOLEAN,
            false,
            "If a voucher can be redeemed while crouching"
    );

    private static <T> RoseSetting<T> create(String key, SettingSerializer<T> serializer, T defaultValue, String... comments) {
        RoseSetting<T> setting = RoseSetting.ofBacked(key, VoucherPlugin.get(), serializer, () -> defaultValue, comments);
        KEYS.add(setting);
        return setting;
    }

    private static RoseSetting<ConfigurationSection> create(String key, String... comments) {
        RoseSetting<ConfigurationSection> setting = RoseSetting.ofBackedSection(key, VoucherPlugin.get(), comments);
        KEYS.add(setting);
        return setting;
    }

    @Override
    public List<RoseSetting<?>> get() {
        return KEYS;
    }
}
