package xyz.oribuin.vouchers.util;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.manager.LocaleManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class VoucherUtils {

    public static GuiItem BORDER = dev.triumphteam.gui.builder.item.ItemBuilder
            .from(Material.BLACK_STAINED_GLASS_PANE)
            .name(Component.text(" "))
            .asGuiItem();

    public VoucherUtils() {
        throw new IllegalStateException("VouchersUtil is a utility class and cannot be instantiated.");
    }

    /**
     * Get a bukkit color from a hex code
     *
     * @param hex The hex code
     * @return The bukkit color
     */
    public static Color fromHex(String hex) {
        if (hex == null)
            return Color.BLACK;

        java.awt.Color awtColor;
        try {
            awtColor = java.awt.Color.decode(hex);
        } catch (NumberFormatException e) {
            return Color.BLACK;
        }

        return Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
    }


    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection with placeholders
     *
     * @param section      The section to deserialize from
     * @param sender       The CommandSender to apply placeholders from
     * @param key          The key to deserialize from
     * @param placeholders The placeholders to apply
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(
            @NotNull CommentedConfigurationSection section,
            @Nullable CommandSender sender,
            @NotNull String key,
            @NotNull StringPlaceholders placeholders
    ) {
        final LocaleManager locale = VoucherPlugin.get().getManager(LocaleManager.class);
        final Material material = Material.getMaterial(locale.format(sender, section.getString(key + ".material"), placeholders), false);
        if (material == null) return null;

        // Load enchantments
        final Map<Enchantment, Integer> enchantments = new HashMap<>();
        final ConfigurationSection enchantmentSection = section.getConfigurationSection(key + ".enchantments");
        if (enchantmentSection != null) {
            for (String enchantmentKey : enchantmentSection.getKeys(false)) {
                final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey.toLowerCase()));
                if (enchantment == null) continue;

                enchantments.put(enchantment, enchantmentSection.getInt(enchantmentKey, 1));
            }
        }

        // Load potion item flags
        final ItemFlag[] flags = section.getStringList(key + ".flags").stream()
                .map(ItemFlag::valueOf)
                .toArray(ItemFlag[]::new);

        // Load offline player texture
        final String owner = section.getString(key + ".owner");
        OfflinePlayer offlinePlayer = null;
        if (owner != null) {
            if (owner.equalsIgnoreCase("self") && sender instanceof Player player) {
                offlinePlayer = player;
            } else {
                offlinePlayer = NMSUtil.isPaper()
                        ? Bukkit.getOfflinePlayerIfCached(owner)
                        : Bukkit.getOfflinePlayer(owner);
            }
        }

        return new ItemBuilder(material)
                .name(locale.format(sender, section.getString(key + ".name"), placeholders))
                .amount(Math.min(1, section.getInt(key + ".amount", 1)))
                .lore(locale.format(sender, section.getStringList(key + ".lore"), placeholders))
                .flags(flags)
                .glow(section.getBoolean(key + ".glow", false))
                .unbreakable(section.getBoolean(key + ".unbreakable", false))
                .model(toInt(locale.format(sender, section.getString(key + ".model-data", "0"), placeholders)))
                .enchant(enchantments)
                .texture(locale.format(sender, section.getString(key + ".texture"), placeholders))
                .color(fromHex(locale.format(sender, section.getString(key + ".potion-color"), placeholders)))
                .owner(offlinePlayer)
                .build();
    }

    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection
     *
     * @param section The section to deserialize from
     * @param key     The key to deserialize from
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(@NotNull CommentedConfigurationSection section, @NotNull String key) {
        return deserialize(section, null, key, StringPlaceholders.empty());
    }

    /**
     * Deserialize an ItemStack from a CommentedConfigurationSection with placeholders
     *
     * @param section The section to deserialize from
     * @param sender  The CommandSender to apply placeholders from
     * @param key     The key to deserialize from
     * @return The deserialized ItemStack
     */
    @Nullable
    public static ItemStack deserialize(@NotNull CommentedConfigurationSection section, @Nullable CommandSender sender, @NotNull String key) {
        return deserialize(section, sender, key, StringPlaceholders.empty());
    }

    /**
     * Parse an integer from an object safely
     *
     * @param object The object
     * @return The integer
     */
    private static int toInt(String object) {
        try {
            return Integer.parseInt(object);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Convert a string to a duration
     *
     * @param input The input string
     * @return The duration
     */
    public static Duration getTime(String input) {
        if (input == null || input.isEmpty()) return Duration.ZERO;

        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;

        String[] split = input.split(" ");
        for (String s : split) {
            if (s.endsWith("s")) {
                seconds += toInt(s.replace("s", ""));
            } else if (s.endsWith("m")) {
                minutes += toInt(s.replace("m", ""));
            } else if (s.endsWith("h")) {
                hours += toInt(s.replace("h", ""));
            } else if (s.endsWith("d")) {
                days += toInt(s.replace("d", ""));
            }
        }

        return Duration.ofSeconds(seconds).plusMinutes(minutes).plusHours(hours).plusDays(days);

    }

    /**
     * Format a time in milliseconds into a string
     *
     * @param time Time in milliseconds
     * @return Formatted time
     */
    public static String formatTime(long time) {
        long totalSeconds = time / 1000;
        if (totalSeconds <= 0) return "";

        long days = (int) Math.floor(totalSeconds / 86400.0);
        totalSeconds %= 86400;

        long hours = (int) Math.floor(totalSeconds / 3600.0);
        totalSeconds %= 3600;

        long minutes = (int) Math.floor(totalSeconds / 60.0);
        long seconds = (totalSeconds % 60);

        final StringBuilder builder = new StringBuilder();
        if (days > 0) builder.append(days).append("d, ");
        if (hours > 0) builder.append(hours).append("h, ");
        if (minutes > 0) builder.append(minutes).append("m, ");
        if (seconds > 0) builder.append(seconds).append("s");
        return builder.toString();
    }


    /**
     * Create a file in a folder from the plugin's resources
     *
     * @param rosePlugin The plugin
     * @param folders    The folders
     * @return The file
     */
    @NotNull
    public static File createFile(@NotNull RosePlugin rosePlugin, @NotNull String... folders) {
        File file = new File(rosePlugin.getDataFolder(), String.join("/", folders)); // Create the file
        if (file.exists())
            return file;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        String path = String.join("/", folders);
        try (InputStream stream = rosePlugin.getResource(path)) {
            if (stream == null) {
                file.createNewFile();
                return file;
            }

            Files.copy(stream, Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Get an enum from a string value
     *
     * @param enumClass The enum class
     * @param name      The name of the enum
     * @param <T>       The enum type
     * @return The enum
     */
    public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String name, T def) {
        if (name == null)
            return def;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return def;
    }


}
