rootProject.name = "common"

include(":modules")
// Common + Modules which depend on it
include(":modules:common")
include(":modules:dev")
// Standalone modules
include(":modules:schem")
include(":modules:test")