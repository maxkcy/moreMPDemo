package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

import io.vertx.core.http.ServerWebSocket;

public class TouchDownPacket implements Transferable<TouchDownPacket> {

    float x;
    float y;

    public TouchDownPacket(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public TouchDownPacket() {
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    ServerWebSocket serverWebSocket; //unitialized. this comment to show how pointers work.

    public void setServerWebSocket(ServerWebSocket serverWebSocket) {
        this.serverWebSocket = serverWebSocket;
    }

    public ServerWebSocket getServerWebSocket() {
        return serverWebSocket;
    }



    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeFloat(x);
        serializer.serializeFloat(y);
    }

    @Override
    public TouchDownPacket deserialize(Deserializer deserializer) throws SerializationException {
        return new TouchDownPacket(deserializer.deserializeFloat(), deserializer.deserializeFloat());
    }
}
