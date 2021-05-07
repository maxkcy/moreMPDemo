package com.max.myfirstmpdemo.headless.Entities;

import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

import io.vertx.core.http.ServerWebSocket;

public class PlayerEntity extends Entity{
    public PlayerEntity(float x, float y) {
        super(x, y, 24, 24);
    }

    public enum States{
        Running,
        Kicking,
        Idle;
    }
    public States state;
    public boolean idleStateSent = false;
    public boolean runningStateSent = false;
    public boolean kickingStateSent = false;

    public CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof BallEntity);

            ((BallEntity)other.userData).world.move()
            return Response.cross;
        }
    };

    public Vector2 startPos = new Vector2();

    public ServerWebSocket playerSocket;
}
