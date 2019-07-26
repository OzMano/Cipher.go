package dom.team.ciphergo.extension

import dom.team.ciphergo.extension.KeyExt
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class GoExt {

    NamedDomainObjectContainer<KeyExt> keys

    String signature = ""
    String encryptSeed = "Cipher.go@DEFAULT"

    GoExt(Project project) {
        keys = project.container(KeyExt)
    }

    def keys(Closure closure) {
        keys.configure closure
    }

    @Override
    public String toString() {
        return "GoExt{" +
                "keys=" + keys +
                ", signature='" + signature + '\'' +
                ", secretKey='" + encryptSeed + '\'' +
                '}';
    }
}