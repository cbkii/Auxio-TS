#!/bin/bash
# Check for unauthorized direct logging calls in production source.
# Timber (L.) is allowed as it is gated by trees in Auxio.kt.
# PerfTimer is allowed as it is debug-only.

FAIL=0

# Log.x
echo "Checking for direct Log.x calls..."
LOG_CALLS=$(grep -r "Log\." app/src/main/java | grep -v "import" | grep -v "BackportBottomSheetBehavior" | grep -v "Auxio.kt")
if [ -n "$LOG_CALLS" ]; then
    echo "ERROR: Found direct Log.x calls. Use Timber (L.) or PerfTimer."
    echo "$LOG_CALLS"
    FAIL=1
fi

# println
echo "Checking for println calls..."
PRINTLN_CALLS=$(grep -r "println" app/src/main/java | grep -v "printStackTrace")
if [ -n "$PRINTLN_CALLS" ]; then
    echo "ERROR: Found println calls."
    echo "$PRINTLN_CALLS"
    FAIL=1
fi

# printStackTrace
echo "Checking for printStackTrace calls..."
PST_CALLS=$(grep -r "printStackTrace" app/src/main/java | grep -v "Auxio.kt")
if [ -n "$PST_CALLS" ]; then
    echo "ERROR: Found printStackTrace calls. Use Timber (L.e)."
    echo "$PST_CALLS"
    FAIL=1
fi

if [ $FAIL -eq 0 ]; then
    echo "Logging silence check passed."
fi
# Avoid exit here to keep session alive if called as part of a block
[ $FAIL -eq 0 ]
