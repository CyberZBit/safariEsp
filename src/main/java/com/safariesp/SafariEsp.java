package com.safariesp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI;

import java.util.HashSet;
import java.util.Set;

public class SafariEsp implements ClientModInitializer {
    public static final String MOD_ID = "safariesp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Set<BlockPos> espList = new HashSet<>();

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (client.player != null && client.level != null) {
                espList.clear();

                    for (Entity entity : client.level.entitiesForRendering()) {
                        if (entity instanceof Display.ItemDisplay itemDisplay) {
                            ItemStack item = itemDisplay.getItemStack();
                            if (item.is(Items.STRING)) {
                                espList.add(entity.blockPosition());
                            }
                        }
                    }
            }
        });

        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register((LevelRenderContext context) -> {
            if (espList.isEmpty()) return;

            PoseStack poseStack = context.poseStack();
            Vec3 camPos = context.gameRenderer().getMainCamera().position();

            VertexConsumer boxConsumerWithESP = context.bufferSource().getBuffer(RenderTypes.textBackgroundSeeThrough());
            for (BlockPos pos : espList) {
                AABB box = new AABB(pos);

                float minX = (float) (box.minX - camPos.x);
                float minY = (float) (box.minY - camPos.y);
                float minZ = (float) (box.minZ - camPos.z);
                float maxX = (float) (box.maxX - camPos.x);
                float maxY = (float) (box.maxY - camPos.y);
                float maxZ = (float) (box.maxZ - camPos.z);

                RenderingUtils.drawCustomBox(poseStack, boxConsumerWithESP, minX, minY, minZ, maxX, maxY, maxZ, 1.0f, 0.0f, 0.0f, 0.4f, true);
            }

            if (context.bufferSource() instanceof MultiBufferSource.BufferSource bufferSource) {
                bufferSource.endBatch();
            }
        });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}