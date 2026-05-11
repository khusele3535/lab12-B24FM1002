#!/usr/bin/env python3
import sys
import os
import re
import json
import statistics

# ANSI colors
RED = "\033[91m"
YELLOW = "\033[93m"
GREEN = "\033[92m"
CYAN = "\033[96m"
BOLD = "\033[1m"
RESET = "\033[0m"
DIM = "\033[2m"

AI_PHRASES = [
    "This method", "This class", "This constructor",
    "Creates a new", "Returns the", "Returns a",
    "Initializes the", "Sets the", "Gets the",
    "The following", "Represents a", "Provides a",
    "Implements the", "Handles the", "Processes the",
    "Validates the", "Checks if", "Determines whether",
    "Constructs a new", "Responsible for",
]

AI_COMMENT_PATTERNS = [
    "// Getters and Setters",
    "// Getter and Setter",
    "// Constructor",
    "// Default constructor",
    "// Parameterized constructor",
    "// Fields",
    "// Instance variables",
    "// Member variables",
    "// Private fields",
    "// Methods",
    "// Utility methods",
    "// Helper methods",
    "// Override",
    "// Main method",
    "// Constants",
    "// Attributes",
    "// Properties",
]


def read_file(path):
    with open(path, "r", encoding="utf-8") as f:
        return f.read()


def analyze_file(filepath):
    content = read_file(filepath)
    lines = content.split("\n")
    non_empty_lines = [l for l in lines if l.strip()]
    code_lines = [l for l in lines if l.strip() and not l.strip().startswith("//") and not l.strip().startswith("*") and not l.strip().startswith("/*")]
    comment_lines = [l for l in lines if l.strip().startswith("//") or l.strip().startswith("*") or l.strip().startswith("/*")]

    results = []
    total_score = 0

    # 1. Javadoc (/** ... */) 3+ удаа
    javadoc_count = len(re.findall(r'/\*\*', content))
    score1 = 18 if javadoc_count >= 3 else 0
    total_score += score1
    results.append({
        "check": "Javadoc comment (3+)",
        "found": javadoc_count,
        "score": score1,
        "max": 18
    })

    # 2. @param/@return 3+ удаа
    param_return_count = len(re.findall(r'@(?:param|return)', content))
    score2 = 15 if param_return_count >= 3 else 0
    total_score += score2
    results.append({
        "check": "@param/@return tags (3+)",
        "found": param_return_count,
        "score": score2,
        "max": 15
    })

    # 3. Comment ratio > 35%
    comment_ratio = len(comment_lines) / max(len(non_empty_lines), 1) * 100
    score3 = 12 if comment_ratio > 35 else 0
    total_score += score3
    results.append({
        "check": f"Comment ratio > 35%",
        "found": f"{comment_ratio:.1f}%",
        "score": score3,
        "max": 12
    })

    # 4. AI phrases
    ai_phrase_count = 0
    found_phrases = []
    for phrase in AI_PHRASES:
        matches = content.count(phrase)
        if matches > 0:
            ai_phrase_count += matches
            found_phrases.append(phrase)
    score4 = min(20, ai_phrase_count * 5) if ai_phrase_count > 0 else 0
    total_score += score4
    results.append({
        "check": "AI phrases",
        "found": f"{ai_phrase_count} ({', '.join(found_phrases[:3])}{'...' if len(found_phrases) > 3 else ''})" if found_phrases else "0",
        "score": score4,
        "max": 20
    })

    # 5. Known AI patterns
    ai_pattern_count = 0
    found_patterns = []
    for pattern in AI_COMMENT_PATTERNS:
        if pattern.lower() in content.lower():
            ai_pattern_count += 1
            found_patterns.append(pattern)
    score5 = min(16, ai_pattern_count * 4) if ai_pattern_count > 0 else 0
    total_score += score5
    results.append({
        "check": "Known AI comment patterns",
        "found": f"{ai_pattern_count} ({', '.join(found_patterns[:2])}{'...' if len(found_patterns) > 2 else ''})" if found_patterns else "0",
        "score": score5,
        "max": 16
    })

    # 6. Perfect indentation (бүх мөр 4-ийн үржвэрээр indent хийгдсэн)
    indented_lines = [l for l in lines if l and not l.strip() == "" and l[0] == " "]
    if len(indented_lines) > 5:
        perfect_indent = all(
            (len(l) - len(l.lstrip())) % 4 == 0
            for l in indented_lines
        )
        score6 = 8 if perfect_indent else 0
    else:
        score6 = 0
    total_score += score6
    results.append({
        "check": "Perfect 4-space indentation",
        "found": "Yes" if score6 > 0 else "No",
        "score": score6,
        "max": 8
    })

    # 7. Line length stddev < 12
    if len(code_lines) > 5:
        line_lengths = [len(l) for l in code_lines]
        stddev = statistics.stdev(line_lengths) if len(line_lengths) > 1 else 0
        score7 = 7 if stddev < 12 else 0
    else:
        stddev = 0
        score7 = 0
    total_score += score7
    results.append({
        "check": "Line length stddev < 12",
        "found": f"{stddev:.1f}",
        "score": score7,
        "max": 7
    })

    # 8. Exception throw 2+ удаа
    exception_count = len(re.findall(r'throw\s+new\s+\w+Exception', content))
    score8 = 10 if exception_count >= 2 else 0
    total_score += score8
    results.append({
        "check": "Exception throws (2+)",
        "found": exception_count,
        "score": score8,
        "max": 10
    })

    # 9. Blank line pattern (жигд зай)
    blank_positions = [i for i, l in enumerate(lines) if l.strip() == ""]
    if len(blank_positions) > 3:
        gaps = [blank_positions[i+1] - blank_positions[i] for i in range(len(blank_positions)-1)]
        if len(gaps) > 2:
            gap_stddev = statistics.stdev(gaps) if len(gaps) > 1 else 0
            score9 = 5 if gap_stddev < 1.5 else 0
        else:
            score9 = 0
    else:
        score9 = 0
    total_score += score9
    results.append({
        "check": "Uniform blank line spacing",
        "found": "Yes" if score9 > 0 else "No",
        "score": score9,
        "max": 5
    })

    # 10. Method naming pattern (хэт жигд camelCase)
    methods = re.findall(r'(?:public|private|protected)\s+\w+\s+(\w+)\s*\(', content)
    methods = [m for m in methods if m not in ("main", "toString")]
    if len(methods) >= 3:
        lengths = [len(m) for m in methods]
        name_stddev = statistics.stdev(lengths) if len(lengths) > 1 else 0
        has_consistent_prefix = len(set(m[:3] for m in methods)) <= 2
        score10 = 6 if (name_stddev < 3 and has_consistent_prefix) else 0
    else:
        score10 = 0
    total_score += score10
    results.append({
        "check": "Overly uniform method naming",
        "found": f"{len(methods)} methods",
        "score": score10,
        "max": 6
    })

    # 11. Unused imports
    imports = re.findall(r'import\s+([\w.]+);', content)
    unused_imports = []
    for imp in imports:
        class_name = imp.split(".")[-1]
        if class_name == "*":
            continue
        uses = len(re.findall(r'\b' + class_name + r'\b', content)) - 1  # -1 import мөрийг хасах
        if uses <= 0:
            unused_imports.append(class_name)
    score11 = min(4, len(unused_imports) * 2)
    total_score += score11
    results.append({
        "check": "Unused imports",
        "found": f"{len(unused_imports)} ({', '.join(unused_imports)})" if unused_imports else "0",
        "score": score11,
        "max": 4
    })

    # Level тодорхойлох
    if total_score >= 40:
        level = "HIGH"
        emoji = "🚨"
        color = RED
    elif total_score >= 20:
        level = "MEDIUM"
        emoji = "⚠️"
        color = YELLOW
    else:
        level = "LOW"
        emoji = "✅"
        color = GREEN

    return {
        "file": filepath,
        "score": total_score,
        "level": level,
        "emoji": emoji,
        "color": color,
        "checks": results
    }


