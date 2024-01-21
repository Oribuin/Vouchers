package xyz.oribuin.vouchers.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedConfigurationSection;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.oribuin.vouchers.model.Voucher;
import xyz.oribuin.vouchers.requirement.Requirement;
import xyz.oribuin.vouchers.requirement.RequirementType;
import xyz.oribuin.vouchers.util.VoucherUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherManager extends Manager {

    private final Map<String, Voucher> vouchers = new HashMap<>();

    public VoucherManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.vouchers.clear();

        // Load all the vouchers from the config
        File voucherFile = VoucherUtils.createFile(this.rosePlugin, "vouchers.yml");
        CommentedFileConfiguration voucherConfig = CommentedFileConfiguration.loadConfiguration(voucherFile);
        CommentedConfigurationSection voucherSection = voucherConfig.getConfigurationSection("vouchers");
        if (voucherSection == null) {
            this.rosePlugin.getLogger().warning("Unable to load vouchers from config. No vouchers section found.");
            return;
        }

        List<String> keys = new ArrayList<>(voucherSection.getKeys(false));
        if (keys.isEmpty()) {
            this.rosePlugin.getLogger().warning("Unable to load vouchers from config. No vouchers found.");
            return;
        }

        keys.forEach(key -> this.load(voucherSection, key));
    }

    /**
     * Load a voucher from a configuration section and cache it.
     *
     * @param section The configuration section to load from.
     */
    public void load(CommentedConfigurationSection section, String key) {
        ItemStack display = VoucherUtils.deserialize(section, key + ".item");

        if (display == null) {
            this.rosePlugin.getLogger().warning("Unable to load voucher item with key '" + key + "'.");
            return;
        }

        // Load all the basic easy values from the config
        Voucher voucher = new Voucher(key, display);

        // Load all the requirements from the config
        List<Requirement> requirements = new ArrayList<>();
        ConfigurationSection requirementSection = section.getConfigurationSection(key + ".requirements");
        if (requirementSection != null) {
            requirementSection.getKeys(false).forEach(id -> {
                CommentedConfigurationSection requirementConfig = section.getConfigurationSection("requirements." + id);
                if (requirementConfig == null) return;

                String type = requirementConfig.getString("type");
                Object input = requirementConfig.get("input");

                if (type == null || input == null) {
                    this.rosePlugin.getLogger().warning("Unable to load requirement '" + id + "' for voucher '" + id + "'. Invalid type or input.");
                    return;
                }

                Requirement requirement = RequirementType.create(type, input);
                requirements.add(requirement);
            });

            voucher.setRequirements(requirements);
        }

        // Load all the basic easy values from the config
        voucher.setRequirementMin(section.getInt(key + ".requirement-min", requirements.size()));
        voucher.setCommands(section.getStringList(key + ".commands"));

        this.vouchers.put(key.toLowerCase(), voucher);
    }

    /**
     * Get a voucher from the cache.
     *
     * @param id The id of the voucher.
     * @return The voucher object.
     */
    public Voucher getVoucher(String id) {
        return this.vouchers.get(id.toLowerCase());
    }

    /**
     * Get a voucher from the cache.
     *
     * @param item The item to get the voucher from.
     * @return The voucher object.
     */
    public Voucher getVoucher(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String id = container.get(Voucher.DATA_KEY, PersistentDataType.STRING);
        if (id == null) return null;

        return this.vouchers.get(id.toLowerCase());
    }

    public Map<String, Voucher> getVouchers() {
        return vouchers;
    }

    @Override
    public void disable() {

    }

}
