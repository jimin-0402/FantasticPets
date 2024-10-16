package kr.jimin.fantasticpets.config;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.util.MessagesUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public enum Message {

    // general
    PREFIX("general.prefix"),
    RELOAD("general.messages.reload"),
    NOT_ITEM("general.messages.not-item"),
    GIVE_ITEM("general.messages.give-item"),
    GIVEN_ITEM("general.messages.given-item"),

    // Pet Messages
    PET_CATEGORY_PREFIX("pet.category.prefix"),
    PET_HAS("pet.ownership.has"),
    PET_HAS_TARGET("pet.ownership.has-target"),
    PET_HAS_NOT("pet.ownership.has-not"),
    PET_HAS_ALL("pet.ownership.has-all"),
    PET_NOT_FOUND("pet.not-found"),
    PET_ACQUIRED("pet.acquired"),

    // Command Messages
    COMMAND_HELP("command.help"),
    COMMAND_GIVE_PLAYER("command.give.player"),
    COMMAND_GIVE_TARGET("command.give.target"),
    COMMAND_TAKE_PLAYER("command.take.player"),
    COMMAND_TAKE_TARGET("command.take.target");

    private final String path;

    Message(final String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return FantasticPetsPlugin.get().getConfigsManager().getLanguage().getString(path);
    }

    public void send(final CommandSender sender, final TagResolver... placeholders) {
        if (sender == null) return;
        String lang = FantasticPetsPlugin.get().getConfigsManager().getLanguage().getString(path);
        if (lang == null || lang.isEmpty()) return;
        FantasticPetsPlugin.get().getAudience().sender(sender).sendMessage(
                MessagesUtils.MINI_MESSAGE_WITH_TAGS.deserialize(lang, TagResolver.resolver(placeholders))
        );
    }

    @NotNull
    public final Component toComponent() {
        return MessagesUtils.MINI_MESSAGE_WITH_TAGS.deserialize(toString());
    }

    @NotNull
    public String toSerializedString() {
        return MessagesUtils.LEGACY_SERIALIZER.serialize(toComponent());
    }

    public void log(final TagResolver... placeholders) {
        send(Bukkit.getConsoleSender(), placeholders);
    }

}