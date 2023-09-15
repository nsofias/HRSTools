package tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
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

    Charset charset = StandardCharsets.ISO_8859_1;
    String base;
    int base_ndex;
    String ELRA_POST;
    int ELRA_POST_index;
    String SB_HRS;
    int SB_HRS_index;
    Predicate<String> validRow = s -> s.split(";").length >= 3 && isNumeric(s.split(";")[base_ndex]);
    Predicate<String> invalidRow = s -> s.split(";").length < 3 || !isNumeric(s.split(";")[base_ndex]);

    public FileComparator(String base, int base_ndex, String ELRA_POST, int ELRA_POST_index, String SB_HRS, int SB_HRS_index, Charset charset) {
        this.base = base;
        this.base_ndex = base_ndex;
        this.ELRA_POST = ELRA_POST;
        this.ELRA_POST_index = ELRA_POST_index;
        this.SB_HRS = SB_HRS;
        this.SB_HRS_index = SB_HRS_index;
        this.charset = charset;
    }

    /*
    base: 
        Ημερ. Ενεργ.	Νούμερο	Κωδ.Status Συν.	Status Συνδεσης	Ημερ. Έναρξης	Μητρώο Split Biller	Μητρώο VOD
    ELRA_POST_1-9-2023:
        Tariff Group	MSISDN	SUBSCRIBER_ID	TARIFF_PLAN	ACTIVATION_DATE	GPRS_SERVICE_LEVEL	LAST_RENEWAL_DATE	PRIMARY_MSISDN
    SB_HRS_September_2023:
        Μητρώο	MSISDN	Πρόγραμμα Χρήσης	Connection Date	Agreement Date	Contract Duration        
     */
    public List<String> getInvalidBaseRows() throws IOException {
        List<String> bad_base_Lines = Files.readAllLines(Paths.get(base), charset)
                .stream().filter(invalidRow)
                // .peek(s->System.out.println("invalid ---->"+s))
                .collect(toList());
        System.out.println("HRSTools:bad_baseOnly base_Lines ok: found " + bad_base_Lines.size());
        return bad_base_Lines;
    }

    public Map<String, List<String>> baseOnly() throws IOException {
        Map<String, List<String>> base_Lines = Files.readAllLines(Paths.get(base), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[base_ndex]));
        System.out.println("HRSTools:baseOnly base_Lines ok: found " + base_Lines.size());
        //---
        Map<String, List<String>> ELRA_POST_Lines = Files.readAllLines(Paths.get(ELRA_POST), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[ELRA_POST_index]));
        System.out.println("HRSTools:ELRA_POST_Lines ok: found " + ELRA_POST_Lines.size());
        //---
        Map<String, List<String>> SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[SB_HRS_index]));
        System.out.println("HRSTools:SB_HRS_Lines ok: found " + SB_HRS_Lines.size());
        //---
        return base_Lines.entrySet().stream()
                .filter(e -> !ELRA_POST_Lines.containsKey(e.getKey()) && !SB_HRS_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<String>> ELRA_POST_only() throws IOException {
        Predicate<String> validRow = s -> s.split(";").length >= 3 && isNumeric(s.split(";")[base_ndex]);
        Map<String, List<String>> base_Lines = Files.readAllLines(Paths.get(base), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[base_ndex]));
        System.out.println("HRSTools:baseOnly base_Lines ok: found " + base_Lines.size());
        //---
        Map<String, List<String>> ELRA_POST_Lines = Files.readAllLines(Paths.get(ELRA_POST), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[ELRA_POST_index]));
        System.out.println("HRSTools:ELRA_POST_Lines ok: found " + ELRA_POST_Lines.size());
        //---
        return ELRA_POST_Lines.entrySet().stream()
                .filter(e -> !base_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<String>> ESB_HRS_only() throws IOException {
        Predicate<String> validRow = s -> s.split(";").length >= 3 && isNumeric(s.split(";")[base_ndex]);
        Map<String, List<String>> base_Lines = Files.readAllLines(Paths.get(base), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[base_ndex]));
        System.out.println("HRSTools:baseOnly base_Lines ok: found " + base_Lines.size());
        //---
        Map<String, List<String>> SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS), charset)
                .stream().filter(validRow).collect(Collectors.groupingBy(l -> l.split(";")[SB_HRS_index]));
        System.out.println("HRSTools:ESB_HRS_Lines ok: found " + SB_HRS_Lines.size());
        //---
        return SB_HRS_Lines.entrySet().stream()
                .filter(e -> !base_Lines.containsKey(e.getKey()))
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
        String base = "C:\\myFiles\\data\\access7\\HRSTools\\data\\base.csv";
        String vod_1 = "C:\\myFiles\\data\\access7\\HRSTools\\data\\ELRA_POST_1-9-2023.csv";
        String vod_2 = "C:\\myFiles\\data\\access7\\HRSTools\\data\\SB_HRS_September_2023.csv";
        FileComparator myFileComparator = new FileComparator(base, 1, vod_1, 1, vod_2, 1, StandardCharsets.UTF_8);
        try {

            List<String> myInvalidRows = myFileComparator.getInvalidBaseRows();
            Map<String, List<String>> hrsOnly = myFileComparator.baseOnly();
            Map<String, List<String>> VOD_ELRA_POST_only = myFileComparator.ELRA_POST_only();
            Map<String, List<String>> VOD_ESB_HRS_only = myFileComparator.ESB_HRS_only();
            //---
            System.out.println();
            System.out.println("*************** SUMMARY ***********");
            System.out.println("INVALID Numbers found: " + myInvalidRows.size());
            System.out.println("Numbers that exist ONLY in HRS (not in Vodafone), found: " + myInvalidRows.size());
            System.out.println("Reasons for  Numbers that exist ONLY in HRS database (not in Vodafone):");
            Map<String, Long> res1 = hrsOnly.values().stream().flatMap(v -> v.stream()).collect(Collectors.groupingBy(s -> s.split(";")[2] + ":" + s.split(";")[3], Collectors.counting()));
            res1.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                System.out.println("        " + e.getValue() + " numbers found for reason " + e.getKey());
            });
            System.out.println("Numbers that exist ONLY in VOD_ELRA_POST (not in Atlantis), found: " + VOD_ELRA_POST_only.size());            
            System.out.println("Numbers that exist ONLY in VOD_ESB_HRS (not in Atlanis), found: " + VOD_ESB_HRS_only.size());
            //-------------------------------------------------
            System.out.println();
            System.out.println("***************************************************************");
            System.out.println("\n\n\n*** DETAILS ***");
            //-------------
            System.out.println("*** Invalid Numbers ***");
            myInvalidRows.stream().filter(s -> !s.contains(";;;"))
                    .forEach(s -> System.out.println("  invalid number: " + s));
            //---------
            System.out.println("\n\n*** Numbers that exist ONLY in HRS (not found in Vodafone) ***");
            hrsOnly.values().stream().flatMap(v -> v.stream()).sorted(Comparator.comparing(s -> s.split(";")[2])).forEach(s -> {
                System.out.println("  ONLY in HRS: " + s.split(";")[1] + " : " + s);
            });
            //----------
            System.out.println("\n\n*** Numbers that exist ONLY in VOD_ELRA_POST (not in Atlantis): " + VOD_ELRA_POST_only.size());
            VOD_ELRA_POST_only.values().stream().flatMap(v -> v.stream()).forEach(s -> System.out.println(" VOD_ELRA_POST_only-> "+s.split(";")[1]+ " : " + s));
            System.out.println("\n\n*** Numbers that exist ONLY in VOD_ESB_HRS (not in Atlanis): " + VOD_ESB_HRS_only.size());
            VOD_ESB_HRS_only.values().stream().flatMap(v -> v.stream()).forEach(s -> System.out.println(" VOD_ESB_HRS_only-> "+s.split(";")[1] +" :"+  s));
        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
