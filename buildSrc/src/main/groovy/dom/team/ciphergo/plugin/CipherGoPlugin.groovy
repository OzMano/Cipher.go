package dom.team.ciphergo.plugin

import com.android.build.gradle.AppExtension
import dom.team.ciphergo.extension.CipherExt
import dom.team.ciphergo.generater.CMakeListsBuilder
import dom.team.ciphergo.task.GenerateCipherGoHeaderTask
import dom.team.ciphergo.task.GenerateJavaClientFileTask
import dom.team.ciphergo.utils.IOUtils
import dom.team.ciphergo.utils.StringUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class CipherGoPlugin implements Plugin<Project> {

    private String originCmakeListPath

    @Override
    void apply(Project project) {

        project.extensions.add("cipher", new CipherExt(project))

        setupProjectNativeSupport(project)

    }

    private def createTasks(Project project, AppExtension android) {

        def generateCmakeListFileTask = project.tasks.create("generateCmakeListFile") {
            group "cipher.go"
            doLast {
                generateCMakeListsFile(project, originCmakeListPath)
            }
        }

        def archiveFile = getNativeArchiveFile(project)
        def copyNativeArchiveTask = project.tasks.create("copyNativeArchive", Copy) {
            group "cipher.go"
            from archiveFile
            include "src/main/cpp/**"
            include "CMakeLists.txt"
            exclude "src/main/cpp/include/extern-keys.h"
            into new File(project.buildDir, "cipher.go")
        }

        copyNativeArchiveTask.dependsOn generateCmakeListFileTask

        android.applicationVariants.all { variant ->

            def configs = project.cipher.go
            def keys = configs.keys.asList()
            def generateCipherGoExternalTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}CipherGoHeader", GenerateCipherGoHeaderTask)
            generateCipherGoExternalTask.configure {
                it.keyExts = keys
                it.outputDir = IOUtils.getNativeHeaderDir(project)
                it.signature = configs.signature
                it.secretKey = configs.encryptSeed
            }
            project.getTasksByName("generateJsonModel${StringUtils.capitalize(variant.name)}", false).each {
                it.dependsOn copyNativeArchiveTask
                it.dependsOn generateCipherGoExternalTask
            }
            def outputDir = new File(project.buildDir, "/generated/source/cipher.go/${variant.name}")
            def generateJavaClientTask = project.tasks.create("generate${StringUtils.capitalize(variant.name)}JavaClient", GenerateJavaClientFileTask)
            generateJavaClientTask.configure {
                it.keyExts = keys
                it.outputDir = outputDir
            }
            variant.registerJavaGeneratingTask(generateJavaClientTask, outputDir)

            def copyJavaArchiveTask = project.tasks.create("copyJavaArchive${StringUtils.capitalize(variant.name)}", Copy) {
                group "cipher.go"
                from archiveFile
                include "src/main/java/**"
                exclude "src/main/java/dom/team/ciphergo/dylan/**"
                exclude "META-INF/**"
                exclude "dom/team/ciphergo/**"
                exclude "CMakeLists.txt"
                eachFile {
                    it.path = it.path.replaceFirst("src/main/java/", "")
                }
                into outputDir
            }

            generateJavaClientTask.dependsOn copyJavaArchiveTask
        }
    }


    private def setupProjectNativeSupport(Project project) {
        project.afterEvaluate {
            unzipNativeArchive(project)
            def android = project.extensions.findByType(AppExtension)
            originCmakeListPath = android.externalNativeBuild.cmake.path?.canonicalPath
            File targetFile = generateCMakeListsFile(project, originCmakeListPath)
            android.externalNativeBuild {
                cmake {
                    path targetFile.canonicalPath
                }
            }
            createTasks(project, android)
        }
    }

    private static def unzipNativeArchive(Project project) {
        def archiveFile = getNativeArchiveFile(project)
        project.copy {
            from archiveFile
            include "src/main/cpp/**"
            include "CMakeLists.txt"
            exclude "src/main/cpp/include/extern-keys.h"
            into new File(project.buildDir, "cipher.go")
        }

    }

    private static def getNativeArchiveFile(Project project) {
        if (project.rootProject.subprojects.find { it.name == "dylan" } != null) {
            return project.rootProject.file("dylan").canonicalPath
        } else {
            def archiveZip = findNativeArchiveFromBuildscript(project)
            if (archiveZip == null) {
                archiveZip = findNativeArchiveFromBuildscript(project.rootProject)
            }
            archiveZip
        }
    }

    private static def findNativeArchiveFromBuildscript(Project project) {
        def archiveZip = null
        project.buildscript.configurations.findAll {
            project.gradle.gradleVersion >= '4.0' ? it.isCanBeResolved() : true
        }.each { config ->
            File file = config.files.find {
                it.name.toUpperCase(Locale.getDefault()).contains("CIPHER.GO")
            }
            if (file != null) {
                archiveZip = project.zipTree(file)
            }
        }
        return archiveZip
    }

    private
    static File generateCMakeListsFile(Project project, String originCMakeListsPath) {
        def outputDir = new File(project.buildDir, "/cipher.go/cmake")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        def targetFile = new File(outputDir, "CMakeLists.txt")
        def writer = new FileWriter(targetFile)
        new CMakeListsBuilder("${project.buildDir.canonicalPath}/cipher.go/CMakeLists.txt").setOriginCMakePath(originCMakeListsPath).build().each {
            writer.append(it)
        }
        writer.flush()
        writer.close()
        targetFile
    }
}