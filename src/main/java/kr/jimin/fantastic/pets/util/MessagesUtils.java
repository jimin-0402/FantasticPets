package kr.jimin.fantastic.pets.util;

import kr.jimin.fantastic.pets.config.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessagesUtils {

    private MessagesUtils() {
    }

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static final TagResolver FurryFriendsTagResolver = TagResolver.resolver(TagResolver.standard(),
            TagResolver.resolver("prefix", Tag.selfClosingInserting(MINI_MESSAGE.deserialize(Message.PREFIX.toString())))
    );

    public static final MiniMessage MINI_MESSAGE_WITH_TAGS = MiniMessage.builder().tags(FurryFriendsTagResolver).build();

    public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component processMessage(String message) {
        if (message.contains("&") || message.contains("§")) {
            String legacyContent = message.replace("&", "§");
            return LEGACY_SERIALIZER.deserialize(legacyContent);
        } else {
            return MINI_MESSAGE_WITH_TAGS.deserialize(message);
        }
    }

    public static TagResolver tagResolver(String string, Component processMessage, TagResolver tagResolver) {
        return TagResolver.resolver(string != null ? string : "",
                Tag.selfClosingInserting(processMessage(string != null ? string : "")));
    }

    public static TagResolver tagResolver(String string, String tag) {
        return TagResolver.resolver(string, Tag.selfClosingInserting(Component.text(tag)));
    }

    public static TagResolver tagResolver(String string, Component tag) {
        return TagResolver.resolver(string, Tag.selfClosingInserting(tag));
    }
}
