import pcdfEvent.PCDFEvent
import java.io.File

class PCDFConverter {
    companion object {
        /**
         * Converts a given persistent PCDF file into an intermediate PCDF file
         */
        fun pToIFile(pFile: File, iFile: File?, p: Boolean) {
            pFile.forEachLine {
                val iString = PCDFEvent.fromString(it).toIntermediate().serialize()
                iFile?.appendText(iString + "\n")
                if (p) println(iString)
            }
        }
    }
}