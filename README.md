# 🐉 Lab 12 — Exception Handling: Dungeon Adventure

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![JUnit](https://img.shields.io/badge/JUnit-5-green?logo=junit5)
![Auto-Grader](https://img.shields.io/badge/Auto--Grader-Enabled-blue)
![AI Detection](https://img.shields.io/badge/AI%20Detection-Enabled-red)

> Dungeon of OOP-ын гүнд аюул заналхийлж байна. Алт хүрэлцэхгүй, мана дутна, авдар хоосон — энэ бүхэн хөтөлбөрийг **крашлуулж** болохгүй. Чи **exception handling** эзэмшиж, `try/catch/finally`, custom exception, checked vs unchecked ялгааг эзэмшин, баатрын аялалыг найдвартай болгоно.

## 📚 Суралцах материал

- **Теори:** [`UEFA-OPP-resources/docs/week-12-exceptions/`](https://github.com/UEFA-OPP/UEFA-OPP-resources/tree/main/docs/week-12-exceptions)
- **Git workflow заавар:** [`UEFA-OPP-resources/docs/git-workflow/`](https://github.com/UEFA-OPP/UEFA-OPP-resources/tree/main/docs/git-workflow)

## 🏗️ Хавтасны бүтэц

```
lab12-template/
├── README.md                                 # Энэ файл
├── .gitignore
├── assignments/
│   └── dungeon/
│       ├── Character.java                    # ← Урьдчилан бичигдсэн (бүү өөрчил)
│       ├── Item.java                         # ← Та энд код бичнэ
│       ├── Shop.java                         # ← Та энд код бичнэ
│       ├── InsufficientGoldException.java    # ← Та энд код бичнэ
│       ├── NotEnoughManaException.java       # ← Та энд код бичнэ
│       ├── DungeonLog.java                   # ← Stretch: try-with-resources
│       └── README.md                         # Даалгаврын дэлгэрэнгүй заавар
├── tests/
│   └── DungeonTest.java                      # JUnit 5 тестүүд (бүү өөрчил)
├── scripts/
│   ├── run_tests.sh                          # Тест ажиллуулах скрипт
│   └── ai_detector.py                        # AI илрүүлэгч
└── .github/workflows/grade.yml               # GitHub Actions автомат шалгагч
```

## 🚀 Лаб хийх заавар (Алхам алхмаар)

### Алхам 1: Repo-г Fork хийх

1. Браузераар [`UEFA-OPP/lab12-template`](https://github.com/UEFA-OPP/lab12-template) руу орно
2. Баруун дээд буланд **Fork** товч дарна
3. Owner-ээр өөрийн account-ийг сонгоод **Create fork** дарна

### Алхам 2: Компьютер дээрээ Clone хийх

```bash
git clone https://github.com/<таны-username>/lab12-template.git
cd lab12-template
```

### Алхам 3: Өөрийн нэрээр branch үүсгэх

```bash
git checkout -b lab12/<өөрийн-нэр>
```

### Алхам 4: Даалгаврын зааврыг унших

```bash
cat assignments/dungeon/README.md
```

### Алхам 5: Код бичих

`assignments/dungeon/` хавтас дахь `// TODO` комментуудыг өөрийн кодоор соль. Ядаж дараах түвшнүүдийг хийж үзнэ үү:

- 🟢 **Core (60 оноо)** — `InsufficientGoldException` (checked), `NotEnoughManaException` (unchecked), `Item`, `Shop.buy`
- 🟡 **Stretch (30 оноо)** — `DungeonLog` try-with-resources, `Shop.autoSave` finally block
- 🔴 **Bonus (10 оноо)** — exception chaining, reflection шалгалт

### Алхам 6: Локал тест ажиллуулах

```bash
# Бүх тестийг ажиллуулах
bash scripts/run_tests.sh

# Тодорхой tier дангаар шалгах
bash scripts/run_tests.sh --tag core
bash scripts/run_tests.sh --tag stretch
bash scripts/run_tests.sh --tag bonus
```

**Жишээ output:**
```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
  Lab 12: Dungeon Adventure
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[core]    ✓ 9/9 tests passed  → 60.0/60
[stretch] ✓ 3/3 tests passed  → 30.0/30
[bonus]   △ 1/2 tests passed  → 5.0/10
─────────────────────────────────────
НИЙТ ОНОО: 95.0 / 100
```

### Алхам 7: Commit хийх

```bash
git add assignments/
git commit -m "Implement dungeon exceptions - <your name>"
```

> **Анхаар:** `tests/`, `scripts/`, `.github/` хавтсуудыг өөрчлөх/commit хийх хэрэггүй.

### Алхам 8: GitHub руу Push хийх

```bash
git push origin lab12/<өөрийн-нэр>
```

### Алхам 9: Pull Request (PR) үүсгэх

1. `https://github.com/<таны-username>/lab12-template` руу орно
2. **Compare & pull request** товч дарна
3. PR title-д **өөрийн нэр, бүлгийг** бичнэ. Жишээ: `Bat-Erdene - SE401`
4. **Create pull request** дарна

### Алхам 10: Автомат шалгалтын дүнг харах

PR үүсгэсний дараа GitHub Actions автоматаар ажиллана. Үр дүн PR-т автоматаар коммент болж бичигдэнэ.

## 📊 Оноо тооцох систем

| Tier | Жин | Тайлбар |
|------|-----|---------|
| 🟢 **Core** | **60%** | Custom exception класс, throws, catch |
| 🟡 **Stretch** | **30%** | try-with-resources, finally block |
| 🔴 **Bonus** | **10%** | Exception chaining, reflection шалгалт |

**Формула:**
```
score = (core_passed / core_total) * 60
      + (stretch_passed / stretch_total) * 30
      + (bonus_passed / bonus_total) * 10
```

## 🤖 AI Detection policy

| Оноо | Түвшин | Үр дагавар |
|------|--------|------------|
| 0-19 | ✅ **LOW** | Асуудалгүй. Сайн! |
| 20-39 | ⚠️ **MEDIUM** | Багш кодыг шалгана. |
| 40+ | 🚨 **HIGH** | Онооноос **50% хасна**. |

## ⚠️ Дүрэм

1. **Тест файлыг өөрчлөхгүй** — `tests/DungeonTest.java`-г хөндөхгүй
2. **Зөвхөн `assignments/dungeon/`-д код бичнэ** (`Character.java`-аас гадуур)
3. **AI ашиглахгүй**
4. **Өөрийн branch дээр ажиллана**
5. **Commit message, код — англиар**

## 🛠️ Шаардлага

- **Java 17+**
- **Python 3.11+**
- **Bash**
- **curl**
- **Git**

## 📞 Асуулт байвал

Багшаасаа асуу. Discord / classroom channel-аар бичиж болно. Амжилт хүсье, адвенчурер! 🗡️🛡️
