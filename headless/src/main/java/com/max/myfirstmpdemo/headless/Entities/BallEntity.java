package com.max.myfirstmpdemo.headless.Entities;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;

public class BallEntity extends Entity{
    public World world;
    public BallEntity(World world) {
        super((600/2) - (32/2),(400/2) - ( 32/2)  , 32, 32);
        this.world = world;
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

    public void startPosition(){
        super.position.x = (600/2) - (32/2);
        super.position.y = (400/2) - (32/2);
    }

   public CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof Entity);
            return Response.touch;
        }
    };

    public int flip = -1;
}


