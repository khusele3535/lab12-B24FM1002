package assignments.dungeon;

public class Main {
    public static void main(String[] args) {
        Character hero = new Character("Warrior", 50, 10); // 50 алт, 10 манатай
        Shop shop = new Shop();
        DungeonLog logger = new DungeonLog();
        Item sword = new Item("Iron Sword", 100); // 100 алтны үнэтэй сэлэм

        System.out.println("--- Dungeon Adventure Start ---");

        try {
            System.out.println("Trying to buy " + sword.getName() + "...");
            shop.buy(hero, sword);
        } catch (InsufficientGoldException e) {
            System.out.println("Caught Expected Error: " + e.getMessage());
            logger.log("Purchase failed: " + e.getMessage());
        } finally {
            shop.autoSave(); // Алдаа гарсан ч заавал ажиллана
        }

        try {
            System.out.println("\nTrying to use 20 mana...");
            hero.useMana(20); // Баатарт ердөө 10 мана байгаа
        } catch (NotEnoughManaException e) {
            System.out.println("Caught Runtime Error: " + e.getMessage());
        }

        System.out.println("\nChecking dungeon.log content...");
        logger.log("Adventure concluded for today.");

        System.out.println("--- Adventure Logged Successfully ---");
    }
}
