package com.max.myfirstmpdemo.headless;

import com.dongbat.jbump.Item;
import com.max.myfirstmpdemo.headless.Entities.Entity;
import com.max.myfirstmpdemo.headless.GameRoom;

import io.vertx.core.http.ServerWebSocket;

public class ClientID {

    private GameRoom clientGameRoom;
    private Item<Entity> clientPlayerItem;
    private ServerWebSocket client;


    public ClientID(ServerWebSocket client) {
        this.client = client;
        playerID = client.toString();
    }

   public enum Team{
        RED,
        BLUE;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public Team team;

    public String playerID;

    public GameRoom getClientGameRoom() {
        return clientGameRoom;
    }

    public void setClientGameRoom(GameRoom clientGameRoom) {
        this.clientGameRoom = clientGameRoom;
    }

    public Item<Entity> getClientPlayerItem() {
        return clientPlayerItem;
    }

    public void setClientPlayerItem(Item<Entity> clientPlayerItem) {
        this.clientPlayerItem = clientPlayerItem;
    }

}
