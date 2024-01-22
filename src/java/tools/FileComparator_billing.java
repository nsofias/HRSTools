package tools;

import java.io.FileWriter;
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
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import javax.servlet.jsp.JspWriter;
import model.CustomerEvent;

/**
 *
 * @author nsofias
 */
public class FileComparator_billing {

    public static String FileComparator_CHARSET = "CHARSET";
    public static String FileComparator_SPLITTER = "SPLITTER";
    public static String FileComparator_MAX_ACTIVATION_DATE = "MAX_ACTIVATION_DATE";
    //
    public static String FileComparator_IGNORE_LIST = "IGNORE_LIST";
    //------------------ HRS DATABASE ------------------------
    public static String FileComparator_ATLANTIS_filename = "ATLANTIS_filename";
    public static String FileComparator_ATLANTIS_MSISDN_index = "ATLANTIS_MSISDN_index";
    public static String FileComparator_ATLANTIS_MSISDN2_index = "ATLANTIS_MSISDN2_index";
    public static String FileComparator_ATLANTIS_DATE_index = "ATLANTIS_DATE_index";
    public static String FileComparator_ATLANTIS_STATUS_index = "ATLANTIS_STATUS_index";
    //------------------ HRS BILLING ------------------------
    public static String FileComparator_HRS_BILLING_filename = "HRS_BILLING_filename";
    public static String FileComparator_HRS_BILLING_MSISDN_index = "HRS_BILLING_MSISDN_index";
    //-------------------------- Vodafon DATABASE ----------------------------------------------
    public static String FileComparator_SB_HRS_filename = "SB_HRS_filename";
    public static String FileComparator_SB_HRS_MSISDN_index = "SB_HRS_MSISDN_index";
    public static String FileComparator_SB_HRS_DATE_index = "SB_HRS_DATE_index";
    //
    public static String FileComparator_ELRA_filename = "ELRA_filename";
    public static String FileComparator_ELRA_MSISDN_index = "ELRA_MSISDN_index";
    public static String FileComparator_ELRA_DATE_index = "ELRA_DATE_index";
    //
    public static String FileComparator_ELRA_PREPAY_filename = "ELRA_PREPAY_filename";
    public static String FileComparator_ELRA_PREPAY_MSISDN_index = "ELRA_PREPAY_MSISDN_index";
    public static String FileComparator_ELRA_PREPAY_DATE_index = "ELRA_PREPAY_DATE_index";
    //
    public static String FileComparator_VODAFONE_SPLIT_filename = "VODAFONE_SPLIT_filename";
    public static String FileComparator_VODAFONE_SPLIT_MSISDN_index = "VODAFONE_SPLIT_MSISDN_index";
    //-------------------------- Vodafon BILLING ----------------------------------------------
    public static String FileComparator_VODAFONE_PREPAY_filename = "VODAFONE_PREPAY_filename";
    public static String FileComparator_VODAFONE_PREPAY_MSISDN_index = "VODAFONE_PREPAY_MSISDN_index";
    //
    public static String FileComparator_VODAFONE_MOBILE_filename = "VODAFONE_MOBILE_filename";
    public static String FileComparator_VODAFONE_MOBILE_MSISDN_index = "VODAFONE_MOBILE_MSISDN_index";
    //
    public static String FileComparator_VODAFONE_FIX_filename = "VODAFONE_FIX_filename";
    public static String FileComparator_VODAFONE_FIX_MSISDN_index = "VODAFONE_FIX_MSISDN_index";
    public static String FileComparator_VODAFONE_FIX_CIRCUIT_index = "VODAFONE_FIX_CIRCUIT_index";
    //-------
    private Map<String, List<CustomerEvent>> VODAFONE_BILLING_Lines = new HashMap();
    private Map<String, List<CustomerEvent>> VODAFONE_DB_Lines = new HashMap();
    Map<String, String> statuses = new HashMap();
    //-----------------------
    Charset CHARSET = Charset.forName("ISO-8859-7");//StandardCharsets.ISO_8859_1;
    String SPLITTER = ";";
    String MAX_ACTIVATION_DATE = "20231030T000000";
    //
    List IGNORE_LIST = Arrays.asList("13", "6", "69", "72", "23", "75", "57", "4", "33", "24", "20", "19", "76");
    Map<String, String> circuitToMSISDN_mapping = new HashMap();
    //------------------ HRS database ------------------------
    String ATLANTIS_filename;
    int ATLANTIS_MSISDN_index;
    int ATLANTIS_MSISDN2_index;
    int ATLANTIS_DATE_index;
    int ATLANTIS_STATUS_index;
    //------------------ HRS billing ------------------------
    String HRS_BILLING_filename;
    int HRS_BILLING_MSISDN_index;
    //-------------------------- Vodafon files ----------------------------------------------
    String SB_HRS_filename;
    int SB_HRS_MSISDN_index;
    int SB_HRS_DATE_index;
    //
    String ELRA_filename;
    int ELRA_MSISDN_index;
    int ELRA_DATE_index;
    //
    String ELRA_PREPAY_filename;
    int ELRA_PREPAY_MSISDN_index;
    int ELRA_PREPAY_DATE_index;
    //
    String VODAFONE_SPLIT_filename;
    int VODAFONE_SPLIT_MSISDN_index;
    //
    String VODAFONE_PREPAY_filename;
    int VODAFONE_PREPAY_MSISDN_index;
    //
    String VODAFONE_MOBILE_filename;
    int VODAFONE_MOBILE_MSISDN_index;
    //
    String VODAFONE_FIX_filename;
    int VODAFONE_FIX_MSISDN_index;
    int VODAFONE_FIX_CIRCUIT_index;
    //
    private Map<String, List<CustomerEvent>> HRS_ALL_Lines = new HashMap();
    //
    Predicate<CustomerEvent> validRow = s -> isNumeric(s.getMSISDN()) && !s.getActivationDate().isEmpty();
    Predicate<CustomerEvent> invalidRow = s -> !(isNumeric(s.getMSISDN()) && !s.getActivationDate().isEmpty());
    Predicate<CustomerEvent> activationDateFilter = s -> !s.getActivationDate().isEmpty() && s.getActivationDate().compareTo(MAX_ACTIVATION_DATE) < 0;

