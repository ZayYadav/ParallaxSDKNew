#ifndef TDCORE_GUARD_H
#define TDCORE_GUARD_H

namespace tdcore {
namespace guard {

/**
 * Native, fail-closed integrity checks for the TDCore SDK.
 *
 * The guard looks for a live debugger, common instrumentation agents and
 * inline function hooks. Any positive result permanently poisons this process:
 * the signed-policy lease is wiped, feature bits return zero and every later
 * authorization attempt is denied. There is no repair path and no offline
 * fallback. All sensitive literals are XOR-encrypted at compile time.
 */
bool selfCheck();
bool compromised();
void wipe();

} // namespace guard
} // namespace tdcore

#endif // TDCORE_GUARD_H
