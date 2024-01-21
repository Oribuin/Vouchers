package xyz.oribuin.vouchers.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.List;

public class VoucherCommandWrapper extends RoseCommandWrapper {

    public VoucherCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "vouchers";
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of("vouch");
    }

    @Override
    public List<String> getCommandPackages() {
        return List.of("xyz.oribuin.vouchers.command.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return true;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }

}
