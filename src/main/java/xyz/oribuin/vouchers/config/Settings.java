package xyz.oribuin.vouchers.config;

import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.RoseSettingSerializer;
import xyz.oribuin.vouchers.VoucherPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.rosewood.rosegarden.config.RoseSettingSerializers.BOOLEAN;

public final class Settings {

    private static final List<RoseSetting<?>> KEYS = new ArrayList<>();

    public static final RoseSetting<Boolean> REDEEM_WHILE_CROUCHING = create(
            "redeem-while-crouching",
            BOOLEAN,
            false,
            "If a voucher can be redeemed while crouching"
    );

    private static <T> RoseSetting<T> create(String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        RoseSetting<T> setting = RoseSetting.backed(VoucherPlugin.get(), key, serializer, defaultValue, comments);
        KEYS.add(setting);
        return setting;
    }

    private static RoseSetting<CommentedConfigurationSection> create(String key, String... comments) {
        RoseSetting<CommentedConfigurationSection> setting = RoseSetting.backedSection(VoucherPlugin.get(), key, comments);
        KEYS.add(setting);
        return setting;
    }

    public static List<RoseSetting<?>> getKeys() {
        return Collections.unmodifiableList(KEYS);
    }

}
