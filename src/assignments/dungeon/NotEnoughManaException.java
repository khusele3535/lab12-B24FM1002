package assignments.dungeon;

public class NotEnoughManaException extends RuntimeException {
    public NotEnoughManaException(String message) {
        super(message);
    }
}
