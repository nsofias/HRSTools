package tools;

import com.sun.tools.javac.util.StringUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author nsofias
 */
public class FileComparator {

    /*
    base: 
        Ημερ. Ενεργ.	Νούμερο	Κωδ.Status Συν.	Status Συνδεσης	Ημερ. Έναρξης	Μητρώο Split Biller	Μητρώο VOD
    ELRA_POST_1-9-2023:
        Tariff Group	MSISDN	SUBSCRIBER_ID	TARIFF_PLAN	ACTIVATION_DATE	GPRS_SERVICE_LEVEL	LAST_RENEWAL_DATE	PRIMARY_MSISDN
    SB_HRS_September_2023:
        Μητρώο	MSISDN	Πρόγραμμα Χρήσης	Connection Date	Agreement Date	Contract Duration        
     */
    public static List<String> getInvalidRows(String base, int base_ndex) throws IOException {
        Predicate<String> invalidRow = s -> s.split(";").length < 3 || !isNumeric(s.split(";")[base_ndex]);
        List<String> bad_base_Lines = Files.readAllLines(Paths.get(base), StandardCharsets.UTF_8)
                .stream().filter(invalidRow)
                // .peek(s->System.out.println("invalid ---->"+s))
                .collect(toList());
        System.out.println("HRSTools:bad_baseOnly base_Lines ok: found " + bad_base_Lines.size());
        return bad_base_Lines;
    }

    public static Map<String, List<String>> baseOnly(String base, int base_ndex, String ELRA_POST, int ELRA_POST_index, String SB_HRS, int SB_HRS_index) throws IOException {
        Predicate<String> validRow = s -> s.split(";").length >= 3 && isNumeric(s.split(";")[base_ndex]);
        Map<String, List<String>> base_Lines = Files.readAllLines(Paths.get(base), StandardCharsets.UTF_8)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[base_ndex]));
        System.out.println("HRSTools:baseOnly base_Lines ok: found " + base_Lines.size());
        //---
        Map<String, List<String>> ELRA_POST_Lines = Files.readAllLines(Paths.get(ELRA_POST), StandardCharsets.ISO_8859_1)
                .stream().filter(s -> s.split(";").length > 1).collect(Collectors.groupingBy(l -> l.split(";")[ELRA_POST_index]));
        System.out.println("HRSTools:ELRA_POST_Lines ok: found " + ELRA_POST_Lines.size());
        //---
        Map<String, List<String>> SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS), StandardCharsets.ISO_8859_1)
                .stream().filter(s -> s.split(";").length > 1).collect(Collectors.groupingBy(l -> l.split(";")[SB_HRS_index]));
        System.out.println("HRSTools:SB_HRS_Lines ok: found " + SB_HRS_Lines.size());
        return base_Lines.entrySet().stream()
                .filter(e -> !ELRA_POST_Lines.containsKey(e.getKey()) && !SB_HRS_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        try {
            double d = Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String base = "C:\\myFiles\\data\\HRSTools\\data\\base.csv";
        String vod_1 = "C:\\myFiles\\data\\HRSTools\\data\\ELRA_POST_1-9-2023.csv";
        String vod_2 = "C:\\myFiles\\data\\HRSTools\\data\\SB_HRS_September_2023.csv";
        try {
            List<String> myInvalidRows = getInvalidRows(base, 1);
            Map<String, List<String>> hrsOnly = FileComparator.baseOnly(base, 1, vod_1, 1, vod_2, 1);

            //---
            System.out.println();
            System.out.println("*************** SUMMARY ***********");
            System.out.println("INVALID Numbers found: "+myInvalidRows.size());
            System.out.println("Numbers that exist ONLY in HRS (not in Vodafone), found: "+myInvalidRows.size());
            System.out.println("Reasons for  Numbers that exist ONLY in HRS database (not in Vodafone):");
            Map<String, Long> res1 = hrsOnly.values().stream().flatMap(v -> v.stream()).filter(s -> s.split(";").length > 3).collect(Collectors.groupingBy(s -> s.split(";")[2] + ":" + s.split(";")[3], Collectors.counting()));
            res1.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                System.out.println("        "+ e.getValue()+" numbers found for reason " + e.getKey());
            });
            System.out.println();
            System.out.println("***************************************************************");
            System.out.println("\n\n\n*** DETAILS ***");
            System.out.println("*** Invalid Numbers ***");
            myInvalidRows.stream().filter(s -> !s.contains(";;;"))
                    .forEach(s -> System.out.println("invalid number: " + s));
            System.out.println("\n\n*** Numbers that exist ONLY in HRS (not found in Vodafone) ***");
            hrsOnly.entrySet().forEach(e -> {
                System.out.println("ONLY in HRS: " + e.getKey() + " : " + e.getValue());
            });

        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