def print_report(report):
    color = report["color"]
    print()
    print(f"{BOLD}{CYAN}{'='*60}{RESET}")
    print(f"{BOLD}{CYAN}  AI Detection Report: {os.path.basename(report['file'])}{RESET}")
    print(f"{BOLD}{CYAN}{'='*60}{RESET}")
    print()

    for check in report["checks"]:
        score_str = f"+{check['score']}" if check["score"] > 0 else " 0"
        indicator = f"{RED}{score_str}{RESET}" if check["score"] > 0 else f"{DIM}{score_str}{RESET}"
        print(f"  {indicator}/{check['max']:<3}  {check['check']:<35} Found: {check['found']}")

    print()
    print(f"  {BOLD}{'─'*50}{RESET}")
    print(f"  {BOLD}Нийт оноо: {color}{report['score']}/121{RESET}")
    print(f"  {BOLD}Түвшин:    {color}{report['emoji']} {report['level']}{RESET}")
    print()

    if report["level"] == "LOW":
        print(f"  {GREEN}AI-ийн шинж тэмдэг бага байна. Сайн!{RESET}")
    elif report["level"] == "MEDIUM":
        print(f"  {YELLOW}Зарим AI-ийн шинж тэмдэг илэрлээ. Анхаарна уу!{RESET}")
    else:
        print(f"  {RED}AI-ийн шинж тэмдэг өндөр байна! Код шалгах шаардлагатай.{RESET}")

    print(f"{CYAN}{'='*60}{RESET}")
    print()


def main():
    if len(sys.argv) < 2:
        print(f"{RED}Хэрэглээ: python3 ai_detector.py <file.java> [--json]{RESET}")
        sys.exit(1)

    filepath = sys.argv[1]
    json_mode = "--json" in sys.argv

    if not os.path.exists(filepath):
        print(f"{RED}Файл олдсонгүй: {filepath}{RESET}")
        sys.exit(1)

    report = analyze_file(filepath)

    if json_mode:
        output = {
            "file": report["file"],
            "score": report["score"],
            "level": report["level"],
            "checks": report["checks"]
        }
        print(json.dumps(output, ensure_ascii=False, indent=2))
    else:
        print_report(report)

    if report["level"] == "HIGH":
        sys.exit(1)
    sys.exit(0)


if __name__ == "__main__":
    main()
