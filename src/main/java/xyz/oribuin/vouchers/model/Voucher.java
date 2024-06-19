package xyz.oribuin.vouchers.model;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.oribuin.vouchers.VoucherPlugin;
import xyz.oribuin.vouchers.action.ActionType;
import xyz.oribuin.vouchers.manager.VoucherManager;
import xyz.oribuin.vouchers.requirement.Requirement;
import xyz.oribuin.vouchers.util.VoucherUtils;

import java.util.ArrayList;
import java.util.List;

public class Voucher {

    public static final NamespacedKey DATA_KEY = new NamespacedKey("vouchers", "uses");

    private final String id;
    private final ItemStack display;
    private List<Requirement> requirements;
    private List<String> commands;
    private List<String> denyCommands;
    private List<String> cooldownActions;
    private int requirementMin;
    private long cooldown;
    private boolean confirmRequired;

    /**
     * Create a new voucher object for caching.
     *
     * @param id      The id of the voucher.
     * @param display The display item of the voucher.
     */
    public Voucher(String id, ItemStack display) {
        this.id = id;
        this.display = this.apply(display);
        this.requirements = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.denyCommands = new ArrayList<>();
        this.cooldownActions = new ArrayList<>();
        this.requirementMin = -1;
        this.cooldown = 0;
        this.confirmRequired = false;
    }

    /**
     * Give the voucher to a player with the modified max uses.
     *
     * @param player The player to give the voucher to.
     */
    public void give(Player player, int amt) {
        ItemStack item = this.display.clone();
        item.setAmount(amt);

        player.getInventory().addItem(item);
    }

    /**
     * Redeem the voucher for a user
     *
     * @param player The player to redeem the voucher for.
     */
    public boolean redeem(Player player) {
        VoucherManager manager = VoucherPlugin.get().getManager(VoucherManager.class);

        // Check if the player is on cooldown
        long cooldown = manager.getCooldown(player.getUniqueId(), this);
        if (cooldown > 0) {
            ActionType.run(player, this.cooldownActions, StringPlaceholders.of(
                    "cooldown", VoucherUtils.formatTime(cooldown)
            ));
            return false;
        }

        // Check if the player meets the requirements
        if (!this.requirements.isEmpty()) {
            int evaluated = (int) this.requirements.stream().filter(x -> x.evaluate(player)).count();

            if (this.requirementMin <= 0) {
                this.requirementMin = this.requirements.size();
            }

            if (evaluated < this.requirementMin) {
                ActionType.run(player, this.denyCommands);
                return false;
            }
        }

        // Run all the commands and actions
        ActionType.run(player, this.commands);

        // Add the cooldown
        if (this.cooldown > 0) {
            manager.addCooldown(player.getUniqueId(), this);
        }

        return true;
    }

    /**
     * Apply the voucher data to an item.
     *
     * @param itemStack The item to apply the voucher data to.
     */
    public ItemStack apply(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(DATA_KEY, PersistentDataType.STRING, this.id.toLowerCase());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public ItemStack getDisplay() {
        return this.display.clone();
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getDenyCommands() {
        return denyCommands;
    }

    public void setDenyCommands(List<String> denyCommands) {
        this.denyCommands = denyCommands;
    }

    public int getRequirementMin() {
        return requirementMin;
    }

    public void setRequirementMin(int requirementMin) {
        this.requirementMin = requirementMin;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public List<String> getCooldownActions() {
        return cooldownActions;
    }

    public void setCooldownActions(List<String> cooldownActions) {
        this.cooldownActions = cooldownActions;
    }

    public boolean isConfirmRequired() {
        return confirmRequired;
    }

    public void setConfirmRequired(boolean confirmRequired) {
        this.confirmRequired = confirmRequired;
    }

}

