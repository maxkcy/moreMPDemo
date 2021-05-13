package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

import io.vertx.core.http.ServerWebSocket;

public class TouchUpPacket implements Transferable<TouchUpPacket> {


    public TouchUpPacket() {
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

    }

    @Override
    public TouchUpPacket deserialize(Deserializer deserializer) throws SerializationException {
        return new TouchUpPacket();
    }
}
