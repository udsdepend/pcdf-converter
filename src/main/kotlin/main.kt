import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import de.unisaarland.pcdfconverter.RTLolaAnalyser

fun main(args: Array<String>) {
    val p = PCDFConverter()
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
    val rdeanalysis by parser.option(
        ArgType.String,
        shortName= "rde",
        description = "Whether an RDE analysis should be attempted."
    )
    val conversion by parser.option(
        ArgType.Choice<Conversion>(),
        shortName = "c",
        description = "Type of conversion, at the moment only persistent to intermediate available"
    )
    val print by parser.option(
        ArgType.Boolean,
        shortName = "p",
        description = "Print resulting events to the command line."
    ).default(false)
    parser.parse(args)

    if (outputPath == null && !print && rdeanalysis == null){
        println("Print option must be set or output file given.")
        return
    }
    val inputFile = File(inputPath)
    val outputFile = if (outputPath != null) File(outputPath!!)  else null

    if (outputFile?.exists() == true) outputFile.delete()

    if (conversion != null) {
        when (conversion) {
            Conversion.P2I -> {
                try {
                    println("Given file was generated with: " + SimAnalyser.isSimulator(inputFile))
                    PCDFConverter.pToIFile(inputFile, outputFile, print)
                } catch (e: Exception) {
                    println("Something went wrong: ")
                    println(e)
                    println("\nMake sure that the input file exists, is readable and that the output location is writable. Also make sure that the paths are *relative* to the location of the working directory of your console.")
                }
            }
            else -> {

            }
        }
    }

    if (rdeanalysis != null) {
        if (outputFile != null) {
            val analyser = RTLolaAnalyser()
            analyser.monitorFile(inputFile, rdeanalysis!!, outputFile)
        } else {
            println("No Output-File given")
        }
    }
}

/**
 * Possible conversions, atm only persistent to intermediate.
 */
enum class Conversion {
    P2I
}