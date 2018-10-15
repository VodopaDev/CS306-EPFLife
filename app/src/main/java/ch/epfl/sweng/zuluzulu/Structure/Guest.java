package ch.epfl.sweng.zuluzulu.Structure;

public final class Guest extends User {
    private static final String GUEST_USERNAME = "Guest";

    protected Guest() {
    }

    @Override
    public String getGaspar() {
        return GUEST_USERNAME;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String toString() {
        return "Guest user";
    }
}
