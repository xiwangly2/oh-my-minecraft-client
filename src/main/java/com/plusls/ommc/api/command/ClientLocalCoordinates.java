package com.plusls.ommc.api.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// Modified from brigadier
public class ClientLocalCoordinates implements ClientCoordinates {
    private final double left;
    private final double up;
    private final double forwards;

    public ClientLocalCoordinates(double left, double up, double forwards) {
        this.left = left;
        this.up = up;
        this.forwards = forwards;
    }

    @Override
    public Vec3 getPosition(@NotNull FabricClientCommandSource commandSourceStack) {
        Vec2 rotation = commandSourceStack.getRotation();
        Vec3 pos = ClientEntityAnchorArgument.Anchor.FEET.apply(commandSourceStack);
        float f = Mth.cos((rotation.y + 90.0F) * (float) (Math.PI / 180.0));
        float g = Mth.sin((rotation.y + 90.0F) * (float) (Math.PI / 180.0));
        float h = Mth.cos(-rotation.x * (float) (Math.PI / 180.0));
        float i = Mth.sin(-rotation.x * (float) (Math.PI / 180.0));
        float j = Mth.cos((-rotation.x + 90.0F) * (float) (Math.PI / 180.0));
        float k = Mth.sin((-rotation.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3 vec32 = new Vec3(f * h, i, g * h);
        Vec3 vec33 = new Vec3(f * j, k, g * j);
        Vec3 vec34 = vec32.cross(vec33).scale(-1.0);
        double d = vec32.x * this.forwards + vec33.x * this.up + vec34.x * this.left;
        double e = vec32.y * this.forwards + vec33.y * this.up + vec34.y * this.left;
        double l = vec32.z * this.forwards + vec33.z * this.up + vec34.z * this.left;
        return new Vec3(pos.x + d, pos.y + e, pos.z + l);
    }

    @Override
    public Vec2 getRotation(FabricClientCommandSource commandSourceStack) {
        return Vec2.ZERO;
    }

    @Override
    public boolean isXRelative() {
        return true;
    }

    @Override
    public boolean isYRelative() {
        return true;
    }

    @Override
    public boolean isZRelative() {
        return true;
    }

    @Contract("_ -> new")
    public static @NotNull ClientLocalCoordinates parse(@NotNull StringReader stringReader) throws CommandSyntaxException {
        int i = stringReader.getCursor();
        double d = readDouble(stringReader, i);
        if (stringReader.canRead() && stringReader.peek() == ' ') {
            stringReader.skip();
            double e = readDouble(stringReader, i);
            if (stringReader.canRead() && stringReader.peek() == ' ') {
                stringReader.skip();
                double f = readDouble(stringReader, i);
                return new ClientLocalCoordinates(d, e, f);
            } else {
                stringReader.setCursor(i);
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader);
            }
        } else {
            stringReader.setCursor(i);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader);
        }
    }

    private static double readDouble(@NotNull StringReader stringReader, int i) throws CommandSyntaxException {
        if (!stringReader.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(stringReader);
        } else if (stringReader.peek() != '^') {
            stringReader.setCursor(i);
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader);
        } else {
            stringReader.skip();
            return stringReader.canRead() && stringReader.peek() != ' ' ? stringReader.readDouble() : 0.0;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ClientLocalCoordinates)) {
            return false;
        } else {
            ClientLocalCoordinates clientLocalCoordinates = (ClientLocalCoordinates) object;
            return this.left == clientLocalCoordinates.left && this.up == clientLocalCoordinates.up && this.forwards == clientLocalCoordinates.forwards;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.up, this.forwards);
    }
}
