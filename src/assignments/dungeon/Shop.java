package assignments.dungeon;

public class Shop {

    public void buy(Character player, Item item) throws InsufficientGoldException {
        if (player.getGold() < item.getPrice()) {
            throw new InsufficientGoldException("Not enough gold to buy " + item.getName());
        }
        player.setGold(player.getGold() - item.getPrice());
        player.getInventory().add(item);
    }

    public void autoSave() {
        try {
            System.out.println("Processing transaction...");
        } finally {
            // Алдаа гарсан ч, гараагүй ч заавал ажиллана
            System.out.println("Shop data auto-saved.");
        }
    }
}
