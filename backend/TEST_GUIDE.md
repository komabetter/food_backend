# Test Execution Guide

## How to Run Service Tests

### Run Only OrderServiceTest
```bash
mvn test -Dtest=OrderServiceTest
```

### Run All Tests
```bash
mvn test
```

### Status ID Mapping (Inferred from Code)
- `1`: Pending
- `2`: Confirmed  
- `3`: In Progress
- `4`: Completed
- `5`: Cancelled

### 1. Updated Business Rules Implementation

The service now properly enforces:

✅ **Sequential Progression Only**:
- 1 → 2 (Pending → Confirmed)
- 2 → 3 (Confirmed → In Progress)  
- 3 → 4 (In Progress → Completed)

✅ **Cancellation Rules**:
- Any status → 5 (Cancelled) **BEFORE** completion
- Status 4 → 5 is **BLOCKED** (cannot cancel after completion)

❌ **Prevented Actions**:
- Backward progression (3→1, 4→2)
- Status skipping (1→3, 2→4)
- Same status updates (2→2, 3→3)
- Cancellation after completion (4→5)

## Test Results

```
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

All 11 tests now pass successfully, covering:
- ✅ Valid sequential progressions
- ✅ Cancellation before completion
- ❌ Backward progression prevention
- ❌ Status skipping prevention  
- ❌ Cancellation after completion prevention
- ❌ Same status update prevention
- ❌ Non-existent order handling