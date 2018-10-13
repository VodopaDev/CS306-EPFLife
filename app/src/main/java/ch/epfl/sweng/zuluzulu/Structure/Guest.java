package ch.epfl.sweng.zuluzulu.Structure;

public final class Guest extends User {
    private static final String GUEST_USERNAME = "Guest";

    @Override
    public String getGaspar() {
        return GUEST_USERNAME;
    }

    protected Guest() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }
}
