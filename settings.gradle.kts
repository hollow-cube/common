rootProject.name = "common"

include(":modules")
// Common + Modules which depend on it
include(":modules:common")
include(":modules:dev")
// Standalone modules
include("modules:instances")
include(":modules:mql")
include(":modules:schem")
include(":modules:block-placement")
include(":modules:test")
