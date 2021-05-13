package com.max.myfirstmpdemo.Packets;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

public class ScorePacket implements Transferable<ScorePacket> {

    public ScorePacket(int score, Team team) {
        this.score = score;
        this.team = team;
    }

    public ScorePacket() {
    }

    int score;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

  public  enum Team{
        Red,
        Blue;

        Team() {
        }
    }

    Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeInt(score);
        serializer.serializeEnum(team);
    }

    @Override
    public ScorePacket deserialize(Deserializer deserializer) throws SerializationException {
        return new ScorePacket(deserializer.deserializeInt(), deserializer.deserializeEnum(Team.values()));
    }
}
