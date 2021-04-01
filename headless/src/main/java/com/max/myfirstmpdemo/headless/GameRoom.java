package com.max.myfirstmpdemo.headless;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.max.myfirstmpdemo.Packets.CountDownPacket;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;

public class GameRoom extends ScreenAdapter {
    public ServerMain serverMain;
    public Array<ServerWebSocket> playersList;
    public float time;// = 300.0f;
    public boolean isActive;// = true;

    //public CountDownPacket countDownPacket = new CountDownPacket(300.0f);
    //for testies and this is demo im going to create a pool to keep pooling fresh in my memory
    Pool<CountDownPacket> countDownPacketPool;// = new Pool<CountDownPacket>() {


    public GameRoom(ServerMain serverMain) {
    this.serverMain = serverMain;
    System.out.println("New GameRoom initiated");
    }


    @Override
    public void show() {
      playersList = new Array<>();
      time = 300.0f;
      isActive = true;
      countDownPacketPool = new Pool<CountDownPacket>() {
          @Override
          protected CountDownPacket newObject() {
              return new CountDownPacket();
          }


          @Override
          protected void reset(CountDownPacket object) {
              super.reset(object);
              object.setTime(time);
          }
      };
      //System.out.println("The GameRoom has players: " + playersList);
    }

    @Override
    public void render(float delta) {
        //super.render(delta);
        if(isActive){
        for (ServerWebSocket serverWebSocket:
             playersList) {
            CountDownPacket countDownPacket = countDownPacketPool.obtain();
            //CountDownPacket countDownPacket = new CountDownPacket();
            countDownPacket.setTime(time);
            System.out.println(countDownPacket.getTime());
            serverWebSocket.writeFinalBinaryFrame(Buffer.buffer(serverMain.manualSerializer.serialize(countDownPacket)));

            countDownPacketPool.free(countDownPacket);
            }
        System.out.println("Countdown packets sent");
        //System.out.println(time);
        }

        if (time <= 0.0f){
            if(isActive == true) {
                System.out.println("Game Room has ended");
                isActive = false;
            }
        }

        time = time - delta;

    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
