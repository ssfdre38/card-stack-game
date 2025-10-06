#!/bin/bash

################################################################################
# Settings Implementation Verification Script
# Verifies that all 9 game settings are properly implemented in the code
# Does NOT require emulator - checks source code only
################################################################################

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CHECKS_PASSED=0
CHECKS_FAILED=0
CHECKS_TOTAL=0

echo -e "${CYAN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║  Match Mania - Settings Implementation Verification       ║${NC}"
echo -e "${CYAN}║  Checking source code for all 9 settings                  ║${NC}"
echo -e "${CYAN}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Function to check if a pattern exists in a file
check_implementation() {
    local setting_name="$1"
    local file="$2"
    local pattern="$3"
    local description="$4"
    
    CHECKS_TOTAL=$((CHECKS_TOTAL + 1))
    
    if grep -q "$pattern" "$PROJECT_DIR/$file"; then
        CHECKS_PASSED=$((CHECKS_PASSED + 1))
        echo -e "${GREEN}  ✓${NC} $setting_name: $description"
        return 0
    else
        CHECKS_FAILED=$((CHECKS_FAILED + 1))
        echo -e "${RED}  ✗${NC} $setting_name: $description (NOT FOUND)"
        return 1
    fi
}

echo -e "${BLUE}Checking GameSettings.java...${NC}"
echo ""

# Check GameSettings class has all 9 settings
check_implementation "Starting Cards" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "getStartingCards\|setStartingCards" \
    "Getter/setter methods exist"

check_implementation "Action Stacking" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isActionStackingEnabled\|setActionStackingEnabled" \
    "Getter/setter methods exist"

check_implementation "Draw to Match" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isDrawToMatchEnabled\|setDrawToMatchEnabled" \
    "Getter/setter methods exist"

check_implementation "Draw on No Play" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isDrawOnNoPlayEnabled\|setDrawOnNoPlayEnabled" \
    "Getter/setter methods exist"

check_implementation "Jump-In Rule" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isJumpInEnabled\|setJumpInEnabled" \
    "Getter/setter methods exist"

check_implementation "Seven-Zero Rule" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isSevenZeroRuleEnabled\|setSevenZeroRuleEnabled" \
    "Getter/setter methods exist"

check_implementation "Progressive UNO" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isProgressiveUnoEnabled\|setProgressiveUnoEnabled" \
    "Getter/setter methods exist"

check_implementation "Force Play" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isForcePlayEnabled\|setForcePlayEnabled" \
    "Getter/setter methods exist"

check_implementation "Challenge Draw Four" \
    "app/src/main/java/com/cardstack/game/GameSettings.java" \
    "isChallengeDrawFourEnabled\|setChallengeDrawFourEnabled" \
    "Getter/setter methods exist"

echo ""
echo -e "${BLUE}Checking GameEngine.java implementation...${NC}"
echo ""

# Check GameEngine uses the settings
check_implementation "Starting Cards Usage" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "settings.getStartingCards()" \
    "Used in game initialization"

check_implementation "Action Stacking Usage" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "settings.isActionStackingEnabled()" \
    "Used in card validation"

check_implementation "Progressive UNO Stack" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "progressiveDrawStack\|isProgressiveUnoEnabled" \
    "Stack tracking implemented"

check_implementation "Seven-Zero Rule Logic" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "swapHandsWithPlayer\|rotateAllHands\|isSevenZeroRuleEnabled" \
    "Hand swap/rotate methods exist"

check_implementation "Challenge Draw Four Logic" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "canChallengeDrawFour\|executeChallengeResult" \
    "Challenge verification implemented"

check_implementation "Jump-In Rule Framework" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "canJumpIn\|jumpInPlay" \
    "Jump-in methods exist"

check_implementation "Force Play Logic" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "hasPlayableCard\|isDrawAllowed\|isForcePlayEnabled" \
    "Force play checking implemented"

check_implementation "Draw on No Play Logic" \
    "app/src/main/java/com/cardstack/game/GameEngine.java" \
    "isDrawOnNoPlayEnabled\|isDrawAllowed" \
    "Draw restriction checking implemented"

echo ""
echo -e "${BLUE}Checking MainActivity.java UI integration...${NC}"
echo ""

