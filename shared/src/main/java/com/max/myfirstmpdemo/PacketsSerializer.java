package com.max.myfirstmpdemo;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;

public class PacketsSerializer {

    public PacketsSerializer() {
    }

    public static void register(ManualSerializer serializer){
        serializer.register(new RoomPacket(RoomEnum.QUE));
    }
}
