package ch.epfl.sweng.zuluzulu.Structure;

public final class Guest extends User {
    protected Guest() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
