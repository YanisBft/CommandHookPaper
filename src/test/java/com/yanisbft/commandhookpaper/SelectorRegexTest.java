/**
 * Author: _NewAge
 * https://github.com/NewAgeCZ/CommandHook/blob/master/modules/plugin/src/test/java/org/bitbucket/_newage/commandhook/SelectorRegexTest.java
 */
package com.yanisbft.commandhookpaper;

import com.yanisbft.commandhookpaper.mapping.Mapping;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.server.ServerCommandEvent;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@Disabled
class SelectorRegexTest {
    private final BlockCommandSender sender = mock();
    private final Mapping mapping = mock();
    private final CommandBlockListener listener = new CommandBlockListener(mapping);

    @Test
    void verifyNoInteractionsForMinecraftVanillaCommand() {
        ServerCommandEvent event = new ServerCommandEvent(sender, "/minecraft:say @p");

        listener.onCommandBlockDispatch(event);

        assertFalse(event.isCancelled());
    }

    @Test
    void verifySelectorAtEndIsProcessed() {
        ServerCommandEvent event = new ServerCommandEvent(sender, "broadcast @a");

        listener.onCommandBlockDispatch(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void verifyNonVanillaSelectorIsNotProcessed() {
        ServerCommandEvent event = new ServerCommandEvent(sender, "say @ptrain");

        listener.onCommandBlockDispatch(event);

        assertFalse(event.isCancelled());
    }

    @Test
    void verifySelectorWithEmptyArgumentsIsProcessed() {
        ServerCommandEvent event = new ServerCommandEvent(sender, "give @a[] stone 1");

        listener.onCommandBlockDispatch(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void verifySelectorFollowingWithCommaIsProcessed() {
        ServerCommandEvent event = new ServerCommandEvent(sender, "say Hello @p, there");

        listener.onCommandBlockDispatch(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void verifyComplexSelectorWithNbtArgumentIsProcessed() {
        String selector = "@p[nbt={SelectedItem:{id:\"minecraft:stone_sword\",tag:{display:{Name:'[{\"text\":\"Blade of the Outsider\",\"italic\":false,\"color\":\"dark_gray\",\"bold\":true}]'}}}}]";

        ServerCommandEvent event = new ServerCommandEvent(sender, "give " + selector + " stone 1");

        listener.onCommandBlockDispatch(event);

        assertTrue(event.isCancelled());
    }
}
