public class Character {

    private String name;
    private int hp;
    private int mp;
    private int gold;

    public Character(String name, int hp, int mp, int gold) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
        this.gold = gold;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMp() {
        return mp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
