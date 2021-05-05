package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class BluePlayerStatePacket implements Transferable<BluePlayerStatePacket> {

    public static final BluePlayerStatePacket.States[] statesEnumArray = BluePlayerStatePacket.States.values();
    public float x;
    public float y;
    public String clientId;
    BluePlayerStatePacket.States state;

    public BluePlayerStatePacket(States state, float x, float y, String clientId) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.clientId = clientId;
    }

    public BluePlayerStatePacket() {
    }

    public enum States{
        idle,
        running,
        kicking;

        States() {
        }
    }

    public States getState() {
        return state;
    }

    public void setState(BluePlayerStatePacket.States state) {
        this.state = state;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeEnum(state);
        serializer.serializeFloat(x);
        serializer.serializeFloat(y);
        serializer.serializeString(clientId);
    }

    @Override
    public BluePlayerStatePacket deserialize(Deserializer deserializer) throws SerializationException {
        return new BluePlayerStatePacket(deserializer.deserializeEnum(statesEnumArray),
                deserializer.deserializeFloat(), deserializer.deserializeFloat(), deserializer.deserializeString());
    }
}
