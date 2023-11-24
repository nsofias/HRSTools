package tools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import model.CustomerEvent;

/**
 *
 * @author nsofias
 */
public class FileComparator {

    Map<String, String> statuses = new HashMap();
    Charset charset = StandardCharsets.UTF_8;
    String splitter = "\t";
    String max_activationDate = "20230930T000000";
    //
    String HRS_BASE_filename = "C:\\myfiles\\data\\HRSTools\\data\\October_2023\\HRS_BASE.txt";
    int HRS_BASE_index = 1;
    int HRS_BASE_secondMSISDN_index = 4;
    int HRS_BASE_activationDate_index = 10;
    int HRS_BASE_status_index = 9;
    int O000xxx_index = 5;
    List ignoreList = Arrays.asList("13", "6", "69", "72", "23", "75", "57", "4", "33", "24", "20", "19", "76");
    //
    String ELRA_POST_filename = "C:\\myfiles\\data\\HRSTools\\data\\October_2023\\VODAFONE ELRA_MOB.txt";
    int ELRA_POST_index = 1;
    int ELRA_POST_activationDate_index = 4;
    int ELRA_POST_status_index = 5;
    //
    String SB_HRS_filename = "C:\\myfiles\\data\\HRSTools\\data\\October_2023\\VODAFONE SB_HRS.txt";
    int SB_HRS_index = 1;
    int SB_HRS_activationDate_index = 3;
    //
    Predicate<CustomerEvent> validRow = s -> isNumeric(s.getMSISDN()) && !s.getActivationDate().isEmpty();
    Predicate<CustomerEvent> invalidRow = s -> !(isNumeric(s.getMSISDN()) && !s.getActivationDate().isEmpty());
    Predicate<CustomerEvent> activationDateFilter = s -> s.getActivationDate().compareTo(max_activationDate) < 0;
    private Map<String, List<CustomerEvent>> base_Lines;
    private Map<String, List<CustomerEvent>> ELRA_POST_Lines;
    private Map<String, List<CustomerEvent>> SB_HRS_Lines;

