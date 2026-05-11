# Lab 12 — Dungeon Adventure (Exception Handling)

**Нийт оноо:** 100 | **Сэдэв:** try/catch/finally, custom exceptions, checked vs unchecked

## 🎭 Түүх

Dungeon of OOP-д олон алдаа тохиолдож болно: баатар **алт хүрэлцэхгүй** бол зэвсэг авч чадахгүй, **мана дутвал** ид шид хэрэглэж чадахгүй, **авдар хоосон** бол юу ч олохгүй. Гэхдээ хөтөлбөр крашлаж болохгүй. **Exception**-г барьж, хэрэглэгчид ойлгомжтой мессежээр хариулах нь чиний үүрэг.

---

## 📋 Урьдчилан бичигдсэн файл

### `Character.java` (бүү өөрчил)

```java
public class Character {
    private String name;
    private int hp;
    private int mp;
    private int gold;

    public Character(String name, int hp, int mp, int gold) { ... }

    public String getName() { ... }
    public int getHp() { ... }
    public int getMp() { ... }
    public int getGold() { ... }

    public void setGold(int gold) { ... }
    public void setMp(int mp) { ... }
}
```

Энэ классыг чи **өөрчилж болохгүй**. Тестийн үед ашиглагдана.

---

## 🟢 Core (60 оноо) — 9 тест

### 1. `InsufficientGoldException.java`

**Checked exception.** `Exception`-оос удамшина.

```java
public class InsufficientGoldException extends Exception {
    public InsufficientGoldException(String message) {
        super(message);
    }
}
```

- `Exception` класс нь **checked** (compile-time шалгагдана).
- `throw new InsufficientGoldException("алт хүрэхгүй")` хийсэн method-д `throws InsufficientGoldException` зарлаж байх ёстой.

### 2. `NotEnoughManaException.java`

**Unchecked exception.** `RuntimeException`-оос удамшина.

```java
public class NotEnoughManaException extends RuntimeException {
    public NotEnoughManaException(String message) {
        super(message);
    }
}
```

- `RuntimeException` нь **unchecked**, method signature-т `throws` зарлахгүй.

### 3. `Item.java`

```java
public class Item {
    private String name;
    private int price;

    public Item(String name, int price) { ... }

    public String getName() { ... }
    public int getPrice() { ... }
}
```

- 2 талбар `private`.
- Constructor 2 параметртэй.
- getter method-ууд.

### 4. `Shop.java` — `buy` method

```java
public class Shop {
    public void buy(Character buyer, Item item) throws InsufficientGoldException {
        if (buyer.getGold() < item.getPrice()) {
            throw new InsufficientGoldException(
                buyer.getName() + "-ийн алт хүрэлцэхгүй байна"
            );
        }
        buyer.setGold(buyer.getGold() - item.getPrice());
    }
}
```

- Хэрэв `buyer.getGold() < item.getPrice()` бол `InsufficientGoldException` шиднэ.
- Эсрэг тохиолдолд алт хасагдана.
- Method signature-т `throws InsufficientGoldException` заавал бичнэ.

**Жишээ:**
```java
Character hero = new Character("Aragorn", 100, 50, 20);
Item sword = new Item("Sword", 50);
Shop shop = new Shop();

try {
    shop.buy(hero, sword); // InsufficientGoldException шиднэ (20 < 50)
} catch (InsufficientGoldException e) {
    System.out.println(e.getMessage());
}
```

---

## 🟡 Stretch (30 оноо) — 3 тест

### 5. `DungeonLog.java` — try-with-resources

```java
public class DungeonLog implements AutoCloseable {
    private boolean closed = false;
    private StringBuilder log = new StringBuilder();

    public void write(String entry) {
        log.append(entry).append("\n");
    }

    public boolean isClosed() { return closed; }

    @Override
    public void close() {
        closed = true;
    }
}
```

- `AutoCloseable` interface-г implement хийнэ.
- `close()` дуудагдсан бол `isClosed()` **true** буцаана.
- try-with-resources:
  ```java
  try (DungeonLog log = new DungeonLog()) {
      log.write("Entered dungeon");
  } // автомат close()
  ```

### 6. `Shop.autoSave()` — finally block

```java
public class Shop {
    public static int autoSaveCount = 0;

    public void autoSave(boolean shouldFail) {
        try {
            if (shouldFail) {
                throw new RuntimeException("Save failed");
            }
        } finally {
            autoSaveCount++;
        }
    }
}
```

- Exception шидэгдсэн ч шидэгдээгүй ч `autoSaveCount` **ямар ч тохиолдолд нэмэгдэнэ**.

---

## 🔴 Bonus (10 оноо) — 2 тест

### 7. Exception chaining — `Shop.buyWithChain()`

```java
public void buyWithChain(Character buyer, Item item) throws Exception {
    try {
        buy(buyer, item);
    } catch (InsufficientGoldException cause) {
        throw new Exception("Transaction failed", cause);
    }
}
```

- `throw new Exception(msg, cause)` — cause параметрээр оригинал exception-г дамжуулна.
- Тест: `caughtException.getCause()` нь анхны `InsufficientGoldException`-ийг буцаана.

### 8. Reflection шалгалт

- `InsufficientGoldException.class.getSuperclass() == Exception.class` (шууд Exception-ээс удамшсан).
- `RuntimeException.class.isAssignableFrom(InsufficientGoldException.class) == false` (RuntimeException биш, тийм учраас checked).

---

## 🧪 Тест ажиллуулах

```bash
bash scripts/run_tests.sh
bash scripts/run_tests.sh --tag core
bash scripts/run_tests.sh --tag stretch
bash scripts/run_tests.sh --tag bonus
```

---

## ✅ Шалгуурын жагсаалт (Checklist)

### Core
- [ ] `InsufficientGoldException extends Exception` (checked)
- [ ] `NotEnoughManaException extends RuntimeException` (unchecked)
- [ ] Constructor `(String)` super-ийг дуудна
- [ ] `Item` класс private талбар, getter
- [ ] `Shop.buy(...)` signature `throws InsufficientGoldException`
- [ ] `buy` insufficient gold үед exception шиднэ
- [ ] `buy` хангалттай гол үед алт хасна

### Stretch
- [ ] `DungeonLog implements AutoCloseable`
- [ ] try-with-resources автомат `close()` дуудна
- [ ] `autoSave` finally block-д counter нэмнэ

### Bonus
- [ ] `buyWithChain` — cause дамжуулсан
- [ ] Reflection — InsufficientGoldException нь RuntimeException биш

---

## 🚫 Түгээмэл алдаанууд

1. **`InsufficientGoldException extends RuntimeException`** — энэ бол буруу, checked байх ёстой
2. **`throws` мартах** — `buy` method signature-т `throws InsufficientGoldException` заавал бичнэ
3. **`super(message)` дуудахгүй** — message үгүй бол `getMessage()` null буцаана
4. **try-with-resources ашиглахгүй** — manual `log.close()` биш, try блокын resource-ээр декларлана
5. **finally-г catch-д оруулах** — finally нь try-тай нэг түвшинд байх ёстой
