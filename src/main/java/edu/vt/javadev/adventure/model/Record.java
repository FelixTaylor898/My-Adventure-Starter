package edu.vt.javadev.adventure.model;

import edu.vt.javadev.adventure.Message;
import org.junit.jupiter.api.Test;

public class Record extends Item implements Placeable, Playable {

    // ==========================================================
    // Fields
    // ==========================================================

    private String songName;
    private RecordPlayer player;

    // ==========================================================
    // Constructors
    // ==========================================================

    public Record(String name, String songName, RecordPlayer player) {
        super(name);
        setSongName(songName);
        setRecordPlayer(player);
    }

    // ==========================================================
    // Getters & setters
    // ==========================================================

    @Override
    public boolean isPlaying() {
        return isPlaced() && player.isPlaying();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        if (songName == null || songName.isEmpty()) {
            throw new IllegalArgumentException("Invalid song name.");
        }
        this.songName = World.capitalize(songName);
    }

    public boolean isPlaced() {
        return getParent() != null && getParent().equals(player);
    }

    public void setRecordPlayer(RecordPlayer player) {
        if (player == null) throw new IllegalArgumentException("RecordPlayer cannot be null");
        this.player = player;
    }

    // ==========================================================
    // Actions
    // ==========================================================

    public void place() {
        if (!(getParent() instanceof Person)) {
            World.currentMessage = Message.notInInventory(this);
            return;
        }
        if (!player.getParent().equals(World.getCurrentRoom())) {
            World.currentMessage = Message.placeTooFar(this);
        } else {
            World.player.removeChild(this);
            player.addChild(this);
            World.currentMessage = Message.placeSuccess(this, player);
        }
    }

    @Override
    public void take() {
        if (isPlaced()) World.currentMessage = Message.takeCant(this);
        else super.take();
    }

    @Override
    public void play() {
        if (!isPlaced()) {
            World.currentMessage = Message.playMustPlace(this);
        } else {
            player.play();
        }
    }

    @Override
    public void examine() {
        String str = getDescription();
        if (isPlaced()) {
            if (!str.isEmpty()) str += " ";
            str += player.isPlaying() ? songName + " is playing on " + player.theName() + "." : capTheName() + " is on " + player.theName() + ".";
        }
        World.currentMessage = str.isEmpty() ? Message.examineNothingSpecial(this) : str;
    }
}
