package assignments.dungeon;

import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private int gold;
    private int mana;
    private List<Item> inventory;

    public Character(String name, int gold, int mana) {
        this.name = name;
        this.gold = gold;
        this.mana = mana;
        this.inventory = new ArrayList<>();
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getMana() {
        return mana;
    }

    public void useMana(int amount) {
        if (this.mana < amount) {
            throw new NotEnoughManaException("Мана хүрэлцэхгүй байна!");
        }
        this.mana -= amount;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }
}
