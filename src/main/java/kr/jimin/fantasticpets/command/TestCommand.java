package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.command.test.GiveCommand;
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
                        (new GiveCommand(plugin).getPlayerPetsCommand())
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                })
                .register();
    }
}
