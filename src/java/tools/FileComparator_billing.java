package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.jsp.JspWriter;
import nsofiasLib.time.TimeStamp1;

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
    //-------------------------- Vodafon BILLING ----------------------------------------------
    public static String FileComparator_VODAFONE_SPLIT_filename = "VODAFONE_SPLIT_filename";
    public static String FileComparator_VODAFONE_SPLIT_MSISDN_index = "VODAFONE_SPLIT_MSISDN_index";
    public static String FileComparator_VODAFONE_SPLIT_VALUE_index = "VODAFONE_SPLIT_VALUE_index";
    //
    public static String FileComparator_VODAFONE_PREPAY_filename = "VODAFONE_PREPAY_filename";
    public static String FileComparator_VODAFONE_PREPAY_MSISDN_index = "VODAFONE_PREPAY_MSISDN_index";
    public static String FileComparator_VODAFONE_PREPAY_VALUE_index = "VODAFONE_PREPAY_VALUE_index";
    //
    public static String FileComparator_VODAFONE_MOBILE_filename = "VODAFONE_MOBILE_filename";
    public static String FileComparator_VODAFONE_MOBILE_MSISDN_index = "VODAFONE_MOBILE_MSISDN_index";
    public static String FileComparator_VODAFONE_MOBILE_VALUE_index = "VODAFONE_MOBILE_VALUE_index";
    //
    public static String FileComparator_VODAFONE_FIX_filename = "VODAFONE_FIX_filename";
    public static String FileComparator_VODAFONE_FIX_MSISDN_index = "VODAFONE_FIX_MSISDN_index";
    public static String FileComparator_VODAFONE_FIX_VALUE_index = "VODAFONE_FIX_VALUE_index";
    //public static String FileComparator_VODAFONE_FIX_ERP_index = "VODAFONE_FIX_ERP_index";
    public static String FileComparator_VODAFONE_FIX_CIRCUIT_index = "VODAFONE_FIX_CIRCUIT_index";
    //-------
    private Map<String, List<CustomerEvent>> VODAFONE_BILLING_Lines = new HashMap();
    private Map<String, List<CustomerEvent>> VODAFONE_DB_Lines = new HashMap();
    private Map<String, List<CustomerEvent>> elra_Prepay_lines = new HashMap();
    private Map<String, List<CustomerEvent>> HRS_DB_Lines = new HashMap();
    private Map<String, List<CustomerEvent>> HRS_BILLING_Lines = new HashMap();
    Map<String, String> statuses = new HashMap();
    //-----------------------
    Charset CHARSET = Charset.forName("ISO-8859-7");//StandardCharsets.ISO_8859_1;
    String SPLITTER = ";";
    String MAX_ACTIVATION_DATE = "20231030T000000";
    //
    List IGNORE_LIST = Arrays.asList("13", "6", "69", "72", "23", "75", "57", "4", "33", "24", "20", "19", "76");
    Map<String, String> MSISDN_to_CIRCUIT_mapping = new HashMap();
    Set<String> blocked_numbers = new HashSet();
    Set<String> secondary_numbers = new HashSet();
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
    int VODAFONE_SPLIT_VALUE_index;
    //
    String VODAFONE_PREPAY_filename;
    int VODAFONE_PREPAY_MSISDN_index;
    int VODAFONE_PREPAY_VALUE_index;
    //
    String VODAFONE_MOBILE_filename;
    int VODAFONE_MOBILE_MSISDN_index;
    int VODAFONE_MOBILE_VALUE_index;
    //
    String VODAFONE_FIX_filename;
    int VODAFONE_FIX_MSISDN_index;
    int VODAFONE_FIX_CIRCUIT_index;
    int VODAFONE_FIX_VALUE_index;
    //
    Predicate<CustomerEvent> validRow = s -> !s.getMSISDN().isEmpty() && !s.getActivationDate().isEmpty();
    Predicate<CustomerEvent> myDBFilter = s -> !IGNORE_LIST.contains(s.getStatus()) && !s.getActivationDate().isEmpty() && s.getActivationDate().compareTo(MAX_ACTIVATION_DATE) < 0;
    Predicate<Entry<String, List<CustomerEvent>>> hasBillingValue = e -> e.getValue().stream()
            .collect(Collectors.summingDouble(custEvent -> custEvent.getValue())) > 2.48;

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
        ATLANTIS_filename = myProperties.getProperty(FileComparator_ATLANTIS_filename);
        ATLANTIS_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_ATLANTIS_MSISDN_index));
        ATLANTIS_MSISDN2_index = Integer.parseInt(myProperties.getProperty(FileComparator_ATLANTIS_MSISDN2_index));
        ATLANTIS_DATE_index = Integer.parseInt(myProperties.getProperty(FileComparator_ATLANTIS_DATE_index));
        ATLANTIS_STATUS_index = Integer.parseInt(myProperties.getProperty(FileComparator_ATLANTIS_STATUS_index));
        //------------------ HRS billing ------------------------
        HRS_BILLING_filename = myProperties.getProperty(FileComparator_HRS_BILLING_filename);
        HRS_BILLING_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_HRS_BILLING_MSISDN_index));
        //-------------------------- Vodafon files ----------------------------------------------
        SB_HRS_filename = myProperties.getProperty(FileComparator_SB_HRS_filename);
        SB_HRS_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_SB_HRS_MSISDN_index));
        SB_HRS_DATE_index = Integer.parseInt(myProperties.getProperty(FileComparator_SB_HRS_DATE_index));
        //
        ELRA_filename = myProperties.getProperty(FileComparator_ELRA_filename);
        ELRA_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_ELRA_MSISDN_index));
        ELRA_DATE_index = Integer.parseInt(myProperties.getProperty(FileComparator_ELRA_DATE_index));
        //
        ELRA_PREPAY_filename = myProperties.getProperty(FileComparator_ELRA_PREPAY_filename);
        ELRA_PREPAY_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_ELRA_PREPAY_MSISDN_index));
        ELRA_PREPAY_DATE_index = Integer.parseInt(myProperties.getProperty(FileComparator_ELRA_PREPAY_DATE_index));
        //-------- vodafon billing ------------
        VODAFONE_SPLIT_filename = myProperties.getProperty(FileComparator_VODAFONE_SPLIT_filename);
        VODAFONE_SPLIT_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_SPLIT_MSISDN_index));
        VODAFONE_SPLIT_VALUE_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_SPLIT_VALUE_index));
        //
        VODAFONE_PREPAY_filename = myProperties.getProperty(FileComparator_VODAFONE_PREPAY_filename);
        VODAFONE_PREPAY_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_PREPAY_MSISDN_index));
        VODAFONE_PREPAY_VALUE_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_PREPAY_VALUE_index));
        //
        VODAFONE_MOBILE_filename = myProperties.getProperty(FileComparator_VODAFONE_MOBILE_filename);
        VODAFONE_MOBILE_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_MOBILE_MSISDN_index));
        VODAFONE_MOBILE_VALUE_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_MOBILE_VALUE_index));
        //
        VODAFONE_FIX_filename = myProperties.getProperty(FileComparator_VODAFONE_FIX_filename);
        VODAFONE_FIX_MSISDN_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_FIX_MSISDN_index));
        VODAFONE_FIX_VALUE_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_FIX_VALUE_index));
        VODAFONE_FIX_CIRCUIT_index = Integer.parseInt(myProperties.getProperty(FileComparator_VODAFONE_FIX_CIRCUIT_index));
    }

    public void loadFiles() throws IOException {
        //-------------- MSISDN_to_CIRCUIT_mapping --------------------
        Files.readAllLines(Paths.get(VODAFONE_FIX_filename), CHARSET)
                .stream()
                .forEach(s -> {
                    String msisdn = s.split(SPLITTER).length > VODAFONE_FIX_MSISDN_index ? s.split(SPLITTER)[VODAFONE_FIX_MSISDN_index] : "";
                    String circuit = VODAFONE_FIX_CIRCUIT_index >= 0 && s.split(SPLITTER).length > VODAFONE_FIX_CIRCUIT_index ? s.split(SPLITTER)[VODAFONE_FIX_CIRCUIT_index] : "";
                    if (!circuit.isEmpty() && !msisdn.isEmpty()) {
                        MSISDN_to_CIRCUIT_mapping.put(msisdn, circuit);
                    }
                });
        System.out.println("circuitToMSISDN_mapping = " + MSISDN_to_CIRCUIT_mapping.size());
        //System.out.println("MSISDN_to_CIRCUIT_mapping ="+MSISDN_to_CIRCUIT_mapping);
        //************************ HRS lines ***************************************************** 
        System.out.println("------------- HRS (Atlantis) lines -------------------");
        Map<String, List<CustomerEvent>> ATLANTIS_lines = Files.readAllLines(Paths.get(ATLANTIS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ATLANTIS_MSISDN_index ? s.split(SPLITTER)[ATLANTIS_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ATLANTIS_DATE_index ? s.split(SPLITTER)[ATLANTIS_DATE_index] : "";
                    //System.out.println("-> activationDate=" + activationDate+" S="+s);
                    String status = ATLANTIS_STATUS_index >= 0 && s.split(SPLITTER).length > ATLANTIS_STATUS_index ? s.split(SPLITTER)[ATLANTIS_STATUS_index] : "";
                    String blocked = s.split(SPLITTER).length > 15 ? s.split(SPLITTER)[15].trim() : "";
                    if (blocked.equals("1") && !msisdn.isEmpty()) {
                        blocked_numbers.add(msisdn);
                    }
                    return new CustomerEvent(msisdn, formatDate(activationDate), status, "ATLANTIS", 0.0);
                })
                .filter(validRow)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("ATLANTIS_lines = " + ATLANTIS_lines.size());
        //---
        Map<String, List<CustomerEvent>> ATLANTIS_2ndlines = Files.readAllLines(Paths.get(ATLANTIS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String secondMSISDN = ATLANTIS_MSISDN2_index >= 0 && s.split(SPLITTER).length > ATLANTIS_MSISDN2_index ? s.split(SPLITTER)[ATLANTIS_MSISDN2_index] : "";
                    String activationDate = s.split(SPLITTER).length > ATLANTIS_DATE_index ? s.split(SPLITTER)[ATLANTIS_DATE_index] : "";
                    String status = ATLANTIS_STATUS_index >= 0 && s.split(SPLITTER).length > ATLANTIS_STATUS_index ? s.split(SPLITTER)[ATLANTIS_STATUS_index] : "";
                    String blocked = s.split(SPLITTER).length > 15 ? s.split(SPLITTER)[15].trim() : "";
                    if (blocked.equals("1") && !secondMSISDN.isEmpty()) {
                        blocked_numbers.add(secondMSISDN);
                    }
                    return new CustomerEvent(secondMSISDN, formatDate(activationDate), status, "ATLANTIS_2ndlines", 0.0);
                })
                .filter(validRow)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("ATLANTIS_2ndlines = " + ATLANTIS_2ndlines.size());
        HRS_DB_Lines.putAll(ATLANTIS_2ndlines);
        HRS_DB_Lines.putAll(ATLANTIS_lines);
        System.out.println("ATLANTIS_lines total = " + ATLANTIS_lines.size());
        //--- secondary_numbers  ---
        secondary_numbers.addAll(ATLANTIS_2ndlines.keySet());
        //************************************
        System.out.println("------------- HRS Billing files----");
        HRS_BILLING_Lines = Files.readAllLines(Paths.get(HRS_BILLING_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > HRS_BILLING_MSISDN_index ? s.split(SPLITTER)[HRS_BILLING_MSISDN_index] : "";
                    msisdn = msisdn.replace("-OLD", "").replace("-old", "");
                    return new CustomerEvent(msisdn, "", "", "HRS_BILLING", 0.0);
                })
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("reading " + HRS_BILLING_filename + "... HRS_BILLING_Lines = " + HRS_BILLING_Lines.size());
        //
        //************************ Vodafone DB lines ***************************************************** 
        System.out.println("------------- Vodafon DB files -------------------");
        Map<String, List<CustomerEvent>> SB_HRS_lines = Files.readAllLines(Paths.get(SB_HRS_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > SB_HRS_MSISDN_index ? s.split(SPLITTER)[SB_HRS_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > SB_HRS_DATE_index ? s.split(SPLITTER)[SB_HRS_DATE_index] : "";
                    return new CustomerEvent(msisdn, formatDate(activationDate), "", "SB_HRS_lines", 0.0);
                })
                .filter(validRow)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("reading " + SB_HRS_filename + "... SB_HRS_lines = " + SB_HRS_lines.size());
        //
        elra_Prepay_lines = Files.readAllLines(Paths.get(ELRA_PREPAY_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ELRA_PREPAY_MSISDN_index ? s.split(SPLITTER)[ELRA_PREPAY_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ELRA_PREPAY_DATE_index ? s.split(SPLITTER)[ELRA_PREPAY_DATE_index] : "";
                    return new CustomerEvent(msisdn, formatDate(activationDate), "", "elra_Prepay_lines", 0.0);
                })
                .filter(validRow)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("elra_Prepay_lines = " + elra_Prepay_lines.size());
        //
        Map<String, List<CustomerEvent>> elra_lines = Files.readAllLines(Paths.get(ELRA_filename), CHARSET)
                .stream()
                .map(s -> {
                    String msisdn = s.split(SPLITTER).length > ELRA_MSISDN_index ? s.split(SPLITTER)[ELRA_MSISDN_index] : "";
                    String activationDate = s.split(SPLITTER).length > ELRA_DATE_index ? s.split(SPLITTER)[ELRA_DATE_index] : "";
                    return new CustomerEvent(msisdn, formatDate(activationDate), "", "elra_lines", 0.0);
                })
                .filter(validRow)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
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
                    try {
                        String msisdn = s.split(SPLITTER).length > VODAFONE_SPLIT_MSISDN_index ? s.split(SPLITTER)[VODAFONE_SPLIT_MSISDN_index] : "";
                        //System.out.println("VODAFONE_SPLIT_Lines line="+msisdn);                    
                        double value = Double.parseDouble(s.split(SPLITTER).length > VODAFONE_SPLIT_VALUE_index ? s.split(SPLITTER)[VODAFONE_SPLIT_VALUE_index].replace(".", "").replace(",", ".") : "-99999");
                        //System.out.println("value:"+value);
                        return new CustomerEvent(msisdn, "", "", "VODAFONE_SPLIT", value);
                    } catch (Exception e) {
                        System.out.println(e + " VODAFONE_SPLIT_VALUE_index:" + s.split(SPLITTER)[VODAFONE_SPLIT_VALUE_index]);
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_SPLIT_Lines = " + VODAFONE_SPLIT_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_PREPAY_Lines = Files.readAllLines(Paths.get(VODAFONE_PREPAY_filename), CHARSET)
                .stream()
                .map(s -> {
                    try {
                        String msisdn = s.split(SPLITTER).length > VODAFONE_PREPAY_MSISDN_index ? s.split(SPLITTER)[VODAFONE_PREPAY_MSISDN_index] : "";
                        //System.out.println("VODAFONE_PREPAY_Lines line="+msisdn);
                        double value = Double.parseDouble(s.split(SPLITTER).length > VODAFONE_PREPAY_VALUE_index ? s.split(SPLITTER)[VODAFONE_PREPAY_VALUE_index].replace(".", "").replace(",", ".") : "-99999");
                        return new CustomerEvent(msisdn, "", "", "VODAFONE_PREPAY", value);
                    } catch (Exception e) {
                        System.out.println(e + " VODAFONE_PREPAY_VALUE_index:" + s.split(SPLITTER)[VODAFONE_PREPAY_VALUE_index]);
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_PREPAY_Lines = " + VODAFONE_PREPAY_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_MOBILE_Lines = Files.readAllLines(Paths.get(VODAFONE_MOBILE_filename), CHARSET)
                .stream()
                .map(s -> {
                    try {
                        String msisdn = s.split(SPLITTER).length > VODAFONE_MOBILE_MSISDN_index ? s.split(SPLITTER)[VODAFONE_MOBILE_MSISDN_index] : "";
                        double value = Double.parseDouble(s.split(SPLITTER).length > VODAFONE_MOBILE_VALUE_index ? s.split(SPLITTER)[VODAFONE_MOBILE_VALUE_index].replace(".", "").replace(",", ".") : "-99999");
                        return new CustomerEvent(msisdn, "", "", "VODAFONE_MOBILE", value);
                    } catch (Exception e) {
                        System.out.println(e + " VODAFONE_MOBILE_VALUE:" + s.split(SPLITTER)[VODAFONE_MOBILE_VALUE_index] + "VODAFONE_MOBILE_filename:" + VODAFONE_MOBILE_filename + " VODAFONE_MOBILE_VALUE_index:" + VODAFONE_MOBILE_VALUE_index);
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_MOBILE_Lines = " + VODAFONE_MOBILE_Lines.size());
        //---
        Map<String, List<CustomerEvent>> VODAFONE_FIX_Lines = Files.readAllLines(Paths.get(VODAFONE_FIX_filename), CHARSET)
                .stream()
                .map(s -> {
                    try {
                        String msisdn = s.split(SPLITTER).length > VODAFONE_FIX_MSISDN_index ? s.split(SPLITTER)[VODAFONE_FIX_MSISDN_index] : "";
                        double value = Double.parseDouble(s.split(SPLITTER).length > VODAFONE_FIX_VALUE_index ? s.split(SPLITTER)[VODAFONE_FIX_VALUE_index].replace(".", "").replace(",", ".") : "-99999");
                        return new CustomerEvent(msisdn, "", "", "VODAFONE_FIX", value);
                    } catch (Exception e) {
                        System.out.println(e + " VODAFONE_FIX_VALUE_index:" + s.split(SPLITTER)[VODAFONE_FIX_VALUE_index]);
                        return null;
                    }
                })
                .filter(e -> e != null)
                .collect(Collectors.groupingBy(l -> l.getMSISDN()));
        System.out.println("VODAFONE_FIX_Lines = " + VODAFONE_FIX_Lines.size());
        //
        VODAFONE_BILLING_Lines.putAll(VODAFONE_SPLIT_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_PREPAY_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_MOBILE_Lines);
        VODAFONE_BILLING_Lines.putAll(VODAFONE_FIX_Lines);
    }

    public Map<String, List<CustomerEvent>> LEFT_only(Map<String, List<CustomerEvent>> M1, Map<String, List<CustomerEvent>> M2, Predicate<Entry<String, List<CustomerEvent>>> myFilter) throws IOException {
        return M1.entrySet().stream()
                .filter(myFilter)
                .filter(e -> {
                    String circuit = MSISDN_to_CIRCUIT_mapping.get(e.getKey());
                    //boolean res =!(M2.containsKey(e.getKey()) || (circuit!=null && M2.containsKey(circuit)) );
                    boolean res = M2.containsKey(e.getKey());
                    boolean res1 = M2.containsKey(circuit);
                    System.out.println(e.getKey() + " " + circuit + "->" + res + " " + res1);
                    return !(M2.containsKey(e.getKey()) || (circuit != null && M2.containsKey(circuit)));
                })
                .collect(Collectors.toMap((Entry e) -> (String) e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<CustomerEvent>> exist_RIGHT(Map<String, List<CustomerEvent>> M1, Map<String, List<CustomerEvent>> M2, Predicate<Entry<String, List<CustomerEvent>>> myFilter) throws IOException {
        return M1.entrySet().stream()
                .filter(myFilter)
                .filter(e -> {
                    String circuit = MSISDN_to_CIRCUIT_mapping.get(e.getKey());
                    return M2.containsKey(e.getKey()) || M2.containsKey(circuit);
                })
                .collect(Collectors.toMap((Entry e) -> (String) e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<CustomerEvent>> filter(Map<String, List<CustomerEvent>> M1, Predicate<Entry<String, List<CustomerEvent>>> myFilter) throws IOException {
        return M1.entrySet().stream()
                .filter(myFilter)
                .collect(Collectors.toMap((Entry e) -> (String) e.getKey(), e -> e.getValue()));
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        try {
            Long.valueOf(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        try {
            Double.valueOf(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void report_HRS_DB_YES_HRS_BILLING_NO(JspWriter out) throws IOException {
        //--- filter out zero values ---
        Map<String, List<CustomerEvent>> withBillingValue = filter(VODAFONE_BILLING_Lines, hasBillingValue);
        Map<String, List<CustomerEvent>> alsoInVodafonBlilling = exist_RIGHT(HRS_DB_Lines, withBillingValue, e -> true);
        Map<String, List<CustomerEvent>> mySet = LEFT_only(alsoInVodafonBlilling, HRS_BILLING_Lines, e -> !blocked_numbers.contains(e.getKey()) && !secondary_numbers.contains(e.getKey()));
        out.println("VODAFONE_BILLING_Lines" + VODAFONE_BILLING_Lines.size());
        out.println("withBillingValue" + withBillingValue.size());
        out.println("alsoInVodafonBlilling" + alsoInVodafonBlilling.size());
        out.println("blocked_number size = " + blocked_numbers.size());
        //---
        out.println("<h1> Numbers that exist in HRS Database but not in HRS Billing </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "HRS_DB records (Atlantis) : " + HRS_DB_Lines.size());
        out.println("<p>" + "HRS_BILLING records : " + HRS_BILLING_Lines.size());
        //---

        //--- filter out zero values ---
        //-------
        Map<String, Long> atlantis_statuses = mySet.values().stream().flatMap(v -> v.stream())
                .filter(myDBFilter)
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            try {
                out.println("<p>        " + e.getValue() + " found. Reason:" + e.getKey() + " - " + statuses.get(e.getKey()));
            } catch (IOException ex) {
            }
        });
        report(mySet, myDBFilter, out);
    }

    public void report_HRS_DB_NO_HRS_BILLING_YES(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> mySet = LEFT_only(HRS_BILLING_Lines, HRS_DB_Lines, e -> true);
        //---
        out.println("<h1> Numbers that exist in HRS Billing but not in HRS Database </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "HRS_DB records (Atlantis) : " + HRS_DB_Lines.size());
        out.println("<p>" + "HRS_BILLING records : " + HRS_BILLING_Lines.size());
        report(mySet, e -> true, out);
    }

    //--------------------------------
    public void report_VOD_DB_YES_VOD_BILLING_NO(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> res = LEFT_only(VODAFONE_DB_Lines, VODAFONE_BILLING_Lines, e -> true);
        Map<String, List<CustomerEvent>> mySetWithoutPrepay = LEFT_only(res, elra_Prepay_lines, e -> true);
        //---VODAFONE_DB_Lines, VODAFONE_BILLING_Lines
        out.println("<h1> Numbers that exist in Vodafon DB but not in Vodafon Billing</h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        //out.println("<p>" + "IGNORE_LIST:" + IGNORE_LIST);
        out.println("<p>" + "VODAFONE_DB records : " + VODAFONE_DB_Lines.size());
        out.println("<p>" + "VODAFONE_BILLING records : " + VODAFONE_BILLING_Lines.size());
        //---
        Map<String, Long> atlantis_statuses = mySetWithoutPrepay.values().stream()
                .flatMap(v -> v.stream())
                .filter(myDBFilter)
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            try {
                out.println("<p>        " + e.getValue() + " found. Reason:" + e.getKey() + " - " + statuses.get(e.getKey()));
            } catch (IOException ex) {
            }
        });
        report(mySetWithoutPrepay, myDBFilter, out);
    }

    public void report_VOD_DB_NO_VOD_BILLING_YES(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> mySet = LEFT_only(VODAFONE_BILLING_Lines, VODAFONE_DB_Lines, e -> true);
        //---VODAFONE_DB_Lines, VODAFONE_BILLING_Lines
        out.println("<h1> Numbers that exist in Vodafon Billing but not in Vodafon DB </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        //out.println("<p>" + "IGNORE_LIST:" + IGNORE_LIST);
        out.println("<p>" + "VODAFONE_DB records  : " + VODAFONE_DB_Lines.size());
        out.println("<p>" + "VODAFONE_BILLING records : " + VODAFONE_BILLING_Lines.size());
        report(mySet, e -> true, out);
    }

    //--------------------------------
    public void report_HRS_BILLING_YES_VOD_BILLING_NO(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> mySet = LEFT_only(HRS_BILLING_Lines, VODAFONE_BILLING_Lines, e -> true);
        out.println("<h1> Numbers that exist in HRS Billing but not in Vodafon Billing </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "VODAFONE_BILLING records : " + VODAFONE_BILLING_Lines.size());
        out.println("<p>" + "HRS_BILLING records : " + HRS_BILLING_Lines.size());
        report(mySet, e -> true, out);
    }

    public void report_HRS_BILLING_NO_VOD_BILLING_YES(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> withBillingValue = filter(VODAFONE_BILLING_Lines, hasBillingValue);
        Map<String, List<CustomerEvent>> mySet = LEFT_only(withBillingValue, HRS_BILLING_Lines, e -> !blocked_numbers.contains(e.getKey()) && !secondary_numbers.contains(e.getKey()));
        //----
        out.println("<h1> Numbers that exist in Vodafon Billing but not in HRS Billing </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "VODAFONE_BILLING records: " + VODAFONE_BILLING_Lines.size());
        out.println("<p>" + "HRS_BILLING records : " + HRS_BILLING_Lines.size());
        report(mySet, e -> true, out);
    }

    //---------------------------------------------
    public void report_HRS_DB_YES_VOD_DB_NO(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> mySet = LEFT_only(HRS_DB_Lines, VODAFONE_DB_Lines, e -> true);
        //---
        out.println("<h1> Numbers that exist in HRS DB but not in Vodafon DB </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        out.println("<p>" + "HRS_DB records (Atlantis) : " + HRS_DB_Lines.size());
        out.println("<p>" + "VODAFONE_DB records : " + VODAFONE_DB_Lines.size());
        //---
        Map<String, Long> atlantis_statuses = mySet.values().stream().flatMap(v -> v.stream())
                .filter(myDBFilter)
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            try {
                out.println("<p>        " + e.getValue() + " found. Reason:" + e.getKey() + " - " + statuses.get(e.getKey()));
            } catch (IOException ex) {
            }
        });
        report(mySet, myDBFilter, out);
    }

    public void report_HRS_DB_NO_VOD_DB_YES(JspWriter out) throws IOException {
        Map<String, List<CustomerEvent>> mySet = LEFT_only(VODAFONE_DB_Lines, HRS_DB_Lines, e -> true);

        //---
        out.println("<h1> Numbers that exist ONLY in Vodafon DB but not in HRS DB </h1>");
        out.println();
        out.println("<p>" + "*************** SUMMARY ***********");
        //out.println("<p>" + "IGNORE_LIST:" + IGNORE_LIST);
        out.println("<p>" + "HRS DB records : " + HRS_DB_Lines.size());
        out.println("<p>" + "VODAFONE DB records : " + VODAFONE_DB_Lines.size());
        report(mySet, e -> true, out);
    }

    public void report() throws IOException {

    }

    public void report(Map<String, List<CustomerEvent>> mySet, Predicate<CustomerEvent> myDBFilter, JspWriter out) throws IOException {
        long count = mySet.values().stream().flatMap(e -> e.stream())
                .filter(s -> isNumeric(s.getMSISDN()))
                .filter(myDBFilter)
                .distinct().collect(Collectors.counting());
        out.println("<h1> found " + count + " records</h1>");
        // ---
        out.println("<table>");
        out.println("<tr><th>MSISDN</th><th>status</th><th>source file</th></tr>");
        mySet.values().stream().flatMap(e -> e.stream())
                .filter(s -> isNumeric(s.getMSISDN()))
                .filter(myDBFilter)
                .distinct()
                .sorted(Comparator.comparing(s -> s.getStatus()))
                .forEach(s -> {
                    try {
                        //String activationDate = s.getActivationDate().isEmpty() ? s.getActivationDate() : formatDateAsNowFormated(s.getActivationDate());
                        out.println("<tr><td>" + s.getMSISDN().replace("-OLD", "").replace("-old", "") + "</td><td>" + s.getStatus() + "</td><td>" + s.getInfo() + "</td></tr>");
                    } catch (IOException ex) {
                    }
                });
        out.println("<table>");
    }

    private static String formatDateAsNowFormated(String mydate) {
        try {
            return new TimeStamp1(mydate).getTodayFormated();
        } catch (Exception e) {
            return mydate;
        }

    }

    private static String formatDate(String activationDate) {
        // 16/12/2022 10:48
        String activationDateFormatted = "";
        try {
            activationDateFormatted = activationDate.split(" ")[0];
            String[] parts = activationDateFormatted.split("/");
            String year = parts[2];
            if (year.length() == 2) {
                year = "20" + year;
            }
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
        try {
            //-----------------------
            myProperties.load(new FileReader(new File("C:\\myfiles\\data\\HRSTools\\data\\ΔΕΚ-23\\parameters.properties"), StandardCharsets.UTF_8));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileComparator_billing myFileComparator = new FileComparator_billing(myProperties);
        try {
            myFileComparator.loadFiles();
            //myFileComparator.report_VOD_DB_NO_VOD_BILLING_YES();
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
