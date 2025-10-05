#!/bin/bash

################################################################################
# Match Mania - Quick Test Script
# Fast single-device test for rapid development testing
################################################################################

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}Match Mania - Quick Test${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Check if emulator is running
if ! adb devices | grep -q "emulator"; then
    echo "❌ No emulator running. Please start an emulator first."
    echo ""
    echo "To start the test emulator:"
    echo "  cd $SCRIPT_DIR"
    echo "  ./run-comprehensive-tests.sh"
    exit 1
fi

echo "✓ Emulator detected"
echo ""

# Run the comprehensive test
exec "$SCRIPT_DIR/run-comprehensive-tests.sh"
