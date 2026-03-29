package utcb.fii;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utcb.fii.gui.GraphicManager;
import utcb.fii.io.FileManager;
import utcb.fii.model.Measurement;

import java.nio.file.Path;
import java.util.List;

public class Laboratoire6 {
    private static final Logger logger = LoggerFactory.getLogger(Laboratoire6.class);

    public static void main(String[] args) {
        Path resourcesPath = Path.of("Laboratoire6","src", "main", "resources");
        String csvFilePath = resourcesPath.resolve("raw_data.csv").toString();
        String xlsxFilePath = resourcesPath.resolve("processed_results.xlsx").toString();
        String pngFilePath = resourcesPath.resolve("graphic_thermo.png").toString();
        logger.info("Start Thermotechnique Analysis System...");
        FileManager fileManager = new FileManager();
        GraphicManager graphicManager = new GraphicManager();
        logger.info("Reading CSV file from {}...", csvFilePath);
        List<Measurement> measurementsList = fileManager.readFromFile(csvFilePath);
        if (measurementsList.isEmpty()) {
            logger.error("CSV file is empty or no valid row was extracted. For preventing mathematical errors, the program will be terminated.");
            return;
        }
        logger.info("Exporting processed data to Excel: {}", xlsxFilePath);
        fileManager.writeToFile(xlsxFilePath, measurementsList);
        logger.info("Generating graphical analysis...");
        Measurement first = measurementsList.getFirst();
        Measurement last = measurementsList.getLast();
        double deltaLogX = last.getDecimalLogX() - first.getDecimalLogX();
        double deltaLogR = last.getDecimalLogR() - first.getDecimalLogR();
        double slopeA = deltaLogX / deltaLogR;
        logger.info("Calculated slope (a) based on formula Δx/Δy: {}", String.format("%.5f", slopeA));
        logger.info("Starting graphical analysis. Saving to: {}", pngFilePath);
        graphicManager.generateGraphic(measurementsList, slopeA, pngFilePath);
        logger.info("Analysis complete. Check resources folder for results.");
    }
}