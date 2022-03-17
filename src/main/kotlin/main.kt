import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("PCDF File Converter")
    println("Hi, I'm the PCDF File Converter!")
    // Parse CLI arguments
    val inputPath by parser.option(
        ArgType.String,
        shortName = "i",
        description = "Relative path to input file"
    ).required()
    val outputPath by parser.option(
        ArgType.String,
        shortName = "o",
        description = "Relative path to output file"
    )
    val conversion by parser.option(
        ArgType.Choice<Conversion>(),
        shortName = "c",
        description = "Type of conversion, at the moment only persistent to intermediate available"
    ).default(Conversion.P2I)
    val print by parser.option(
        ArgType.Boolean,
        shortName = "p",
        description = "Print resulting events to the command line."
    ).default(false)
    parser.parse(args)

    val inputFile = File(inputPath)
    val outputFile = File(outputPath)

    if (outputFile.exists()) outputFile.delete()

    when (conversion) {

        Conversion.P2I -> {
            try {
                PCDFConverter.pToIFile(inputFile, outputFile)
            } catch (e: Exception) {
                println("Something went wrong: ")
                println(e)
                println("\nMake sure that the input file exists, is readable and that the output location is writable. Also make sure that the paths are *relative* to the location of the working directory of your console.")
            }
        }
    }
}

/**
 * Possible conversions, atm only persistent to intermediate.
 */
enum class Conversion {
    P2I
}