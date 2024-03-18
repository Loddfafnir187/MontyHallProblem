package com.montyhall;

public class Door {
    // 0 - дверь закрыта, 1 - открыта и пустая, 2 - открыта и победная
    private int doorState = 0;
    private boolean winDoor = false;

    public Door(int doorState, boolean winDoor) {
        this.doorState = doorState;
        this.winDoor = winDoor;
    }
    public boolean isWinDoor() {
        return winDoor;
    }
    public void setWinDoor(boolean winDoor) {
        this.winDoor = winDoor;
    }
    public int getDoorState() {
        return doorState;
    }
    public void setDoorState(int doorState) {
        this.doorState = doorState;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + doorState;
        result = prime * result + (winDoor ? 1231 : 1237);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Door other = (Door) obj;
        if (doorState != other.doorState)
            return false;
        if (winDoor != other.winDoor)
            return false;
        return true;
    }
    
}
