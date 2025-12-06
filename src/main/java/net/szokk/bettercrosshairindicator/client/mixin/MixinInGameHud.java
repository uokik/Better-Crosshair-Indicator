package net.szokk.bettercrosshairindicator.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.szokk.bettercrosshairindicator.config.CrosshairConfig;
import net.szokk.bettercrosshairindicator.gui.CrosshairConfigScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderCrosshair", at = @At("TAIL"))
    private void drawCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        boolean isConfigOpen = this.client.currentScreen instanceof CrosshairConfigScreen;

        if (!isConfigOpen && this.client.currentScreen != null) {
            return;
        }

        if (!isConfigOpen) {
            HitResult hitResult = this.client.crosshairTarget;

            if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY) {
                return;
            }

            EntityHitResult entityHit = (EntityHitResult) hitResult;
            Entity targetEntity = entityHit.getEntity();

            if (!(targetEntity instanceof PlayerEntity)) {
                return;
            }
        }


        CrosshairConfig config = CrosshairConfig.getInstance();
        int size = config.getSize();
        int color = config.getColorARGB();

        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();

        int centerX = (w - 1) / 2;
        int centerY = (h - 1) / 2;

        int gap = size;
        int minGap = 2;
        int maxLen = 3;

        int len = Math.min(maxLen, gap - minGap + 1);
        len = Math.max(len, 1);

        drawCorner(context, centerX, centerY, -gap, -gap, len, 1, 1, color,
                config.getTopLeftOffsetX(), config.getTopLeftOffsetY());

        drawCorner(context, centerX, centerY, gap, -gap, len, -1, 1, color,
                config.getTopRightOffsetX(), config.getTopRightOffsetY());

        drawCorner(context, centerX, centerY, -gap, gap, len, 1, -1, color,
                config.getBottomLeftOffsetX(), config.getBottomLeftOffsetY());

        drawCorner(context, centerX, centerY, gap, gap, len, -1, -1, color,
                config.getBottomRightOffsetX(), config.getBottomRightOffsetY());
    }

    private void drawCorner(DrawContext ctx, int cx, int cy, int offX, int offY, int len, int dirX, int dirY, int color, int cornerOffsetX, int cornerOffsetY) {
        int cornerX = cx + offX + cornerOffsetX;
        int cornerY = cy + offY + cornerOffsetY;

        for (int i = 0; i < len; i++) {
            int px = cornerX + (dirX * i);
            ctx.fill(px, cornerY, px + 1, cornerY + 1, color);
        }

        for (int i = 0; i < len; i++) {
            int py = cornerY + (dirY * i);
            ctx.fill(cornerX, py, cornerX + 1, py + 1, color);
        }
    }
}
