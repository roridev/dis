package org.siscode.dis.mixin;

import kotlinx.coroutines.channels.Channel;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.siscode.dis.DisServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;
import java.util.function.Function;

import static org.siscode.dis.utils.functional.ChannelFunctionalKt.trySend;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerMessageMixin {
    @Shadow public ServerPlayerEntity player;

    @Redirect(method = "handleMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
    public void chatMessage(PlayerManager instance, Text txt, Function<ServerPlayerEntity, Text> playerMessageFactory, MessageType playerMessageType, UUID sender) {
        Channel<Text> mcIn = DisServer.Companion.getMCIn();
        trySend(mcIn, txt);
    }
}
