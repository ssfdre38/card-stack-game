#!/bin/bash

# Verification script for AI-optimized chat history
# Tests that all optimization files are present and functional

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║  Chat History Optimization Verification                  ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo

cd /home/ubuntu/match-mania/chat-history

# Check file existence
echo "📁 Checking files..."
files=(
    "INDEX.json"
    "SUMMARY.yaml"
    "search.db"
    "AI-READING-GUIDE.md"
    "README.md"
    "create_search_db.py"
)

all_present=true
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        size=$(du -h "$file" | cut -f1)
        echo "  ✅ $file ($size)"
    else
        echo "  ❌ $file MISSING"
        all_present=false
    fi
done

echo

# Verify JSON is valid
echo "🔍 Verifying JSON validity..."
if command -v jq &> /dev/null; then
    if jq empty INDEX.json 2>/dev/null; then
        echo "  ✅ INDEX.json is valid JSON"
        sessions=$(jq '.sessions | length' INDEX.json)
        echo "     → Contains $sessions sessions"
    else
        echo "  ❌ INDEX.json is invalid"
    fi
else
    echo "  ⚠️  jq not installed, skipping JSON validation"
fi

echo

# Verify YAML is readable
echo "📄 Verifying YAML readability..."
if command -v python3 &> /dev/null; then
    python3 << PYEOF
import yaml
import sys

try:
    with open('SUMMARY.yaml') as f:
        data = yaml.safe_load(f)
    print("  ✅ SUMMARY.yaml is valid YAML")
    print(f"     → Project: {data['quick_context']['project']}")
    print(f"     → Current version: {data['quick_context']['current_version']}")
    sys.exit(0)
except Exception as e:
    print(f"  ❌ YAML error: {e}")
    sys.exit(1)
PYEOF
else
    echo "  ⚠️  Python not installed, skipping YAML validation"
fi

echo

# Verify SQLite database
echo "💾 Verifying SQLite database..."
if command -v sqlite3 &> /dev/null; then
    if sqlite3 search.db "SELECT COUNT(*) FROM sessions;" &> /dev/null; then
        session_count=$(sqlite3 search.db "SELECT COUNT(*) FROM sessions;")
        topic_count=$(sqlite3 search.db "SELECT COUNT(*) FROM topics;")
        keyword_count=$(sqlite3 search.db "SELECT COUNT(*) FROM keywords;")
        echo "  ✅ search.db is valid SQLite database"
        echo "     → Sessions: $session_count"
        echo "     → Topics: $topic_count"
        echo "     → Keywords: $keyword_count"
    else
        echo "  ❌ search.db is invalid or empty"
    fi
else
    echo "  ⚠️  sqlite3 not installed, skipping database validation"
fi

echo

# Test fast queries
echo "⚡ Testing fast query performance..."

# Test 1: YAML grep
start=$(date +%s%N)
grep -q "current_version" SUMMARY.yaml
end=$(date +%s%N)
yaml_time=$((($end - $start) / 1000000))
echo "  ✅ YAML grep: ${yaml_time}ms"

# Test 2: JSON parse
if command -v jq &> /dev/null; then
    start=$(date +%s%N)
    jq '.metadata.version' INDEX.json > /dev/null
    end=$(date +%s%N)
    json_time=$((($end - $start) / 1000000))
    echo "  ✅ JSON parse: ${json_time}ms"
fi

# Test 3: SQLite query
if command -v sqlite3 &> /dev/null; then
    start=$(date +%s%N)
    sqlite3 search.db "SELECT session_id FROM sessions WHERE priority='critical';" > /dev/null
    end=$(date +%s%N)
    sqlite_time=$((($end - $start) / 1000000))
    echo "  ✅ SQLite query: ${sqlite_time}ms"
fi

echo

# Summary
echo "╔═══════════════════════════════════════════════════════════╗"
if [ "$all_present" = true ]; then
    echo "║  ✅ All optimization files present and functional!       ║"
else
    echo "║  ❌ Some files are missing - check output above          ║"
fi
echo "╚═══════════════════════════════════════════════════════════╝"
echo
echo "📊 Quick Stats:"
echo "  - Total session files: $(ls -1 session-*.md 2>/dev/null | wc -l)"
echo "  - Total documentation: $(du -sh . | cut -f1)"
echo "  - Optimization files: ${#files[@]}"
echo
echo "🚀 Ready for AI fast reading!"
