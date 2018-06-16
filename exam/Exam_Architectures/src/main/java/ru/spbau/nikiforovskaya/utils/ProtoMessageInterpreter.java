package ru.spbau.nikiforovskaya.utils;

import com.google.common.primitives.Ints;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ProtoMessageInterpreter {

    public static ProtoMessage.Array readArrayMessage(DataInputStream in, int size) throws IOException {
        byte[] buffer = new byte[size];
        in.readFully(buffer, 0, buffer.length);
        return ProtoMessage.Array.parseFrom(buffer);
    }

    public static ProtoMessage.Array readArrayMessage(byte[] data) throws IOException {
        return ProtoMessage.Array.parseFrom(data);
    }

    public static void writeArrayMessage(DataOutputStream out, int[] array) throws IOException {
        byte[] toSend = ProtoMessage.Array.newBuilder()
                .addAllData(Ints.asList(array))
                .build().toByteArray();
        out.writeInt(toSend.length);
        out.write(toSend);
        out.flush();
    }

    public static ProtoMessage.Settings readSettingsMessage(DataInputStream in, int size) throws IOException {
        byte[] buffer = new byte[size];
        in.readFully(buffer, 0, buffer.length);
        return ProtoMessage.Settings.parseFrom(buffer);
    }

    public static void writeSettingsMessage(DataOutputStream out,
                                       ProtoMessage.Settings.ServerType type, int port) throws IOException {
        byte[] toSend = ProtoMessage.Settings.newBuilder()
                .setType(type).setPort(port)
                .build().toByteArray();
        out.writeInt(toSend.length);
        out.write(toSend);
        out.flush();
    }

    public static ProtoMessage.Timing readTimingMessage(DataInputStream in, int size) throws IOException {
        byte[] buffer = new byte[size];
        in.readFully(buffer, 0, buffer.length);
        return ProtoMessage.Timing.parseFrom(buffer);
    }

    public static void writeTimingMessage(DataOutputStream out, int timeToSort,
                                          int timeToProcess) throws IOException {
        byte[] toSend = ProtoMessage.Timing.newBuilder()
                .setProcessingTime(timeToProcess)
                .setQueryTime(timeToSort).build().toByteArray();
        out.writeInt(toSend.length);
        out.write(toSend);
        out.flush();
    }

    public static byte[] getBytesFromArrayMessage(int[] array) {
        return ProtoMessage.Array.newBuilder()
                .addAllData(Ints.asList(array))
                .build().toByteArray();
    }

    public static byte[] getBytesFromTimingMessage(int timeToSort, int timeToProcess) {
        return ProtoMessage.Timing.newBuilder()
                .setProcessingTime(timeToProcess)
                .setQueryTime(timeToSort).build().toByteArray();
    }
}