    //#############################################################################################
    public FileComparator_billing(Properties myProperties) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("C:\\myfiles\\data\\HRSTools\\statuses.csv"), StandardCharsets.UTF_8);
            lines.forEach(l -> statuses.put(l.split(";")[0], l.split(";")[1]));
            System.out.println(statuses);
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
        }

        CHARSET = Charset.forName(myProperties.getProperty("CHARSET"));
        SPLITTER = myProperties.getProperty("SPLITTER");
        MAX_ACTIVATION_DATE = myProperties.getProperty("MAX_ACTIVATION_DATE");
        //
        IGNORE_LIST = Arrays.asList(myProperties.getProperty("IGNORE_LIST").split(","));
        System.out.println("IGNORE_LIST=" + IGNORE_LIST);
        //------------------ HRS database ------------------------
        ATLANTIS_filename = myProperties.getProperty("ATLANTIS_filename");
        ATLANTIS_MSISDN_index = Integer.parseInt(myProperties.getProperty("ATLANTIS_MSISDN_index"));
        ATLANTIS_MSISDN2_index = Integer.parseInt(myProperties.getProperty("ATLANTIS_MSISDN2_index"));
        ATLANTIS_DATE_index = Integer.parseInt(myProperties.getProperty("ATLANTIS_DATE_index"));
        ATLANTIS_STATUS_index = Integer.parseInt(myProperties.getProperty("ATLANTIS_STATUS_index"));
        //------------------ HRS billing ------------------------
        HRS_BILLING_filename = myProperties.getProperty("HRS_BILLING_filename");
        HRS_BILLING_MSISDN_index = Integer.parseInt(myProperties.getProperty("HRS_BILLING_MSISDN_index"));
        //-------------------------- Vodafon files ----------------------------------------------
        SB_HRS_filename = myProperties.getProperty("SB_HRS_filename");
        SB_HRS_MSISDN_index = Integer.parseInt(myProperties.getProperty("SB_HRS_MSISDN_index"));
        SB_HRS_DATE_index = Integer.parseInt(myProperties.getProperty("SB_HRS_DATE_index"));
        //
        ELRA_filename = myProperties.getProperty("ELRA_filename");
        ELRA_MSISDN_index = Integer.parseInt(myProperties.getProperty("ELRA_MSISDN_index"));
        ELRA_DATE_index = Integer.parseInt(myProperties.getProperty("ELRA_DATE_index"));
        //
        ELRA_PREPAY_filename = myProperties.getProperty("ELRA_PREPAY_filename");
        ELRA_PREPAY_MSISDN_index = Integer.parseInt(myProperties.getProperty("ELRA_PREPAY_MSISDN_index"));
        ELRA_PREPAY_DATE_index = Integer.parseInt(myProperties.getProperty("ELRA_PREPAY_DATE_index"));
        //
        VODAFONE_SPLIT_filename = myProperties.getProperty("VODAFONE_SPLIT_filename");
        VODAFONE_SPLIT_MSISDN_index = Integer.parseInt(myProperties.getProperty("VODAFONE_SPLIT_MSISDN_index"));
        //
        VODAFONE_PREPAY_filename = myProperties.getProperty("VODAFONE_PREPAY_filename");
        VODAFONE_PREPAY_MSISDN_index = Integer.parseInt(myProperties.getProperty("VODAFONE_PREPAY_MSISDN_index"));
        //
        VODAFONE_MOBILE_filename = myProperties.getProperty("VODAFONE_MOBILE_filename");
        VODAFONE_MOBILE_MSISDN_index = Integer.parseInt(myProperties.getProperty("VODAFONE_MOBILE_MSISDN_index"));
        //
        VODAFONE_FIX_filename = myProperties.getProperty("VODAFONE_FIX_filename");
        VODAFONE_FIX_MSISDN_index = Integer.parseInt(myProperties.getProperty("VODAFONE_FIX_MSISDN_index"));
        VODAFONE_FIX_CIRCUIT_index = Integer.parseInt(myProperties.getProperty("VODAFONE_FIX_CIRCUIT_index"));
    }

    public void loadFiles() throws IOException {
        //-------------- circuitToMSISDN_mapping --------------------
        Files.readAllLines(Paths.get(VODAFONE_FIX_filename), CHARSET)
                .stream()
                .forEach(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_FIX_MSISDN_index ? s.split(SPLITTER)[VODAFONE_FIX_MSISDN_index] : "";
                    String circuit = VODAFONE_FIX_CIRCUIT_index >= 0 && s.split(SPLITTER).length > VODAFONE_FIX_CIRCUIT_index ? s.split(SPLITTER)[VODAFONE_FIX_CIRCUIT_index] : "";
                    if (!circuit.isEmpty() && !msisdn.isEmpty()) {
                        circuitToMSISDN_mapping.put(circuit, msisdn);
                    }
                });
        System.out.println("circuitToMSISDN_mapping = " + circuitToMSISDN_mapping.size());
        //System.out.println("circuitToMSISDN_mapping ="+circuitToMSISDN_mapping);
        //************************ HRS lines ***************************************************** 
        System.out.println("------------- HRS (Atlantis) lines -------------------");
        Map<String, List<CustomerEvent>> ATLANTIS_lines = Files.readAllLines(Paths.get(ATLANTIS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ATLANTIS_MSISDN_index ? s.split(SPLITTER)[ATLANTIS_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ATLANTIS_DATE_index ? s.split(SPLITTER)[ATLANTIS_DATE_index] : "";
                    //System.out.println("-> activationDate=" + activationDate+" S="+s);
                    String status = ATLANTIS_STATUS_index >= 0 && s.split(SPLITTER).length > ATLANTIS_STATUS_index ? s.split(SPLITTER)[ATLANTIS_STATUS_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), formatDate(activationDate), status, s + " ATLANTIS");
                })
                .filter(validRow).filter(activationDateFilter).filter(e -> !IGNORE_LIST.contains(e.getStatus())).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("ATLANTIS_lines = " + ATLANTIS_lines.size());
        //---
        Map<String, List<CustomerEvent>> ATLANTIS_2ndlines = Files.readAllLines(Paths.get(ATLANTIS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String secondMSISDN = ATLANTIS_MSISDN2_index >= 0 && s.split(SPLITTER).length > ATLANTIS_MSISDN2_index ? s.split(SPLITTER)[ATLANTIS_MSISDN2_index] : "";
                    String activationDate = s.split(SPLITTER).length > ATLANTIS_DATE_index ? s.split(SPLITTER)[ATLANTIS_DATE_index] : "";
                    String status = ATLANTIS_STATUS_index >= 0 && s.split(SPLITTER).length > ATLANTIS_STATUS_index ? s.split(SPLITTER)[ATLANTIS_STATUS_index] : "";
                    return new CustomerEvent(circuitToMSISDN(secondMSISDN), formatDate(activationDate), status, s + " ATLANTIS_2ndlines");
                })
                .filter(validRow).filter(activationDateFilter).filter(e -> !e.getMSISDN().isEmpty() && !IGNORE_LIST.contains(e.getStatus()))
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("ATLANTIS_2ndlines = " + ATLANTIS_2ndlines.size());
        HRS_ALL_Lines.putAll(ATLANTIS_2ndlines);
        HRS_ALL_Lines.putAll(ATLANTIS_lines);
        System.out.println("ATLANTIS_lines total = " + ATLANTIS_lines.size());
        System.out.println("------------- HRS Billing files----");
        Map<String, List<CustomerEvent>> HRS_BILLING_Lines = Files.readAllLines(Paths.get(HRS_BILLING_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > HRS_BILLING_MSISDN_index ? s.split(SPLITTER)[HRS_BILLING_MSISDN_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), "", "", s + " HRS_BILLING");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("HRS_BILLING_Lines = " + HRS_BILLING_Lines.size());
        //
        //************************ Vodafone DB lines ***************************************************** 
        System.out.println("------------- Vodafon DB files -------------------");
        Map<String, List<CustomerEvent>> SB_HRS_lines = Files.readAllLines(Paths.get(SB_HRS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > SB_HRS_MSISDN_index ? s.split(SPLITTER)[SB_HRS_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > SB_HRS_DATE_index ? s.split(SPLITTER)[SB_HRS_DATE_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), formatDate(activationDate), "", s + " SB_HRS_lines");
                })
                .filter(validRow).filter(activationDateFilter).filter(e -> !IGNORE_LIST.contains(e.getStatus()))
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("SB_HRS_lines = " + SB_HRS_lines.size());
        //
        Map<String, List<CustomerEvent>> elra_Prepay_lines = Files.readAllLines(Paths.get(ELRA_PREPAY_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ELRA_PREPAY_MSISDN_index ? s.split(SPLITTER)[ELRA_PREPAY_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ELRA_PREPAY_DATE_index ? s.split(SPLITTER)[ELRA_PREPAY_DATE_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), formatDate(activationDate), "", s + " elra_Prepay_lines");
                })
                .filter(validRow).filter(activationDateFilter).filter(e -> !IGNORE_LIST.contains(e.getStatus())).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("elra_Prepay_lines = " + elra_Prepay_lines.size());
        //
        Map<String, List<CustomerEvent>> elra_lines = Files.readAllLines(Paths.get(ELRA_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ELRA_MSISDN_index ? s.split(SPLITTER)[ELRA_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ELRA_DATE_index ? s.split(SPLITTER)[ELRA_DATE_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), formatDate(activationDate), "", s + " elra_lines");
                })
                .filter(validRow).filter(activationDateFilter).filter(e -> !IGNORE_LIST.contains(e.getStatus())).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("elra_lines = " + elra_lines.size());
        VODAFONE_DB_Lines.putAll(SB_HRS_lines);
        VODAFONE_DB_Lines.putAll(elra_Prepay_lines);
        VODAFONE_DB_Lines.putAll(elra_lines);
        //
        //------------- Vodafon billing files ------------------- 
        System.out.println("------------- Vodafon billing files -------------------");
        Map<String, List<CustomerEvent>> VODAFONE_SPLIT_Lines = Files.readAllLines(Paths.get(VODAFONE_SPLIT_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_SPLIT_MSISDN_index ? s.split(SPLITTER)[VODAFONE_SPLIT_MSISDN_index] : "";
                    //System.out.println("VODAFONE_SPLIT_Lines line="+msisdn);
                    return new CustomerEvent(circuitToMSISDN(msisdn), "", "", s + " VODAFONE_SPLIT");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_SPLIT_Lines = " + VODAFONE_SPLIT_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_PREPAY_Lines = Files.readAllLines(Paths.get(VODAFONE_PREPAY_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_PREPAY_MSISDN_index ? s.split(SPLITTER)[VODAFONE_PREPAY_MSISDN_index] : "";
                    //System.out.println("VODAFONE_PREPAY_Lines line="+msisdn);
                    return new CustomerEvent(circuitToMSISDN(msisdn), "", "", s + " VODAFONE_PREPAY");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_PREPAY_Lines = " + VODAFONE_PREPAY_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_MOBILE_Lines = Files.readAllLines(Paths.get(VODAFONE_MOBILE_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_MOBILE_MSISDN_index ? s.split(SPLITTER)[VODAFONE_MOBILE_MSISDN_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), "", "", s + " VODAFONE_MOBILE");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_MOBILE_Lines = " + VODAFONE_MOBILE_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_FIX_Lines = Files.readAllLines(Paths.get(VODAFONE_FIX_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_FIX_MSISDN_index ? s.split(SPLITTER)[VODAFONE_FIX_MSISDN_index] : "";
                    return new CustomerEvent(circuitToMSISDN(msisdn), "", "", s + " VODAFONE_FIX");
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_FIX_Lines = " + VODAFONE_FIX_Lines.size());
        //
        VODAFONE_BILLING_Lines.putAll(VODAFONE_SPLIT_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_PREPAY_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_MOBILE_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_FIX_Lines);
    }

    public Set<CustomerEvent> HRS_NO_BILLING() throws IOException {
        return LEFT_only(HRS_ALL_Lines, VODAFONE_BILLING_Lines);
    }

    public Set<CustomerEvent> HRS_NO_VODDB() throws IOException {
        return LEFT_only(HRS_ALL_Lines, VODAFONE_DB_Lines);
    }

    public Set<CustomerEvent> VODAFON_BILLING_only() throws IOException {
        return LEFT_only(VODAFONE_BILLING_Lines, HRS_ALL_Lines);
    }

    public Set<CustomerEvent> VODAFON_DB_only() throws IOException {
        return LEFT_only(VODAFONE_DB_Lines, HRS_ALL_Lines);
    }

    public Set<CustomerEvent> LEFT_only(Map<String, List<CustomerEvent>> M1, Map<String, List<CustomerEvent>> M2) throws IOException {
        return M1.values().stream()
                .flatMap(l -> l.stream())
                .filter(e -> !M2.containsKey(e.getMSISDN()))
                .collect(toSet());
    }

    private String circuitToMSISDN(String circuit) {
        if (circuit.startsWith("O")) {
            String res = circuitToMSISDN_mapping.get(circuit);
            if (res != null) {
                return res;
            }
        }
        return circuit;//it is in fact an MSISDN
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public Set<CustomerEvent> getInvalidBaseRows() throws IOException {
        return HRS_ALL_Lines.values()
                .stream()
                .flatMap(l -> l.stream())
                .filter(invalidRow)
                // .peek(s->System.out.println("invalid ---->"+s))
                .collect(toSet());
    }

    public void report() throws IOException {
        Set<CustomerEvent> myInvalidRows = getInvalidBaseRows();
        Set<CustomerEvent> HRS_NO_VODDB = HRS_NO_VODDB();
        Set<CustomerEvent> HRS_NO_BILLING = HRS_NO_BILLING();
        Set<CustomerEvent> VODAFON_BILLING_only = VODAFON_BILLING_only();
        Set<CustomerEvent> VODAFON_DB_only = VODAFON_DB_only();

        //---
        System.out.println();
        System.out.println("*************** SUMMARY ***********");
        System.out.println("IGNORE_LIST:" + IGNORE_LIST);
        System.out.println("\nHRS records (Atlantis) : " + HRS_ALL_Lines.size());
        System.out.println("VODAFONE DB records : " + VODAFONE_DB_Lines.size());
        System.out.println("VODAFONE BILLING records: " + VODAFONE_BILLING_Lines.size());
        //---
        //System.out.println("\nINVALID ATLANTIS rows found: " + myInvalidRows.size());
        {
            System.out.println("\n\nNumbers that exist in HRS but not in Vod Billing : " + HRS_NO_BILLING.size());
            Map<String, Long> statusList = HRS_NO_BILLING.stream()
                    .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
            statusList.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                System.out.println("        " + e.getValue() + " - " + statuses.get(e.getKey()));
            });
        }
        //---
        System.out.println("\nNumbers that exist ONLY in VOD BILLING but not in HRS: " + VODAFON_BILLING_only.size());
        //---
        {
            System.out.println("\nNumbers that exist in HRS but not in Vod DB : " + HRS_NO_VODDB.size());
            Map<String, Long> statusList = HRS_NO_VODDB.stream()
                    .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
            statusList.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                System.out.println("        " + e.getValue() + " - " + statuses.get(e.getKey()));
            });
        }
        //---        
        System.out.println("\nNumbers that exist ONLY in VOD DB but not in HRS : " + VODAFON_DB_only.size());
        //-----------
        System.out.println();
        System.out.println("\n\n\n\n***************************************************************");
        System.out.println("*** DETAILS ***");
        System.out.println("***************************************************************");
        //---------
        System.out.println("\n\n*** Numbers that exist in HRS but not in Vod Billing, details ***");
        HRS_NO_BILLING.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> System.out.println("  HRS_NO_BILLING: " + s.getMSISDN() + " : " + s));
        //----------       
        System.out.println("\n\n*** umbers that exist ONLY in VOD BILLING but not in HRS, details ***");
        VODAFON_BILLING_only.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> System.out.println("  VODAFON_BILLING_only-> " + s.getMSISDN() + " : " + s));
        //----------
        System.out.println("\n\n*** Numbers that exist in HRS but not in Vod DB, details ***");
        HRS_NO_VODDB.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> System.out.println("  HRS_NO_VODDB: " + s.getMSISDN() + " : " + s));

        //----------        
        System.out.println("\n\n*** Numbers that exist ONLY in VOD DB but not in HRS, details ***");
        VODAFON_DB_only.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> System.out.println("  VODAFON_DB_only-> " + s.getMSISDN() + " : " + s));
    }

    public void report(JspWriter out) throws IOException {
        Set<CustomerEvent> myInvalidRows = getInvalidBaseRows();
        Set<CustomerEvent> HRS_NO_VODDB = HRS_NO_VODDB();
        Set<CustomerEvent> HRS_NO_BILLING = HRS_NO_BILLING();
        Set<CustomerEvent> VODAFON_BILLING_only = VODAFON_BILLING_only();
        Set<CustomerEvent> VODAFON_DB_only = VODAFON_DB_only();

        //---
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "IGNORE_LIST:" + IGNORE_LIST);
        out.println("<p>" + "HRS records (Atlantis) : " + HRS_ALL_Lines.size());
        out.println("<p>" + "VODAFONE DB records : " + VODAFONE_DB_Lines.size());
        out.println("<p>" + "VODAFONE BILLING records: " + VODAFONE_BILLING_Lines.size());
        //---
        //System.out.println("<p>"+"\nINVALID ATLANTIS rows found: " + myInvalidRows.size());
        {
            out.println("<h1>" + "Numbers that exist in HRS but not in Vod Billing : " + HRS_NO_BILLING.size() + "</h1>");
            Map<String, Long> statusList = HRS_NO_BILLING.stream()
                    .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
            statusList.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                try {
                    out.println("<p>" + "        " + e.getValue() + " - " + statuses.get(e.getKey()));
                } catch (Exception ex) {
                }
            });
        }
        //---
        out.println("<h1>" + "Numbers that exist ONLY in VOD BILLING but not in HRS: " + VODAFON_BILLING_only.size() + "</h1>");
        //---
        {
            out.println("<p>" + "\nNumbers that exist in HRS but not in Vod DB : " + HRS_NO_VODDB.size());
            Map<String, Long> statusList = HRS_NO_VODDB.stream()
                    .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
            statusList.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
                try {
                    out.println("<p>" + "        " + e.getValue() + " - " + statuses.get(e.getKey()));
                } catch (Exception ex) {
                }
            });
        }
        //---        
        out.println("<h1>" + "\nNumbers that exist ONLY in VOD DB but not in HRS : " + VODAFON_DB_only.size() + "</h1>");
        //-----------
        out.println("<p>");
        out.println("<p>" + "***************************************************************");
        out.println("<p>" + "*** DETAILS ***");
        out.println("<p>" + "***************************************************************");
        //---------
        out.println("<p>" + "\n\n*** Numbers that exist in HRS but not in Vod Billing, details ***");
        HRS_NO_BILLING.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> {
                    try {
                        out.println("<p>" + "  HRS_NO_BILLING: " + s.getMSISDN() + " : " + s);
                    } catch (IOException ex) {
                    }
                });
        //----------       
        out.println("<p>" + "\n\n*** umbers that exist ONLY in VOD BILLING but not in HRS, details ***");
        VODAFON_BILLING_only.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> {
                    try {
                        out.println("<p>" + "  VODAFON_BILLING_only-> " + s.getMSISDN() + " : " + s);
                    } catch (IOException ex) {
                    }
                });
        //----------
        out.println("<p>" + "\n\n*** Numbers that exist in HRS but not in Vod DB, details ***");
        HRS_NO_VODDB.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> {
                    try {
                        out.println("<p>" + "  HRS_NO_VODDB: " + s.getMSISDN() + " : " + s);
                    } catch (IOException ex) {
                    }
                });

        //----------        
        out.println("<p>" + "\n\n*** Numbers that exist ONLY in VOD DB but not in HRS, details ***");
        VODAFON_DB_only.stream().sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> {
                    try {
                        out.println("<p>" + "  VODAFON_DB_only-> " + s.getMSISDN() + " : " + s);
                    } catch (IOException ex) {
                    }
                });
    }

    private static String formatDate(String activationDate) {
        // 16/12/2022 10:48
        String activationDateFormatted = "";
        try {
            activationDateFormatted = activationDate.split(" ")[0];
            String[] parts = activationDateFormatted.split("/");
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
            return activationDateFormatted;
        }
    }

    public static void main(String[] args) {
        System.out.println(formatDate("9/5/2014"));
        Properties myProperties = new Properties();
        //-----------------------
        myProperties.put(FileComparator_billing.FileComparator_CHARSET, "ISO-8859-7");//StandardCharsets.ISO_8859_1;
        myProperties.put(FileComparator_billing.FileComparator_SPLITTER, ";");
        myProperties.put(FileComparator_billing.FileComparator_MAX_ACTIVATION_DATE, "20231030T000000");
        //
        myProperties.put(FileComparator_billing.FileComparator_IGNORE_LIST, "13, 6, 69, 72, 23, 75, 57, 4, 33, 24, 20, 19, 76");
        //------------------ HRS files ------------------------
        myProperties.put(FileComparator_billing.FileComparator_ATLANTIS_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΒΑΣΗ HRS ATLANTIS.csv");
        myProperties.put(FileComparator_billing.FileComparator_ATLANTIS_MSISDN_index, "3");
        myProperties.put(FileComparator_billing.FileComparator_ATLANTIS_MSISDN2_index, "6");
        myProperties.put(FileComparator_billing.FileComparator_ATLANTIS_DATE_index, "11");
        myProperties.put(FileComparator_billing.FileComparator_ATLANTIS_STATUS_index, "10");
        //-------------------------- Vodafon files ----------------------------------------------
        myProperties.put(FileComparator_billing.FileComparator_SB_HRS_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\SB_HRS_November_2023 new.csv");
        myProperties.put(FileComparator_billing.FileComparator_SB_HRS_MSISDN_index, "3");
        myProperties.put(FileComparator_billing.FileComparator_SB_HRS_DATE_index, "5");
        //
        myProperties.put(FileComparator_billing.FileComparator_ELRA_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\elra.csv");
        myProperties.put(FileComparator_billing.FileComparator_ELRA_MSISDN_index, "1");
        myProperties.put(FileComparator_billing.FileComparator_ELRA_DATE_index, "4");
        //
        myProperties.put(FileComparator_billing.FileComparator_ELRA_PREPAY_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\elra_Prepay.csv");
        myProperties.put(FileComparator_billing.FileComparator_ELRA_PREPAY_MSISDN_index, "5");
        myProperties.put(FileComparator_billing.FileComparator_ELRA_PREPAY_DATE_index, "3");
        //
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_SPLIT_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ SPLIT VODAFONE.csv");
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_SPLIT_MSISDN_index, "3");

        //
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_PREPAY_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΚΑΡΤΟΚΙΝΗΤΗ VODAFONE.csv");
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_PREPAY_MSISDN_index, "3");
        //
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_MOBILE_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΚΙΝΗΤΗ VODAFONE.csv");
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_MOBILE_MSISDN_index, "3");
        //
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_FIX_filename, "C:\\myfiles\\data\\HRSTools\\data\\november_2023\\csv\\ΝΟΕΜΒΡΙΟΣ ΣΤΑΘΕΡΗ VODAFONE.csv");
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_FIX_MSISDN_index, "5");
        myProperties.put(FileComparator_billing.FileComparator_VODAFONE_FIX_CIRCUIT_index, "6");
        //---------------
        System.out.println(myProperties);
        try {
            myProperties.store(new FileWriter("C:\\myfiles\\data\\HRSTools\\conf\\params.properties"), "utf-8");
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileComparator_billing myFileComparator = new FileComparator_billing(myProperties);
        try {
            myFileComparator.loadFiles();
            myFileComparator.report();
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
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
