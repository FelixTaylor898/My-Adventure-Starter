package edu.vt.javadev.adventure.model;

public interface Lockable {
    void lock();
    void unlock();
    boolean isLocked();
}

