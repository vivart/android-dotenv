import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File

open class GenerateConfiguration : DefaultTask() {
    companion object {
        private const val PACKAGE = "com.example.android_dotenv"
        private const val CLASS = "Env"
    }

    @Incremental
    @InputFile
    lateinit var configFile: File

    @OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun execute(inputChanges: InputChanges) {
        println(if (inputChanges.isIncremental) "Executing incrementally" else "Executing non-incrementally")
        generateFile(configFile.readLines())
    }

    private fun generateFile(lines: List<String>) {
        val envFile =
            FileSpec.builder(PACKAGE, CLASS)
                .addType(createEnvClass(lines))
                .build()
        envFile.writeTo(outputDir)
    }

    private fun createEnvClass(lines: List<String>) =
        TypeSpec.classBuilder(CLASS)
            .addType(
                createCompanionObject(lines)
            ).build()

    private fun createCompanionObject(lines: List<String>): TypeSpec {
        val typeSpec = TypeSpec.companionObjectBuilder()
        for (line in lines) {
            typeSpec.addProperty(createProperties(line))
        }
        return typeSpec.build()
    }

    private fun createProperties(line: String): PropertySpec {
        if (!line.contains('=')) error("file is not in correct format")
        val values = line.split("=")
        return PropertySpec.builder(values[0], String::class)
            .initializer("%S", values[1]).build()
    }

}