package com.safariesp;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.joml.Matrix4f;

public class RenderingUtils {

    public static void drawCustomBox(PoseStack matrices, VertexConsumer consumer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a, boolean throughWalls) {
        Matrix4f posMatrix = matrices.last().pose();

        int red = (int) (r * 255);
        int green = (int) (g * 255);
        int blue = (int) (b * 255);
        int alpha = (int) (a * 255);

        // West
        drawQuad(consumer, posMatrix, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, red, green, blue, alpha);
        // East
        drawQuad(consumer, posMatrix, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        // South
        drawQuad(consumer, posMatrix, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, red, green, blue, alpha);
        // North
        drawQuad(consumer, posMatrix, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, red, green, blue, alpha);
        // Top
        drawQuad(consumer, posMatrix, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, red, green, blue, alpha);
        // Bottom
        drawQuad(consumer, posMatrix, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, red, green, blue, alpha);
    }

    private static void drawQuad(VertexConsumer consumer, Matrix4f posMatrix, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
        putVertex(consumer, posMatrix, x1, y1, z1, r, g, b, a);
        putVertex(consumer, posMatrix, x2, y2, z2, r, g, b, a);
        putVertex(consumer, posMatrix, x3, y3, z3, r, g, b, a);
        putVertex(consumer, posMatrix, x4, y4, z4, r, g, b, a);
    }

    private static void putVertex(VertexConsumer consumer, Matrix4f posMatrix, float x, float y, float z, int r, int g, int b, int a) {
        consumer.addVertex(posMatrix, x, y, z)
                .setColor(r, g, b, a)
                .setLight(15728880);
    }
}