package com.max.myfirstmpdemo.headless.Entities;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;

public class BallEntity extends Entity{
    public BallEntity() {
        super((600/2) - (32/2),(400/2) - ( 32/2)  , 32, 32);
    }

    public class Boundry {
        public int boundryXLeft = 0;
        public int boundryXRight = 600;
        public int boundryYBottom = 0;
        public int boundryYtop = 400;
    }

    public Boundry boundry = new Boundry();

    public enum States{
        STATIC,
        MOVING;
    }
    public States state;

    public void reset(){
        super.position.x = (600/2) - (32/2);
        super.position.y = (400/2) - (32/2);
    }

    CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof Entity);
            return Response.bounce;
        }
    };

    public int flip = -1;
}


