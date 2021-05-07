package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class AsteroidStatePacket implements Transferable<AsteroidStatePacket> {

    float x;
    float y;

    public AsteroidStatePacket(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public AsteroidStatePacket() {
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeFloat(x);
        serializer.serializeFloat(y);
    }

    @Override
    public AsteroidStatePacket deserialize(Deserializer deserializer) throws SerializationException {
        return new AsteroidStatePacket(deserializer.deserializeFloat(), deserializer.deserializeFloat());
    }


}