    public FileComparator() {
        this.charset = StandardCharsets.UTF_8;
        try {
            List<String> lines = Files.readAllLines(Paths.get("C:\\myfiles\\data\\HRSTools\\statuses.txt"), StandardCharsets.UTF_8);
            lines.forEach(l -> statuses.put(l.split(";")[0], l.split(";")[1]));
            System.out.println(statuses);
        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadFiles() throws IOException {
        base_Lines = Files.readAllLines(Paths.get(HRS_BASE_filename), charset)
                .stream()
                .map(s -> {
                    //System.out.println("s.split(splitter).length="+s.split(splitter).length);
                    String msisdn = s.split(splitter).length > HRS_BASE_index ? s.split(splitter)[HRS_BASE_index] : "";
                    String HRS_BASE_secondMSISDN = s.split(splitter).length > HRS_BASE_secondMSISDN_index ? s.split(splitter)[HRS_BASE_secondMSISDN_index] : "";
                    String O000xxx = s.split(splitter).length > O000xxx_index ? s.split(splitter)[O000xxx_index] : "";
                    String activationDate = s.split(splitter).length > HRS_BASE_activationDate_index ? s.split(splitter)[HRS_BASE_activationDate_index] : "";
                    String status = s.split(splitter).length > HRS_BASE_status_index ? s.split(splitter)[HRS_BASE_status_index] : "";
                    return new CustomerEvent(msisdn, HRS_BASE_secondMSISDN, O000xxx, formatDate(activationDate), status, s);
                })
                .filter(validRow).filter(e -> !ignoreList.contains(e.getStatus())).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        //---
        ELRA_POST_Lines = Files.readAllLines(Paths.get(ELRA_POST_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > ELRA_POST_index ? s.split(splitter)[ELRA_POST_index] : "";
                    String activationDate = s.split(splitter).length > ELRA_POST_activationDate_index ? s.split(splitter)[ELRA_POST_activationDate_index] : "";
                    String status = s.split(splitter).length > ELRA_POST_status_index ? s.split(splitter)[ELRA_POST_status_index] : "";
                    return new CustomerEvent(msisdn, "", "", formatDate(activationDate), status, s);
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        //---
        SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > SB_HRS_index ? s.split(splitter)[SB_HRS_index] : "";
                    String activationDate = s.split(splitter).length > SB_HRS_activationDate_index ? s.split(splitter)[SB_HRS_activationDate_index] : "";
                    //String status = s.split(splitter).length > SB_HRS_status_index ? s.split(splitter)[SB_HRS_status_index] : "";
                    return new CustomerEvent(msisdn, "", "", formatDate(activationDate), "", s);
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
    }


    /*
    HRS_BASE_filename: 
        Ημερ. Ενεργ.	Νούμερο	Κωδ.Status Συν.	Status Συνδεσης	Ημερ. Έναρξης	Μητρώο Split Biller	Μητρώο VOD
    ELRA_POST_1-9-2023:
        Tariff Group	MSISDN	SUBSCRIBER_ID	TARIFF_PLAN	ACTIVATION_DATE	GPRS_SERVICE_LEVEL	LAST_RENEWAL_DATE	PRIMARY_MSISDN
    SB_HRS_September_2023:
        Μητρώο	MSISDN	Πρόγραμμα Χρήσης	Connection Date	Agreement Date	Contract Duration        
     */
    public Set<CustomerEvent> getInvalidBaseRows() throws IOException {
        return base_Lines.values()
                .stream()
                .flatMap(l -> l.stream())
                .filter(invalidRow)
                // .peek(s->System.out.println("invalid ---->"+s))
                .collect(toSet());
    }

    public Set<CustomerEvent> baseOnly() throws IOException {
        return getBase_Lines().values().stream()
                .flatMap(l -> l.stream())
                .filter(activationDateFilter)
                .filter(e -> !ELRA_POST_Lines.containsKey(e.getMSISDN()) && !SB_HRS_Lines.containsKey(e.getMSISDN())
                && !ELRA_POST_Lines.containsKey(e.getO000xxx()) && !SB_HRS_Lines.containsKey(e.getO000xxx()))
                .collect(toSet());
    }

    public Set<CustomerEvent> ELRA_POST_only() throws IOException {
        List<String> o0List = getBase_Lines().values().stream()
                .flatMap(l -> l.stream()).map(v -> v.getO000xxx()).collect(toList());
        List<String> secondMSISDNList = getBase_Lines().values().stream()
                .flatMap(l -> l.stream()).map(v -> v.getSecondMSISDN()).collect(toList());
        return getELRA_POST_Lines().values().stream()
                .flatMap(l -> l.stream())
                .filter(activationDateFilter)
                .filter(e -> !base_Lines.containsKey(e.getMSISDN()) && !o0List.contains(e.getMSISDN())&& !secondMSISDNList.contains(e.getMSISDN()))
                .collect(toSet());
    }

    public List<CustomerEvent> ESB_HRS_only() throws IOException {
        List<String> secondMSISDNList = getBase_Lines().values().stream()
                .flatMap(l -> l.stream()).map(v -> v.getSecondMSISDN()).collect(toList());
        return getSB_HRS_Lines().values().stream()
                .flatMap(l -> l.stream())
                .filter(activationDateFilter)
                .filter(e -> !base_Lines.containsKey(e.getMSISDN())&& !secondMSISDNList.contains(e.getMSISDN()))
                .collect(toList());
    }

    /**
     * @return the base_Lines
     */
    public Map<String, List<CustomerEvent>> getBase_Lines() {
        return base_Lines;
    }

    /**
     * @return the ELRA_POST_Lines
     */
    public Map<String, List<CustomerEvent>> getELRA_POST_Lines() {
        return ELRA_POST_Lines;
    }

    /**
     * @return the SB_HRS_Lines
     */
    public Map<String, List<CustomerEvent>> getSB_HRS_Lines() {
        return SB_HRS_Lines;
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

    public void report() throws IOException {
        Set<CustomerEvent> myInvalidRows = getInvalidBaseRows();
        Set<CustomerEvent> hrsOnly = baseOnly();
        Set<CustomerEvent> VOD_ELRA_POST_only = ELRA_POST_only();
        List<CustomerEvent> VOD_ESB_HRS_only = ESB_HRS_only();
        //---
        System.out.println();
        System.out.println("*************** SUMMARY ***********");
        System.out.println("\nATLANTIS Numbers found: " + getBase_Lines().size());
        System.out.println("\nELRA_POST Numbers found: " + getELRA_POST_Lines().size());
        System.out.println("\nSB_HRS Numbers found: " + getSB_HRS_Lines().size());
        //---
        System.out.println("\nINVALID ATLANTIS rows found: " + myInvalidRows.size());
        System.out.println("\nNumbers that exist ONLY in ATLANTIS (not in Vodafone), found: " + hrsOnly.size());
        System.out.println("\nReasons for  Numbers that exist ONLY in ATLANTIS database (not in Vodafone): - ignoreList:" + ignoreList);
        Map<String, Long> atlantis_statuses = hrsOnly.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey() + " - " + statuses.get(e.getKey()));
        });
        System.out.println("\nReasons for Numbers that exist ONLY in VOD_ELRA_POST (not in ATLANTIS), found: " + VOD_ELRA_POST_only.size());
        Map<String, Long> VOD_ELRA_statuses = VOD_ELRA_POST_only.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        VOD_ELRA_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey());
        });
        System.out.println("\nReasons for Numbers that exist ONLY in VOD_ESB_HRS (not in ATLANTIS), found: " + VOD_ESB_HRS_only.size());
        Map<String, Long> VOD_ESB_HRS_statuses = VOD_ESB_HRS_only.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        VOD_ESB_HRS_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey());
        });
        System.out.println();
        System.out.println("\n\n\n\n***************************************************************");
        System.out.println("*** DETAILS ***");
        System.out.println("***************************************************************");
        //-------------
        System.out.println("*** Invalid ATLANTIS rows details ***");
        myInvalidRows.stream().filter(s -> !s.getInfo().contains(";;;"))
                .forEach(s -> System.out.println("  invalid number: " + s));
        //---------
        System.out.println("\n\n*** Numbers that exist ONLY in ATLANTIS (not found in Vodafone), details ***");
        hrsOnly.stream().sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> {
            System.out.println("  ONLY in HRS: " + s.getMSISDN() + " : " + s);
        });
        //----------
        System.out.println("\n\n*** Numbers that exist ONLY in VOD_ELRA_POST (not in ATLANTIS): " + VOD_ELRA_POST_only.size());
        VOD_ELRA_POST_only.stream().sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> System.out.println(" VOD_ELRA_POST_only-> " + s.getMSISDN() + " : " + s));
        System.out.println("\n\n*** Numbers that exist ONLY in VOD_ESB_HRS (not in Atlanis): " + VOD_ESB_HRS_only.size());
        VOD_ESB_HRS_only.stream().forEach(s -> System.out.println(" VOD_ESB_HRS_only-> " + s.getMSISDN() + " :" + s));
    }

    private static String formatDate(String activationDate) {
        //9/5/2014
        try {
            activationDate = activationDate.split(" ")[0];
            String[] parts = activationDate.split("/");
            String year = parts[2];
            String month = parts[1];
            if (month.length() == 1) {
                month = "0" + month;
            }
            String day = parts[0];
            if (day.length() == 1) {
                day = "0" + day;
            }
            return year + month + day + "T000000.000";
        } catch (Exception e) {
            System.out.println("activationDate=" + activationDate);
            return activationDate;
        }
    }

    public static void main(String[] args) {
        System.out.println(formatDate("9/5/2014"));

        FileComparator myFileComparator = new FileComparator();
        try {
            myFileComparator.loadFiles();
            myFileComparator.report();
        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


/*
        9170 found. Reason:13(Μεταβ.στη Vodafone)
        6805 found. Reason:6(Οριστική Διακοπή)
        3945 found. Reason:69(ΑΠΟΣΥΝΔΕΣΗ DATA)
        2932 found. Reason:501(ΦΟΡΗΤΟΤΗΤΑ ΣΤΑΘΕΡΗΣ)
        2727 found. Reason:72(PORT OUT KINHTHΣ )
        1637 found. Reason:23(ΚΑΡΤΟΚΙΝΗΤΟ)
        1631 found. Reason:502(Νεα συνδεση σταθερο)
        437 found. Reason:75(PORT OUT ΣΤΑΘΕΡΗΣ )
        359 found. Reason:37(Νέα σύνδεση-Αλλαγή Στοιχ.)
        336 found. Reason:57(Ακύρωση Συμβολαίου)
        247 found. Reason:4(Διακοπή)
        190 found. Reason:33(προς Δικαστήρια-οριστ.δια)
        114 found. Reason:10(ΦΟΡΗΤΟΤΗΤΑ ΚΙΝΗΤΗΣ)
        113 found. Reason:51(ΦΟΡΗΤΟΤΗΤΑ)
        104 found. Reason:26(ΕΙΚΟΝΙΚΗ ΑΠΟΣΥΝΔΕΣΗ)
        87 found. Reason:24(δικαστηρια-οριστ.διακοπή)
        73 found. Reason:12(ΝΕΑ ΣΥΝΔΕΣΗ)
        68 found. Reason:16(ΝΕΑ ΣΥΝΔΕΣΗ DATA)
        52 found. Reason:1(Σε ισχυ)
        36 found. Reason:500(ΜΕΤΑΒ.  ΣΤΑΘΕΡΗΣ ΠΡΟΣ HRS)
        24 found. Reason:25(προς Δικαστήρια)
        23 found. Reason:36(Νέα σύνδεση-Αλλαγή Αριθμο)
        13 found. Reason:7(Μεταφορά)
        12 found. Reason:52(Νεα συνδεση σταθερο)
        11 found. Reason:20(Νέα Σύνδεση WIND)
        9 found. Reason:70(ΔΕΥΤΕΡΕΥΟΥΣΑ)
        6 found. Reason:()
        5 found. Reason:19(Μετ.Στην Cosmote)
        5 found. Reason:14(ΜΕΤΑΒ. ΚΙΝΗΤΗΣ ΠΡΟΣ HRS)
        4 found. Reason:50(ΜΕΤΑΦΟΡΑ ΑΠΟ VODAFONE)
        4 found. Reason:503(Νεο σταθερό LT)
        4 found. Reason:56(NEA ΣΥΝΔΕΣΗ ΑΝΑΚΥΚΛΩΣΗ )
        2 found. Reason:63(Αναμονή νέας σύνδεσης στα)
        2 found. Reason:22(Καρτοσυμβόλαιο Win Back)
        1 found. Reason:76(ΟΡΙΣΤΙΚΗ ΔΙΑΚΟΠΗ ΣΤΑΘΕΡΗΣ)
        1 found. Reason:2(Φραγή)
        1 found. Reason:60(Αναμονή data)
        1 found. Reason:17(Καρτοσυμβ. Νέα Γραμμη)
        1 found. Reason:61(Αναμονή νέας σύνδεσης κιν)
 */
