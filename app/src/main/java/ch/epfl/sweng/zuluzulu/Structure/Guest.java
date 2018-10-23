package ch.epfl.sweng.zuluzulu.Structure;

public final class Guest extends User {
    protected Guest() {
        super();
    }

    @Override
    public boolean hasRole(UserRole role){
        // Should never have role
        return false;
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
