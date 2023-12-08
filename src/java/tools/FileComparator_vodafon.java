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
import static java.util.stream.Collectors.toSet;
import model.CustomerEvent;

/**
 *
 * @author nsofias
 */
public class FileComparator_vodafon {

    Map<String, String> statuses = new HashMap();
    Charset charset = StandardCharsets.UTF_8;
    String splitter = ";";
    String max_activationDate = "20231030T000000";
    //SB_HRS_November_2023.csv
    String SB_HRS_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\SB_HRS_November_2023.csv";
    int SB_HRS_index = 3;
    private Map<String, List<CustomerEvent>> SB_HRS_Lines;
    //
    String elra_Prepay_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\elra_Prepay.csv";
    int elra_Prepay_index = 5;
    private Map<String, List<CustomerEvent>> elra_Prepay_Lines;
    //
    String elra_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\elra.csv";
    int elra_index = 1;
    private Map<String, List<CustomerEvent>> elra_Lines;
    //
    String VODAFONE_SPLIT_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ SPLIT VODAFONE.csv";
    int VODAFONE_SPLIT_MSISDN_index = 3;
    private Map<String, List<CustomerEvent>> VODAFONE_SPLIT_Lines;
    //
    String VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΚΑΡΤΟΚΙΝΗΤΗ VODAFONE.csv";
    int VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_MSISDN_index = 3;
    private Map<String, List<CustomerEvent>> VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines;
    //
    String VODAFONE_ΚΙΝΗΤΗ_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΚΙΝΗΤΗ VODAFONE.csv";
    int VODAFONE_ΚΙΝΗΤΗ_MSISDN_index = 3;
    private Map<String, List<CustomerEvent>> VODAFONE_ΚΙΝΗΤΗ_Lines;
    //
    String VODAFONE_ΣΤΑΘΕΡΗ_filename = "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΣΤΑΘΕΡΗ VODAFONE.csv";
    int VODAFONE_ΣΤΑΘΕΡΗ_MSISDN_index = 3;
    int VODAFONE_ΣΤΑΘΕΡΗ_MSISDN2_index = 6;
    //int O000xxx_index = 6;
    private Map<String, List<CustomerEvent>> VODAFONE_ΣΤΑΘΕΡΗ_Lines;
    //
    private Map<String, List<CustomerEvent>> VODAFONE_ALL_Lines = new HashMap();
    private Map<String, List<CustomerEvent>> VODAFONE_elra_ALL_Lines = new HashMap();
    //
    Predicate<CustomerEvent> activationDateFilter = s -> s.getActivationDate().compareTo(max_activationDate) < 0;

