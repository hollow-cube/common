rootProject.name = "common"

include(":modules")
// Common + Modules which depend on it
include(":modules:common")
include(":modules:dev")
include(":modules:test")
include("modules:gui")
findProject(":modules:gui")?.name = "gui"
// Standalone modules
include(":modules:mql")
include(":modules:schem")
include(":modules:test")
