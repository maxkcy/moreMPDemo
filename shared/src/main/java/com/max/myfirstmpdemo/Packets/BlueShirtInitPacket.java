package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class BlueShirtInitPacket implements Transferable<BlueShirtInitPacket> {
    public String IDKey;

    public BlueShirtInitPacket() {
    }

    public BlueShirtInitPacket(String IDKey) {
        this.IDKey = IDKey;
    }

    public void setIDKey(String IDKey) {
        this.IDKey = IDKey;
    }


    @Override
    public void serialize(Serializer serializer) throws SerializationException {

    }

    @Override
    public BlueShirtInitPacket deserialize(Deserializer deserializer) throws SerializationException {
        return null;
    }
}
