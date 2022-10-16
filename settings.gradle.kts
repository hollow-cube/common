rootProject.name = "common"

include(":modules")
// Common + Modules which depend on it
include(":modules:common")
include(":modules:dev")
// Standalone modules
include(":modules:mql")
include(":modules:schem")
include(":modules:motion")

include(":modules:test")
include(":tools:canary:api")
include(":tools:canary:host")
