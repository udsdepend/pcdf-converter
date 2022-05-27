import de.unisaarland.pcdfanalyser.eventStream.FileEventStream
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import de.unisaarland.pcdfconverter.RTLolaAnalyser
import pcdfEvent.events.obdEvents.obdIntermediateEvents.OBDIntermediateEvent
import pcdfEvent.events.obdEvents.obdIntermediateEvents.multiComponentEvents.NOXSensorAlternativeEvent
import pcdfEvent.events.obdEvents.obdIntermediateEvents.multiComponentEvents.NOXSensorCorrectedAlternativeEvent
import pcdfEvent.events.obdEvents.obdIntermediateEvents.multiComponentEvents.NOXSensorCorrectedEvent
import pcdfEvent.events.obdEvents.obdIntermediateEvents.multiComponentEvents.NOXSensorEvent
import pcdfUtilities.StreamFilter

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
            val eventStream = FileEventStream(inputFile)
            val invalidNOxValue = 65535
            val filteredStream = StreamFilter(eventStream) {
                !(it is NOXSensorEvent && (it.sensor1_1 == invalidNOxValue || it.sensor1_2 == invalidNOxValue || it.sensor2_1 == invalidNOxValue || it.sensor2_2 == invalidNOxValue) ||
                it is NOXSensorAlternativeEvent && (it.sensor1_1 == invalidNOxValue || it.sensor1_2 == invalidNOxValue || it.sensor2_1 == invalidNOxValue || it.sensor2_2 == invalidNOxValue) ||
                it is NOXSensorCorrectedEvent  && (it.sensor1_1 == invalidNOxValue || it.sensor1_2 == invalidNOxValue || it.sensor2_1 == invalidNOxValue || it.sensor2_2 == invalidNOxValue) ||
                it is NOXSensorCorrectedAlternativeEvent && (it.sensor1_1 == invalidNOxValue || it.sensor1_2 == invalidNOxValue || it.sensor2_1 == invalidNOxValue || it.sensor2_2 == invalidNOxValue))
            }
            analyser.monitorEventStream(filteredStream, rdeanalysis!!, outputFile)
            //analyser.monitorFile(inputFile, rdeanalysis!!, outputFile)
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