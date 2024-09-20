package xyz.oribuin.vouchers.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import xyz.oribuin.vouchers.command.command.GiveCommand;
import xyz.oribuin.vouchers.command.command.ListCommand;

public class BaseCommand extends BaseRoseCommand {

    public BaseCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("vouchers")
                .descriptionKey("command-help-description")
                .aliases("vouch")
                .arguments(ArgumentsDefinition.builder()
                        .requiredSub(
                                new GiveCommand(this.rosePlugin),
                                new ListCommand(this.rosePlugin)
                        ))
                .build();
    }


}
