public class Shop {

    // ─────── 🟡 Stretch: finally block counter ───────
    public static int autoSaveCount = 0;

    // TODO: buy(Character buyer, Item item) → void
    // - throws InsufficientGoldException (заавал method signature-д бичнэ)
    // - buyer.getGold() < item.getPrice() бол InsufficientGoldException шид
    // - эс бөгөөс buyer.setGold(buyer.getGold() - item.getPrice())

    // ─────── 🟡 Stretch (30 оноо) ───────

    // TODO: autoSave(boolean shouldFail) → void
    // - try блок дотор: хэрэв shouldFail бол throw new RuntimeException("Save failed")
    // - finally блок дотор: autoSaveCount++
    // - Exception шидэгдсэн ч, үгүй ч counter заавал нэмэгдэнэ

    // ─────── 🔴 Bonus (10 оноо) ───────

    // TODO: buyWithChain(Character buyer, Item item) → void
    // - throws Exception
    // - try блокоос buy(buyer, item) дуудна
    // - catch (InsufficientGoldException cause) бол:
    //   throw new Exception("Transaction failed", cause) — cause дамжуулсан шинэ exception
}
