package dom.team.ciphergo.task

import dom.team.ciphergo.extension.KeyExt
import dom.team.ciphergo.generater.CipherGoHeaderBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class GenerateCipherGoHeaderTask extends DefaultTask {

    private static final String TARGET_FILE_NAME = "extern-keys.h"
    private static final String GROUP_NAME = 'cipher.go'

    @OutputDirectory
    File outputDir

    @Input
    List<KeyExt> keyExts

    @Input
    String signature

    @Input
    String secretKey

    GenerateCipherGoHeaderTask() {
        group = GROUP_NAME
    }

    @TaskAction
    void generate() {
        def targetFile = new File(outputDir, TARGET_FILE_NAME)
        def writer = new FileWriter(targetFile)
        new CipherGoHeaderBuilder(TARGET_FILE_NAME, keyExts)
                .setSignature(signature)
                .setSecretKey(secretKey)
                .build()
                .each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
    }


}