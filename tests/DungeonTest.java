import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@DisplayName("Lab 12: Dungeon Adventure (Exception Handling)")
public class DungeonTest {

    private Character hero;
    private Item sword;
    private Shop shop;

    @BeforeEach
    void setUp() {
        hero = new Character("Aragorn", 100, 50, 20);
        sword = new Item("Sword", 50);
        shop = new Shop();
    }

    // ==================== 🟢 CORE ====================

    @Test
    @Tag("core")
    @DisplayName("InsufficientGoldException нь Exception-оос удамшина (checked)")
    void insufficientGoldIsChecked() {
        assertEquals(Exception.class,
            InsufficientGoldException.class.getSuperclass(),
            "InsufficientGoldException нь шууд Exception-оос удамшина (RuntimeException биш)");
    }

    @Test
    @Tag("core")
    @DisplayName("InsufficientGoldException(String) constructor ажилна")
    void insufficientGoldConstructor() throws Exception {
        InsufficientGoldException e = new InsufficientGoldException("no gold");
        assertEquals("no gold", e.getMessage(),
            "super(message) дуудсан байх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("NotEnoughManaException нь RuntimeException-оос удамшина (unchecked)")
    void notEnoughManaIsUnchecked() {
        assertTrue(
            RuntimeException.class.isAssignableFrom(NotEnoughManaException.class),
            "NotEnoughManaException нь RuntimeException-оос удамшина");
    }

    @Test
    @Tag("core")
    @DisplayName("NotEnoughManaException(String) constructor ажилна")
    void notEnoughManaConstructor() {
        NotEnoughManaException e = new NotEnoughManaException("no mana");
        assertEquals("no mana", e.getMessage());
    }

    @Test
    @Tag("core")
    @DisplayName("Item класс private талбар, getter-ууд")
    void itemFieldsAndGetters() throws Exception {
        Field[] fields = Item.class.getDeclaredFields();
        assertTrue(fields.length >= 2, "Дор хаяж 2 талбар зарлагдсан байх ёстой");
        for (Field f : fields) {
            assertTrue(Modifier.isPrivate(f.getModifiers()),
                f.getName() + " талбар private биш байна!");
        }
        assertEquals("Sword", sword.getName());
        assertEquals(50, sword.getPrice());
    }

    @Test
    @Tag("core")
    @DisplayName("Shop.buy signature-д throws InsufficientGoldException байна")
    void buyDeclaresThrows() throws Exception {
        Method m = Shop.class.getMethod("buy", Character.class, Item.class);
        Class<?>[] exceptions = m.getExceptionTypes();
        boolean hasIt = false;
        for (Class<?> ex : exceptions) {
            if (ex.equals(InsufficientGoldException.class)) {
                hasIt = true;
                break;
            }
        }
        assertTrue(hasIt, "buy method нь 'throws InsufficientGoldException' зарласан байх ёстой");
    }

    @Test
    @Tag("core")
    @DisplayName("buy алт хүрэлцэхгүй үед InsufficientGoldException шиднэ")
    void buyThrowsWhenGoldInsufficient() {
        // hero has 20 gold, sword costs 50
        assertThrows(InsufficientGoldException.class, () -> shop.buy(hero, sword));
    }

    @Test
    @Tag("core")
    @DisplayName("buy хангалттай гол үед алт хасна, exception шидэхгүй")
    void buyDeductsGoldWhenSufficient() throws Exception {
        Character rich = new Character("Gimli", 100, 50, 200);
        shop.buy(rich, sword); // should not throw
        assertEquals(150, rich.getGold(), "алт item үнээр хасагдана");
    }

    @Test
    @Tag("core")
    @DisplayName("buy үлдсэн алт item үнэтэй тэнцсэн үед ажилна")
    void buyWorksAtExactAmount() throws Exception {
        Character exact = new Character("Legolas", 100, 50, 50);
        shop.buy(exact, sword);
        assertEquals(0, exact.getGold());
    }

    // ==================== 🟡 STRETCH ====================

    @Test
    @Tag("stretch")
    @DisplayName("DungeonLog нь AutoCloseable-г implement хийнэ")
    void dungeonLogIsAutoCloseable() {
        assertTrue(AutoCloseable.class.isAssignableFrom(DungeonLog.class),
            "DungeonLog нь AutoCloseable-г implement хийсэн байх ёстой");
    }

    @Test
    @Tag("stretch")
    @DisplayName("try-with-resources автомат close() дуудна")
    void tryWithResourcesClosesLog() throws Exception {
        DungeonLog ref;
        try (DungeonLog log = new DungeonLog()) {
            log.write("Entered dungeon");
            ref = log;
            assertFalse(ref.isClosed(), "try блок дотор хараахан хаагдаагүй");
        }
        assertTrue(ref.isClosed(), "try-with-resources close() дуудсан байх ёстой");
    }

    @Test
    @Tag("stretch")
    @DisplayName("autoSave finally block-д counter нэмэгдэнэ (exception-тэй ч, үгүй ч)")
    void autoSaveFinallyRuns() {
        int start = Shop.autoSaveCount;
        shop.autoSave(false);
        assertEquals(start + 1, Shop.autoSaveCount, "амжилттай үед нэмэгдэнэ");

        try {
            shop.autoSave(true);
            fail("shouldFail=true үед exception шидэгдэх ёстой");
        } catch (RuntimeException expected) {
            // expected
        }
        assertEquals(start + 2, Shop.autoSaveCount,
            "exception шидэгдсэн ч finally-ээс counter нэмэгдэнэ");
    }

    // ==================== 🔴 BONUS ====================

    @Test
    @Tag("bonus")
    @DisplayName("buyWithChain нь cause-тай шинэ Exception шиднэ")
    void buyWithChainPropagatesCause() {
        try {
            shop.buyWithChain(hero, sword);
            fail("Exception шидэгдэх ёстой");
        } catch (Exception e) {
            assertNotNull(e.getCause(), "cause null байж болохгүй");
            assertTrue(e.getCause() instanceof InsufficientGoldException,
                "cause нь анхны InsufficientGoldException байх ёстой");
        }
    }

    @Test
    @Tag("bonus")
    @DisplayName("InsufficientGoldException нь RuntimeException биш (checked баталгаажуулалт)")
    void insufficientGoldIsNotRuntime() {
        assertFalse(
            RuntimeException.class.isAssignableFrom(InsufficientGoldException.class),
            "InsufficientGoldException нь checked — RuntimeException-оос удамшихгүй");
    }
}
