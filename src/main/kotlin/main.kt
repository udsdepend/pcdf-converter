import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = ArgParser("PCDF File Converter")

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
    val printConversionResult by parser.option(
        ArgType.Boolean,
        shortName = "p",
        description = "Print resulting events to the command line/stdout."
    ).default(false)
    val onlyMachineReadableConsoleOutput by parser.option(
        ArgType.Boolean,
        shortName = "m",
        description = "Suppress console outputs that are not machine readable."
    ).default(false)
    parser.parse(args)

    if (!onlyMachineReadableConsoleOutput)
        println("Hi, I'm the PCDF File Converter!")

    if (outputPath == null && !printConversionResult){
        println("ERROR: Print option must be set or output file given.")
        exitProcess(-1)
    }
    val inputFile = File(inputPath)
    val outputFile = if (outputPath != null) File(outputPath!!)  else null

    if (outputFile?.exists() == true) outputFile.delete()

    when (conversion) {

        Conversion.P2I -> {
            try {
                if (!onlyMachineReadableConsoleOutput)
                    println("Input file type: " + SimAnalyser.isSimulator(inputFile))
                PCDFConverter.pToIFile(inputFile, outputFile, printConversionResult)
                if (!onlyMachineReadableConsoleOutput)
                    println("Conversion successful!")
            } catch (e: Exception) {
                if (!onlyMachineReadableConsoleOutput) {
                    println("Something went wrong: ")
                    println(e)
                    println("\nMake sure that the input file exists, is readable and that the output location is writable. Also make sure that the paths are *relative* to the location of the working directory of your console.")
                } else {
                    println("ERROR: ${e.message}")
                    exitProcess(-1)
                }

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