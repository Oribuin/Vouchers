package xyz.oribuin.vouchers.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.vouchers.manager.VoucherManager;

public class ListCommand extends RoseCommand {

    public ListCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, @Optional int page) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Plugin Vouchers"))
                .disableAllInteractions()
                .rows(5)
                .create();

        VoucherManager manager = this.rosePlugin.getManager(VoucherManager.class);
        GuiItem border = ItemBuilder.from(Material.BLACK_STAINED_GLASS).name(Component.text(" ")).asGuiItem();
        for (int i = 0; i < 8; i++) gui.setItem(i, border);
        for (int i = 36; i < 44; i++) gui.setItem(i, border);

        manager.getVouchers().forEach((string, voucher) -> {
            ItemStack item = voucher.getDisplay();

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> event
                    .getWhoClicked()
                    .getInventory()
                    .addItem(voucher.getDisplay())
            );

            gui.addItem(guiItem);
        });

        gui.setItem(41, ItemBuilder.from(Material.ARROW).name(Component.text("Next Page")).asGuiItem(event -> gui.next()));
        gui.setItem(39, ItemBuilder.from(Material.ARROW).name(Component.text("Previous Page")).asGuiItem(event -> gui.previous()));
        gui.open((HumanEntity) context.getSender(), page);
    }

    @Override
    protected String getDefaultName() {
        return "list";
    }

    @Override
    public String getDescriptionKey() {
        return "command-list-description";
    }

    @Override
    public String getRequiredPermission() {
        return "vouchers.list";
    }

}
