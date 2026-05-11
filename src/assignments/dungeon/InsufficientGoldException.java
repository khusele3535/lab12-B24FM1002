package assignments.dungeon;

public class InsufficientGoldException extends Exception {
    public InsufficientGoldException(String message) {
        super(message);
    }
}
