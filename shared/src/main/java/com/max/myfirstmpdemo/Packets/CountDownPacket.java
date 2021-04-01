package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class CountDownPacket implements Transferable<CountDownPacket> {

    private float time; //must be inti in the constructor... not sure why

    public CountDownPacket(float time) {
        this.time = time;
        //dont do this.time = somenumber.0f in my case 300.0f testing here because incoming packets always 300.0f ... yea fixed it. dont set this.time = 300; in constructor it will always be 300 no matter what
    }
    public CountDownPacket() {
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeFloat(time);
    }

    @Override
    public CountDownPacket deserialize(Deserializer deserializer) throws SerializationException {
        return new CountDownPacket(deserializer.deserializeFloat());
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
