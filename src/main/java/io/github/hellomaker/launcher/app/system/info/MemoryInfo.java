package io.github.hellomaker.launcher.app.system.info;

public class MemoryInfo {
    private long capacity;
    private int speed;

    // Getters and Setters for the fields

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "MemoryInfo{" +
                "capacity=" + capacity +
                ", speed=" + speed +
                '}';
    }
// Override toString for better output representation
}