    public FileComparator_vodafon() {
        this.charset = StandardCharsets.UTF_8;
        try {
            List<String> lines = Files.readAllLines(Paths.get("C:\\myfiles\\data\\HRSTools\\statuses.csv"), StandardCharsets.UTF_8);
            lines.forEach(l -> statuses.put(l.split(";")[0], l.split(";")[1]));
            System.out.println(statuses);
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_vodafon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadFiles() throws IOException {
        //databases

        SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > SB_HRS_index ? s.split(splitter)[SB_HRS_index] : "";
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: SB_HRS_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("SB_HRS_Lines total=" + SB_HRS_Lines.size());
        //        
        elra_Prepay_Lines = Files.readAllLines(Paths.get(elra_Prepay_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > elra_Prepay_index ? s.split(splitter)[elra_Prepay_index] : "";
                    //System.out.println("VODAFONE_SPLIT_Lines line="+msisdn);
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: elra_Prepay_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("elra_Prepay_Lines total=" + elra_Prepay_Lines.size());
        //
        elra_Lines = Files.readAllLines(Paths.get(elra_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > elra_index ? s.split(splitter)[elra_index] : "";
                    //System.out.println("VODAFONE_SPLIT_Lines line="+msisdn);
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: elra_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("elra_Lines total=" + elra_Lines.size());
        //
        VODAFONE_elra_ALL_Lines.putAll(SB_HRS_Lines);
        VODAFONE_elra_ALL_Lines.putAll(elra_Prepay_Lines);
        VODAFONE_elra_ALL_Lines.putAll(elra_Lines);
        //---        
        VODAFONE_SPLIT_Lines = Files.readAllLines(Paths.get(VODAFONE_SPLIT_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > VODAFONE_SPLIT_MSISDN_index ? s.split(splitter)[VODAFONE_SPLIT_MSISDN_index] : "";
                    //System.out.println("VODAFONE_SPLIT_Lines line="+msisdn);
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: VODAFONE_SPLIT_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_SPLIT_Lines total=" + VODAFONE_SPLIT_Lines.size());
        //---
        VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines = Files.readAllLines(Paths.get(VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_MSISDN_index ? s.split(splitter)[VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_MSISDN_index] : "";
                    //System.out.println("VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines line="+msisdn);
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines total=" + VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines.size());
        //---
        VODAFONE_ΚΙΝΗΤΗ_Lines = Files.readAllLines(Paths.get(VODAFONE_ΚΙΝΗΤΗ_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(splitter).length > VODAFONE_ΚΙΝΗΤΗ_MSISDN_index ? s.split(splitter)[VODAFONE_ΚΙΝΗΤΗ_MSISDN_index] : "";
                    //System.out.println("VODAFONE_ΚΙΝΗΤΗ_Lines line="+msisdn);
                    return new CustomerEvent(msisdn, "", "", "", "", s + " file: VODAFONE_ΚΙΝΗΤΗ_Lines");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_ΚΙΝΗΤΗ_Lines total=" + VODAFONE_ΚΙΝΗΤΗ_Lines.size());
        //---
        VODAFONE_ΣΤΑΘΕΡΗ_Lines = Files.readAllLines(Paths.get(VODAFONE_ΣΤΑΘΕΡΗ_filename), charset)
                .stream()
                .map(s -> {
                    //System.out.println("s.split(splitter).length="+s.split(splitter).length);
                    String msisdn = s.split(splitter).length > VODAFONE_ΣΤΑΘΕΡΗ_MSISDN_index ? s.split(splitter)[VODAFONE_ΣΤΑΘΕΡΗ_MSISDN_index] : "";
                    //System.out.println("VODAFONE_ΣΤΑΘΕΡΗ_Lines line="+msisdn);
                    String HRS_BASE_secondMSISDN = s.split(splitter).length > VODAFONE_ΣΤΑΘΕΡΗ_MSISDN2_index ? s.split(splitter)[VODAFONE_ΣΤΑΘΕΡΗ_MSISDN2_index] : "";
                    return new CustomerEvent(msisdn, HRS_BASE_secondMSISDN, "", "", "", s + " file: VODAFONE_ΣΤΑΘΕΡΗ_filename");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        Map<String, List<CustomerEvent>> VODAFONE_ΣΤΑΘΕΡΗ_2ndLines = Files.readAllLines(Paths.get(VODAFONE_ΣΤΑΘΕΡΗ_filename), charset)
                .stream()
                .map(s -> {
                    //System.out.println("s.split(splitter).length="+s.split(splitter).length);
                    String msisdn = s.split(splitter).length > VODAFONE_ΣΤΑΘΕΡΗ_MSISDN_index ? s.split(splitter)[VODAFONE_ΣΤΑΘΕΡΗ_MSISDN_index] : "";
                    String HRS_BASE_secondMSISDN = s.split(splitter).length > VODAFONE_ΣΤΑΘΕΡΗ_MSISDN2_index ? s.split(splitter)[VODAFONE_ΣΤΑΘΕΡΗ_MSISDN2_index] : "";
                    return new CustomerEvent(msisdn, HRS_BASE_secondMSISDN, "", "", "", s + " file: VODAFONE_ΣΤΑΘΕΡΗ_filename");
                })
                .filter(e -> !e.getSecondMSISDN().isEmpty())
                .collect(Collectors.groupingBy(l -> l.getSecondMSISDN()));
        VODAFONE_ΣΤΑΘΕΡΗ_Lines.putAll(VODAFONE_ΣΤΑΘΕΡΗ_2ndLines);
        System.out.println("VODAFONE_ΣΤΑΘΕΡΗ_Lines total=" + VODAFONE_ΣΤΑΘΕΡΗ_Lines.size());
        //
        VODAFONE_ALL_Lines.putAll(VODAFONE_SPLIT_Lines);
        VODAFONE_ALL_Lines.putAll(VODAFONE_ΚΑΡΤΟΚΙΝΗΤΗ_Lines);
        VODAFONE_ALL_Lines.putAll(VODAFONE_ΚΙΝΗΤΗ_Lines);
        VODAFONE_ALL_Lines.putAll(VODAFONE_ΣΤΑΘΕΡΗ_Lines);
    }

    public Set<CustomerEvent> base_only() throws IOException {
        return VODAFONE_elra_ALL_Lines.values().stream()
                .flatMap(l -> l.stream())
                .filter(activationDateFilter)
                .filter(e -> !VODAFONE_ALL_Lines.containsKey(e.getMSISDN()) && !VODAFONE_ALL_Lines.containsKey(e.getSecondMSISDN()))
                .collect(toSet());
    }

    public Set<CustomerEvent> VODAFON_only() throws IOException {
        return VODAFONE_ALL_Lines.values().stream()
                .flatMap(l -> l.stream())
                .filter(e -> !VODAFONE_elra_ALL_Lines.containsKey(e.getMSISDN()) && !VODAFONE_elra_ALL_Lines.containsKey(e.getSecondMSISDN()))
                .collect(toSet());

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
        Set<CustomerEvent> ATLANTIS_only = base_only();
        Set<CustomerEvent> VODAFON_only = VODAFON_only();
        //---
        System.out.println();
        System.out.println("*************** SUMMARY ***********");
        System.out.println("\nVODAFONE_ALL_Lines found: " + VODAFONE_ALL_Lines.size());
        System.out.println("\nVODAFONE_elra_ALL_Lines found: " + VODAFONE_elra_ALL_Lines.size());

        //---
        System.out.println("\nNumbers that exist ONLY in Vodafon DB, found: " + ATLANTIS_only.size());
        Map<String, Long> atlantis_statuses = ATLANTIS_only.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey() + " - " + statuses.get(e.getKey()));
        });
        System.out.println("\nReasons for Numbers that exist ONLY in VODAFON (but not in Vodafon DB), found: " + VODAFON_only.size());
        Map<String, Long> VOD_ELRA_statuses = VODAFON_only.stream()
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        VOD_ELRA_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey());
        });

        System.out.println();
        System.out.println("\n\n\n\n***************************************************************");
        System.out.println("*** DETAILS ***");
        System.out.println("***************************************************************");
        //-------------
        System.out.println("*** Invalid Vodafon DB rows details ***");
        //---------
        System.out.println("\n\n*** Numbers that exist ONLY in DB (not found in Vodafone files), details ***");
        ATLANTIS_only.stream().sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> {
            System.out.println("  Vodafon DB_ONLY: " + s.getMSISDN() + " : " + s);
        });
        //----------
        System.out.println("\n\n*** Numbers that exist ONLY in VODAFON (not in Vodafon DB): " + VODAFON_only.size());
        VODAFON_only.stream().sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> System.out.println(" VODAFON_only-> " + s.getMSISDN() + " : " + s));
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

        FileComparator_vodafon myFileComparator = new FileComparator_vodafon();
        try {
            myFileComparator.loadFiles();
            myFileComparator.report();
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_vodafon.class.getName()).log(Level.SEVERE, null, ex);
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
