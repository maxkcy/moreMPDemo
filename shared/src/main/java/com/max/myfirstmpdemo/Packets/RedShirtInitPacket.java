package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;


public class RedShirtInitPacket implements Transferable<RedShirtInitPacket> {

    public String IDKey;

    public RedShirtInitPacket() {
    }

    public RedShirtInitPacket(String IDKey) {
        this.IDKey = IDKey;
    }


    public void setIDKey(String IDKey) {
        this.IDKey = IDKey;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeString(IDKey);
    }

    @Override
    public RedShirtInitPacket deserialize(Deserializer deserializer) throws SerializationException {
        return new RedShirtInitPacket(deserializer.deserializeString());
    }
}
