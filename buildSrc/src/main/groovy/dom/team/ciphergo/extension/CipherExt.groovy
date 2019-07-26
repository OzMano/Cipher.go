package dom.team.ciphergo.extension

import org.gradle.api.Project

class CipherExt {

    GoExt go

    Project project

    CipherExt(Project project) {
        this.go = new GoExt(project)
        this.project = project
    }

    def go(Closure closure) {
//        closure.resolveStrategy = Closure.DELEGATE_ONLY
//        closure.delegate = so
        project.configure(go, closure)
    }
}