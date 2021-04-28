
package com.max.myfirstmpdemo;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.CountDownPacket;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RoomPacket;

public class PacketsSerializer {

    public PacketsSerializer() {
    }

    public static void register(ManualSerializer serializer){
        serializer.register(new RoomPacket());
        serializer.register(new CountDownPacket());
        serializer.register(new RedShirtInitPacket());
        serializer.register(new BlueShirtInitPacket());
        serializer.register(new RedPlayerStatePacket());
    }
}
