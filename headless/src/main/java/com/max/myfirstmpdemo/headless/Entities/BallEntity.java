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

    public class Boundary {
        public int boundaryXLeft = 0;
        public int boundaryXRight = 600;
        public int boundaryYBottom = 0;
        public int boundaryYtop = 400;
    }

    public Boundary boundary = new Boundary();

    public enum States{
        STATIC,
        MOVING;
    }
    public States state;

    public void resetPosition(){
        super.position.x = (600/2) - (32/2);
        super.position.y = (400/2) - (32/2);
    }

   public CollisionFilter collisionFilter = new CollisionFilter() {
        @Override
        public Response filter(Item item, Item other) {
            if(other.userData instanceof Entity){
                return Response.bounce;
            }
            return Response.bounce;
        }
    };

    public int flip = -1;
}


