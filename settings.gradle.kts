rootProject.name = "common"

include(":modules")
include(":modules:common")
include(":modules:dev")
include(":modules:test")
include("modules:gui")
findProject(":modules:gui")?.name = "gui"
