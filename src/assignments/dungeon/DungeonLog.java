package assignments.dungeon;

import java.io.*;

public class DungeonLog {
    public void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("dungeon.log", true))) {
            out.println(message);
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }
}
