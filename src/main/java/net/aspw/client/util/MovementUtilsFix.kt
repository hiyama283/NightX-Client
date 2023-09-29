package net.aspw.client.util

import net.aspw.client.Client
import net.aspw.client.features.module.impl.combat.KillAura
import net.aspw.client.features.module.impl.combat.TPAura
import net.aspw.client.features.module.impl.visual.Animations
import net.minecraft.item.ItemSword
import net.minecraft.util.MathHelper
import java.util.*

object MovementUtilsFix : MinecraftInstance() {
    val direction: Double
        get() {
            var rotationYaw = mc.thePlayer.rotationYaw
            if (mc.thePlayer.moveForward < 0f) rotationYaw += 180f
            var forward = 1f
            if (mc.thePlayer.moveForward < 0f) forward =
                -0.5f else if (mc.thePlayer.moveForward > 0f) forward = 0.5f
            if (mc.thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward
            if (mc.thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward
            return Math.toRadians(rotationYaw.toDouble())
        }

    fun isVisualBlocking(): Boolean {
        return Animations.swingAnimValue.get() == "1.7" && !mc.thePlayer.isEating && !mc.thePlayer.isUsingItem && !Objects.requireNonNull(
            Client.moduleManager.getModule(
                TPAura::class.java
            )
        )?.isBlocking!! && !mc.thePlayer.isBlocking && (Objects.requireNonNull(
            Client.moduleManager.getModule(
                KillAura::class.java
            )
        )?.target != null && (Objects.requireNonNull(
            Client.moduleManager.getModule(
                KillAura::class.java
            )
        )?.autoBlockModeValue?.get() == "None" || mc.thePlayer.heldItem.item !is ItemSword) || Objects.requireNonNull(
            Client.moduleManager.getModule(
                KillAura::class.java
            )
        )?.target == null)
    }

    private val funDirection: Double
        get() {
            var rotationYaw: Float = mc.thePlayer.rotationYaw

            if (mc.thePlayer.moveForward < 0) {
                rotationYaw += 180f
            }

            var forward = 1f

            if (mc.thePlayer.moveForward < 0) {
                forward = -0.5f
            } else if (mc.thePlayer.moveForward > 0) {
                forward = 0.5f
            }

            if (mc.thePlayer.moveStrafing > 0) {
                rotationYaw -= 70 * forward
            }

            if (mc.thePlayer.moveStrafing < 0) {
                rotationYaw += 70 * forward
            }

            return Math.toRadians(rotationYaw.toDouble())
        }

    fun theStrafe(speed: Double) {
        if (!MovementUtils.isMoving()) {
            return
        }
        mc.thePlayer.motionX = -MathHelper.sin(funDirection.toFloat()) * speed
        mc.thePlayer.motionZ = MathHelper.cos(funDirection.toFloat()) * speed
    }

    var bps = 0.0
        private set
    private var lastX = 0.0
    private var lastY = 0.0
    private var lastZ = 0.0

    fun updateBlocksPerSecond() {
        if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
            bps = 0.0
        }
        val distance = mc.thePlayer.getDistance(lastX, lastY, lastZ)
        lastX = mc.thePlayer.posX
        lastY = mc.thePlayer.posY
        lastZ = mc.thePlayer.posZ
        bps = distance * (20 * mc.timer.timerSpeed)
    }

    val movingYaw: Float
        get() = (direction * 180f / Math.PI).toFloat()
}
