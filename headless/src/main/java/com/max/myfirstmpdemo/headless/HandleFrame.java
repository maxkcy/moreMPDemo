package com.max.myfirstmpdemo.headless;

import com.max.myfirstmpdemo.Packets.RoomEnum;
import com.max.myfirstmpdemo.Packets.RoomPacket;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

public class HandleFrame {
ServerMain serverMain;
    public HandleFrame(ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    public void handleFrame(ServerWebSocket webSocket, final WebSocketFrame frame){
        final Object request = serverMain.manualSerializer.deserialize(frame.binaryData().getBytes());
        System.out.println("Received packet: " + request + " from: RA " + webSocket.remoteAddress());

        if (request instanceof RoomPacket){
            System.out.println(((RoomPacket) request).roomEnum);
            if(((RoomPacket) request).roomEnum == RoomEnum.QUE){
                serverMain.waitingForGameQueue.addLast(webSocket);
                System.out.println("client " + webSocket + " added to que");

                final RoomPacket response = new RoomPacket(RoomEnum.QUE);
                response.roomEnum = RoomEnum.QUE;
                webSocket.writeFinalBinaryFrame(Buffer.buffer(serverMain.manualSerializer.serialize(response)));
                System.out.println("sent response confirming client added to que");}
        }


    }
}
