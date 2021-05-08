package com.max.myfirstmpdemo.headless.Entities;

import com.badlogic.gdx.math.MathUtils;
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
            float angle = MathUtils.atan2((((BallEntity)other.userData).position.y ) - (((PlayerEntity)item.userData).position.y ),
                    (((BallEntity)other.userData).position.x) - (((PlayerEntity)item.userData).position.x)) * MathUtils.radiansToDegrees;

            angle = (((angle % 360) + 360) % 360);
            ((BallEntity)other.userData).world.move(other, (((BallEntity)other.userData).position.x) + (MathUtils.cosDeg(angle) * 2),
                    (((BallEntity)other.userData).position.y) + (MathUtils.sinDeg(angle) * 2), ((BallEntity) other.userData).collisionFilter);
            //((BallEntity)other.userData).position.x = ((BallEntity)other.userData).world.getRect(other).x;
            //((BallEntity)other.userData).position.y = ((BallEntity)other.userData).world.getRect(other).y;
            return Response.cross;
        }
    };

    public Vector2 startPos = new Vector2();

    public ServerWebSocket playerSocket;
}
