
package com.max.myfirstmpdemo;

import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.max.myfirstmpdemo.Packets.AsteroidStatePacket;
import com.max.myfirstmpdemo.Packets.BluePlayerStatePacket;
import com.max.myfirstmpdemo.Packets.BlueShirtInitPacket;
import com.max.myfirstmpdemo.Packets.CountDownPacket;
import com.max.myfirstmpdemo.Packets.TouchUpPacket;
import com.max.myfirstmpdemo.Packets.RedPlayerStatePacket;
import com.max.myfirstmpdemo.Packets.RedShirtInitPacket;
import com.max.myfirstmpdemo.Packets.RoomPacket;
import com.max.myfirstmpdemo.Packets.ScorePacket;
import com.max.myfirstmpdemo.Packets.TouchDownPacket;

public class PacketsSerializer {

    public PacketsSerializer() {
    }

    public static void register(ManualSerializer serializer){
        serializer.register(new RoomPacket());
        serializer.register(new CountDownPacket());
        serializer.register(new RedShirtInitPacket());
        serializer.register(new BlueShirtInitPacket());
        serializer.register(new RedPlayerStatePacket());
        serializer.register(new BluePlayerStatePacket());
        serializer.register(new TouchDownPacket());
        serializer.register(new AsteroidStatePacket());
        serializer.register(new ScorePacket());
        serializer.register(new TouchUpPacket());
    }
}
