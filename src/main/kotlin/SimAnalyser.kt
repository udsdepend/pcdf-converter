import SimAnalyser.Companion.SimResult.*
import pcdfEvent.PCDFEvent
import pcdfEvent.events.GPSEvent
import pcdfEvent.events.obdEvents.obdIntermediateEvents.singleComponentEvents.SpeedEvent
import java.io.File

class SimAnalyser {
    companion object {
        fun isSimulator(pFile: File?) : SimResult {
            var gpsSpeed = 0.0
            var gpsCount = 0
            var obdSpeed = 0.0
            var obdCount = 0
            pFile?.forEachLine {
                when (val event = PCDFEvent.fromString(it).toIntermediate()) {
                    is SpeedEvent -> {
                        obdSpeed += event.speed
                        obdCount += 1
                    }
                    is GPSEvent -> {
                        gpsSpeed += event.speed?.times(3.6) ?: 0.0
                        gpsCount += 1
                    }
                }
            }

            if (gpsCount == 0 || obdCount == 0 || obdSpeed == 0.0) return MONITORING

            val avgGPSSpeed = gpsSpeed / gpsCount
            val avgOBDSpeed = obdSpeed / obdCount
            return if (avgGPSSpeed / avgOBDSpeed < 0.8) {
                SIMULATOR
            } else {
                RDE
            }
        }
        enum class SimResult {
            SIMULATOR,
            RDE,
            MONITORING
        }
    }
}