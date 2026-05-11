package tests;

import assignments.dungeon.*;
import assignments.dungeon.Character;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;

public class DungeonTest {

    private Character hero;
    private Shop shop;
    private Item expensiveItem;

    @BeforeEach
    void setUp() {
        hero = new Character("Hero", 50, 10);
        shop = new Shop();
        expensiveItem = new Item("Legendary Sword", 100);
    }

    @Test
    @Tag("core")
    @DisplayName("Core: InsufficientGoldException шидэгдэж байгааг шалгах")
    void testInsufficientGoldException() {
        assertThrows(InsufficientGoldException.class, () -> {
            shop.buy(hero, expensiveItem);
        }, "Алт хүрэлцэхгүй байхад InsufficientGoldException шидэх ёстой.");
    }

    @Test
    @Tag("core")
    @DisplayName("Core: NotEnoughManaException (Unchecked) шидэгдэж байгааг шалгах")
    void testNotEnoughManaException() {
        assertThrows(NotEnoughManaException.class, () -> {
            hero.useMana(20);
        }, "Мана хүрэлцэхгүй байхад NotEnoughManaException шидэх ёстой.");
    }

    @Test
    @Tag("stretch")
    @DisplayName("Stretch: DungeonLog файл руу зөв бичиж байгааг шалгах")
    void testDungeonLog() throws Exception {
        DungeonLog logger = new DungeonLog();
        String testMessage = "Test Log Entry";
        logger.log(testMessage);

        File logFile = new File("dungeon.log");
        assertTrue(logFile.exists(), "dungeon.log файл үүссэн байх ёстой.");

        String content = Files.readString(logFile.toPath());
        assertTrue(content.contains(testMessage), "Лог файл дотор мессеж хадгалагдсан байх ёстой.");
    }

    @Test
    @Tag("stretch")
    @DisplayName("Stretch: Shop autoSave-ийн finally блок ажиллаж байгааг шалгах")
    void testShopFinallyBlock() {
        // Энэ тест нь ихэвчлэн консол руу хэвлэж буйг эсвэл
        // алдаа гарсан ч ажиллаж буйг шалгадаг.
        assertDoesNotThrow(() -> shop.autoSave());
    }
}
