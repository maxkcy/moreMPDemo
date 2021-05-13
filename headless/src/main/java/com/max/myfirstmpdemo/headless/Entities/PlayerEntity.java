package com.max.myfirstmpdemo.headless.Entities;

import com.badlogic.gdx.Gdx;
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

    public float angle;
    public BallEntity ballEntity;
    public Item worldBallItem;
    public boolean touchedBall;
    public float kickingTimer;

    public CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof BallEntity) {
                //angle = MathUtils.atan2((((BallEntity) other.userData).position.y) - (((PlayerEntity) item.userData).position.y),
                  //      (((BallEntity) other.userData).position.x) - (((PlayerEntity) item.userData).position.x)) * MathUtils.radiansToDegrees;

                //angle = (((angle % 360) + 360) % 360);
                ballEntity = ((BallEntity)other.userData);
                worldBallItem = other;

                //((BallEntity) other.userData).world.move(other, (((BallEntity) other.userData).position.x) + (MathUtils.cosDeg(angle) * 2),
                        //(((BallEntity) other.userData).position.y) + (MathUtils.sinDeg(angle) * 2), ((BallEntity) other.userData).collisionFilter);
                touchedBall = true;
                Gdx.app.log(this.toString(), "touched ball is TRUE");
                //((BallEntity)other.userData).position.x = ((BallEntity)other.userData).world.getRect(other).x;
                //((BallEntity)other.userData).position.y = ((BallEntity)other.userData).world.getRect(other).y;
                return Response.touch;

            } else{touchedBall = false;
                Gdx.app.log(this.toString(), "touched something other than ball");}


            return Response.touch;
        }
    };

    public Vector2 startPos = new Vector2();

    public ServerWebSocket playerSocket;
}
