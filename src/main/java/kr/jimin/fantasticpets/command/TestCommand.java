package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.command.test.TGiveCommand;
import kr.jimin.fantasticpets.config.Message;

public class TestCommand {

    private final FantasticPetsPlugin plugin;

    public TestCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadCommands() {
        new CommandAPICommand("testf")
                .withAliases("tf")
                .withSubcommands(
                        (new TGiveCommand(plugin).getPlayerPetsCommand())
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                })
                .register();
    }
}