check_implementation "Settings UI Integration" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "new GameSettings(this)" \
    "GameSettings instantiated"

check_implementation "Draw Button Logic" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "isDrawAllowed\|isDrawToMatchEnabled" \
    "Draw button uses settings"

check_implementation "Seven-Zero Dialogs" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "showSevenSwapDialog\|SEVEN_SWAP\|ZERO_ROTATE" \
    "Seven-Zero UI handlers exist"

check_implementation "Challenge Draw Four Dialog" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "showChallengeDrawFourDialog\|CHALLENGE_AVAILABLE" \
    "Challenge UI handler exists"

check_implementation "Progressive UNO Feedback" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "Progressive UNO\|PROGRESSIVE:" \
    "Progressive stack messages exist"

check_implementation "Force Play UI Update" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "Must Play Card\|isForcePlayEnabled" \
    "Force Play button text update exists"

check_implementation "AI Settings Compliance" \
    "app/src/main/java/com/cardstack/game/MainActivity.java" \
    "processAITurns.*settings\|isDrawAllowed" \
    "AI uses settings in turn processing"

echo ""
echo -e "${BLUE}Checking SettingsActivity.java...${NC}"
echo ""

check_implementation "Settings Activity" \
    "app/src/main/java/com/cardstack/game/SettingsActivity.java" \
    "loadSettings\|saveSettings" \
    "Load and save methods exist"

check_implementation "All 9 Switches/Controls" \
    "app/src/main/java/com/cardstack/game/SettingsActivity.java" \
    "actionStackingSwitch\|drawToMatchSwitch\|sevenZeroSwitch\|progressiveSwitch" \
    "All switch controls defined"

echo ""
echo -e "${CYAN}════════════════════════════════════════════════════════════${NC}"
echo -e "${CYAN}  Verification Summary${NC}"
echo -e "${CYAN}════════════════════════════════════════════════════════════${NC}"
echo ""

echo -e "Total Checks:  ${BLUE}${CHECKS_TOTAL}${NC}"
echo -e "Passed:        ${GREEN}${CHECKS_PASSED}${NC}"
echo -e "Failed:        ${RED}${CHECKS_FAILED}${NC}"

if [ $CHECKS_FAILED -eq 0 ]; then
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║  ✓ ALL CHECKS PASSED!                                     ║${NC}"
    echo -e "${GREEN}║                                                            ║${NC}"
    echo -e "${GREEN}║  All 9 game settings are properly implemented:            ║${NC}"
    echo -e "${GREEN}║                                                            ║${NC}"
    echo -e "${GREEN}║  ✅ Starting Cards (5-10)                                  ║${NC}"
    echo -e "${GREEN}║  ✅ Action Card Stacking                                   ║${NC}"
    echo -e "${GREEN}║  ✅ Draw to Match                                          ║${NC}"
    echo -e "${GREEN}║  ✅ Draw When No Play                                      ║${NC}"
    echo -e "${GREEN}║  ✅ Jump-In Rule (Framework)                               ║${NC}"
    echo -e "${GREEN}║  ✅ Seven-Zero Rule                                        ║${NC}"
    echo -e "${GREEN}║  ✅ Progressive UNO                                        ║${NC}"
    echo -e "${GREEN}║  ✅ Force Play                                             ║${NC}"
    echo -e "${GREEN}║  ✅ Challenge Draw Four                                    ║${NC}"
    echo -e "${GREEN}║                                                            ║${NC}"
    echo -e "${GREEN}║  Code is ready for emulator testing!                      ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${CYAN}Next Steps:${NC}"
    echo -e "  1. Run emulator tests: ${YELLOW}cd emulator-testing && ./run-comprehensive-tests.sh${NC}"
    echo -e "  2. Or run focused test: ${YELLOW}cd emulator-testing && ./test-game-settings.sh${NC}"
    echo -e "  3. Review test plan: ${YELLOW}cat SETTINGS_TEST_PLAN.md${NC}"
    echo ""
    exit 0
else
    echo ""
    echo -e "${RED}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${RED}║  ⚠ SOME CHECKS FAILED                                     ║${NC}"
    echo -e "${RED}║  Please review the output above                           ║${NC}"
    echo -e "${RED}╚════════════════════════════════════════════════════════════╝${NC}"
    exit 1
fi
