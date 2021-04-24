package com.max.myfirstmpdemo.headless.Entities;

import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

import io.vertx.core.http.ServerWebSocket;

public class PlayerEntity extends Entity{
    public PlayerEntity(float x, float y) {
        super(x, y, 40, 100);
    }

    public enum States{
        Running,
        Kicking,
        Idle;
    }
    public States state;

    CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof BallEntity);
            return Response.cross;
        }
    };

    public Vector2 startPos;

    public ServerWebSocket playerSocket;
}
