package nkcx;
/**
 * Created by davbzh on 2017-02-08.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.BufferedReader;
import java.io.IOException;


public class CaseControlMatching {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaseControlMatching.class);
    //\\//:
    public static long getDifferenceDays(String string_d1, String string_d2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        Date endDate;
        long diff = 0;
        try {
            startDate = df.parse(string_d1);
            endDate = df.parse(string_d2);
            diff = endDate.getTime() - startDate.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    public static void process(String input_case_Path, String input_ctrl_Path, String outputPath) {
        Path case_Path = Paths.get(input_case_Path);
        Path ctrl_Path = Paths.get(input_ctrl_Path);
        Path output = Paths.get(outputPath);
        processLine(case_Path, ctrl_Path, output);
    }

    private static void processLine(Path input_case_Path, Path input_ctrl_Path, Path outputPath) {
        try (BufferedReader case_reader = Files.newBufferedReader(input_case_Path);
             BufferedReader ctrl_reader = Files.newBufferedReader(input_ctrl_Path);
            BufferedWriter successWriter = Files.newBufferedWriter(outputPath)) {
            String case_line;
            String ctrl_line;

            LOGGER.info("Processing file {}", input_case_Path.getFileName().toString());
            while ((case_line = case_reader.readLine()) != null) {
                String[] case_vals = case_line.toString().split(",");
                String case_pnr = case_vals[0];
                int case_age = Integer.parseInt(case_vals[1]);
                String case_sampleDate = case_vals[2];
                String case_snomed = case_vals[3];
                boolean found_matches_ctrl = false;
                while ((ctrl_line = ctrl_reader.readLine()) != null && !found_matches_ctrl) {
                    String[] ctrl_vals = ctrl_line.toString().split(",");
                    String ctrl_pnr = ctrl_vals[0];
                    int ctrl_age = Integer.parseInt(ctrl_vals[1]);
                    String ctrl_sampleDate = ctrl_vals[2];
                    String ctrl_snomed = ctrl_vals[3];

                    if (Math.abs(getDifferenceDays(case_sampleDate, ctrl_sampleDate)) <= 60 &&
                            Math.abs(case_age - ctrl_age) <= 1) {
                        found_matches_ctrl = true;
                    }

                    if (found_matches_ctrl) {
                        //break;
                        String result = "";
                        result += case_pnr + "\t";
                        result += ctrl_pnr;
                        System.out.println(result);
                        successWriter.write( result + "\n");
                        continue;
                    }

                }
            }

        } catch (IOException e) {
            LOGGER.error("Error while processing file {}", input_case_Path.getFileName().toString());
            LOGGER.info("Exception. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println( getDifferenceDays("2016-07-01",  "2016-05-01") );
    }

}
