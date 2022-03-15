import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("PCDF File Converter")
    print("Hello World")
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
    ).required()
    val conversion by parser.option(
        ArgType.Choice<Conversion>(),
        shortName = "c",
        description = "Type of conversion, at the moment only persistent to intermediate available"
    ).default(Conversion.P2I)
    parser.parse(args)

    val inputFile = File(inputPath)
    val outputFile = File(outputPath)

    if (outputFile.exists()) outputFile.delete()

    when (conversion) {
        Conversion.P2I -> {
            PCDFConverter.pToIFile(inputFile, outputFile)
        }
    }
}

/**
 * Possible conversions, atm only persistent to intermediate.
 */
enum class Conversion {
    P2I
}