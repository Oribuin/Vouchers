package xyz.oribuin.vouchers.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
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
import xyz.oribuin.vouchers.model.Voucher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static xyz.oribuin.vouchers.util.VoucherUtils.BORDER;

public class ListCommand extends BaseRoseCommand {

    public ListCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, Integer page) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Plugin Vouchers"))
                .disableAllInteractions()
                .rows(5)
                .create();

        VoucherManager manager = this.rosePlugin.getManager(VoucherManager.class);
        for (int i = 0; i < 9; i++) gui.setItem(i, BORDER);
        for (int i = 36; i < 44; i++) gui.setItem(i, BORDER);

        List<Voucher> vouchers = new ArrayList<>(manager.getVouchers().values());
        vouchers = vouchers.stream()
                .sorted(Comparator.comparing(Voucher::getId))
                .toList();

        vouchers.forEach(voucher -> {
            ItemStack item = voucher.getDisplay();

            GuiItem guiItem = new GuiItem(item, event -> event.getWhoClicked()
                    .getInventory()
                    .addItem(voucher.getDisplay())
            );

            gui.addItem(guiItem);
        });

        gui.setItem(41, ItemBuilder.from(Material.ARROW).name(Component.text("Next Page")).asGuiItem(event -> gui.next()));
        gui.setItem(39, ItemBuilder.from(Material.ARROW).name(Component.text("Previous Page")).asGuiItem(event -> gui.previous()));
        gui.open((HumanEntity) context.getSender(), page == null ? 1 : page);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("list")
                .descriptionKey("command-list-description")
                .permission("vouchers.list")
                .playerOnly(true)
                .arguments(ArgumentsDefinition.builder()
                        .optional("page", ArgumentHandlers.INTEGER)
                        .build())
                .build();
    }

}
