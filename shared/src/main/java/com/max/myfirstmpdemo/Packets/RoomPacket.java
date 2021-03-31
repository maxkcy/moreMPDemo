package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class  RoomPacket implements Transferable<RoomPacket> {
public RoomEnum roomEnum;
public static final RoomEnum[] roomEnumArrayValues = RoomEnum.values();



    public RoomPacket(RoomEnum roomEnum) {
    this.roomEnum = roomEnum;
    }


    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeEnum(roomEnum);
    }

    @Override
    public RoomPacket deserialize(Deserializer deserializer) throws SerializationException {
        return new RoomPacket(deserializer.deserializeEnum(roomEnumArrayValues));
    }
}
