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
import model.CustomerEvent;

/**
 *
 * @author nsofias
 */
public class FileComparator {

    Charset charset = StandardCharsets.ISO_8859_1;
    String HRS_BASE_filename;
    int HRS_BASE_index = 1;
    int HRS_BASE_activationDate_index = 6;
    int HRS_BASE_status_index = 5;
    //
    String ELRA_POST_filename;
    int ELRA_POST_index = 1;
    int ELRA_POST_activationDate_index = 4;
    int ELRA_POST_status_index = 5;
    //
    String SB_HRS_filename;
    int SB_HRS_index = 1;
    int SB_HRS_activationDate_index = 3;
    int SB_HRS_status_index = -1;
    Predicate<CustomerEvent> validRow = s -> isNumeric(s.getMSISDN());
    Predicate<CustomerEvent> invalidRow = (CustomerEvent s) -> !isNumeric(s.getMSISDN());
    private Map<String, List<CustomerEvent>> base_Lines;
    private Map<String, List<CustomerEvent>> ELRA_POST_Lines;
    private Map<String, List<CustomerEvent>> SB_HRS_Lines;

    public FileComparator(String base, int base_index, String ELRA_POST, int ELRA_POST_index, String SB_HRS, int SB_HRS_index, Charset charset) {
        this.HRS_BASE_filename = base;
        this.HRS_BASE_index = base_index;

        this.ELRA_POST_filename = ELRA_POST;
        this.ELRA_POST_index = ELRA_POST_index;
        this.SB_HRS_filename = SB_HRS;
        this.SB_HRS_index = SB_HRS_index;
        this.charset = charset;
    }

    public void loadFiles() throws IOException {
        base_Lines = Files.readAllLines(Paths.get(HRS_BASE_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(";").length > HRS_BASE_index ? s.split(";")[HRS_BASE_index] : "";
                    String activationDate = s.split(";").length > HRS_BASE_activationDate_index ? s.split(";")[HRS_BASE_activationDate_index] : "";
                    String status = s.split(";").length > HRS_BASE_status_index ? s.split(";")[HRS_BASE_status_index] : "";
                    return new CustomerEvent(msisdn, activationDate, status, s);
                })
                .filter(validRow).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        //---
        ELRA_POST_Lines = Files.readAllLines(Paths.get(ELRA_POST_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(";").length > ELRA_POST_index ? s.split(";")[ELRA_POST_index] : "";
                    String activationDate = s.split(";").length > ELRA_POST_activationDate_index ? s.split(";")[ELRA_POST_activationDate_index] : "";
                    String status = s.split(";").length > ELRA_POST_status_index ? s.split(";")[ELRA_POST_status_index] : "";
                    return new CustomerEvent(msisdn, activationDate, status, s);
                })
                .filter(validRow).collect(Collectors.groupingBy(l -> l.getMSISDN()));
        //---
        SB_HRS_Lines = Files.readAllLines(Paths.get(SB_HRS_filename), charset)
                .stream()
                .map(s -> {
                    String msisdn = s.split(";").length > SB_HRS_index ? s.split(";")[SB_HRS_index] : "";
                    String activationDate = s.split(";").length > SB_HRS_activationDate_index ? s.split(";")[SB_HRS_activationDate_index] : "";
                    //String status = s.split(";").length > SB_HRS_status_index ? s.split(";")[SB_HRS_status_index] : "";
                    return new CustomerEvent(msisdn, activationDate, "", s);
                })
                .filter(validRow).collect(Collectors.groupingBy(l -> l.getMSISDN()));
    }

    /*
    HRS_BASE_filename: 
        Ημερ. Ενεργ.	Νούμερο	Κωδ.Status Συν.	Status Συνδεσης	Ημερ. Έναρξης	Μητρώο Split Biller	Μητρώο VOD
    ELRA_POST_1-9-2023:
        Tariff Group	MSISDN	SUBSCRIBER_ID	TARIFF_PLAN	ACTIVATION_DATE	GPRS_SERVICE_LEVEL	LAST_RENEWAL_DATE	PRIMARY_MSISDN
    SB_HRS_September_2023:
        Μητρώο	MSISDN	Πρόγραμμα Χρήσης	Connection Date	Agreement Date	Contract Duration        
     */
    public List<CustomerEvent> getInvalidBaseRows() throws IOException {
        List<CustomerEvent> bad_base_Lines = base_Lines.values()
                .stream() 
                .flatMap(l->l.stream())
                .filter(invalidRow)
                // .peek(s->System.out.println("invalid ---->"+s))
                .collect(toList());
        return bad_base_Lines;
    }

    public Map<String, List<CustomerEvent>> baseOnly() throws IOException {
        return getBase_Lines().entrySet().stream()
                .filter(e -> !ELRA_POST_Lines.containsKey(e.getKey()) && !SB_HRS_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<CustomerEvent>> ELRA_POST_only() throws IOException {
        return getELRA_POST_Lines().entrySet().stream()
                .filter(e -> !base_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Map<String, List<CustomerEvent>> ESB_HRS_only() throws IOException {
        return getSB_HRS_Lines().entrySet().stream()
                .filter(e -> !base_Lines.containsKey(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
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
        List<CustomerEvent> myInvalidRows = getInvalidBaseRows();
        Map<String, List<CustomerEvent>> hrsOnly = baseOnly();
        Map<String, List<CustomerEvent>> VOD_ELRA_POST_only = ELRA_POST_only();
        Map<String, List<CustomerEvent>> VOD_ESB_HRS_only = ESB_HRS_only();
        //---
        System.out.println();
        System.out.println("*************** SUMMARY ***********");
        System.out.println("\nATLANTIS Numbers found: " + getBase_Lines().size());
        System.out.println("\nELRA_POST Numbers found: " + getELRA_POST_Lines().size());
        System.out.println("\nSB_HRS Numbers found: " + getSB_HRS_Lines().size());
        //---
        System.out.println("\nINVALID ATLANTIS rows found: " + myInvalidRows.size());
        System.out.println("\nNumbers that exist ONLY in ATLANTIS (not in Vodafone), found: " + hrsOnly.size());
        System.out.println("\nReasons for  Numbers that exist ONLY in ATLANTIS database (not in Vodafone):");
        Map<String, Long> atlantis_statuses = hrsOnly.values().stream().flatMap(v -> v.stream())
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        atlantis_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey());
        });
        System.out.println("\nReasons for Numbers that exist ONLY in VOD_ELRA_POST (not in ATLANTIS), found: " + VOD_ELRA_POST_only.size());
        Map<String, Long> VOD_ELRA_statuses = VOD_ELRA_POST_only.values().stream().flatMap(v -> v.stream())
                .collect(Collectors.groupingBy(s -> s.getStatus(), Collectors.counting()));
        VOD_ELRA_statuses.entrySet().stream().sorted(comparing(e -> -e.getValue())).forEach(e -> {
            System.out.println("        " + e.getValue() + " found. Reason:" + e.getKey());
        });
        System.out.println("\nReasons for Numbers that exist ONLY in VOD_ESB_HRS (not in ATLANTIS), found: " + VOD_ESB_HRS_only.size());
        Map<String, Long> VOD_ESB_HRS_statuses = VOD_ESB_HRS_only.values().stream().flatMap(v -> v.stream())
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
        hrsOnly.values().stream().flatMap(v -> v.stream()).sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> {
            System.out.println("  ONLY in HRS: " + s.getMSISDN() + " : " + s);
        });
        //----------
        System.out.println("\n\n*** Numbers that exist ONLY in VOD_ELRA_POST (not in ATLANTIS): " + VOD_ELRA_POST_only.size());
        VOD_ELRA_POST_only.values().stream().flatMap(v -> v.stream()).sorted(Comparator.comparing(s -> s.getStatus())).forEach(s -> System.out.println(" VOD_ELRA_POST_only-> " + s.getMSISDN() + " : " + s));
        System.out.println("\n\n*** Numbers that exist ONLY in VOD_ESB_HRS (not in Atlanis): " + VOD_ESB_HRS_only.size());
        VOD_ESB_HRS_only.values().stream().flatMap(v -> v.stream()).forEach(s -> System.out.println(" VOD_ESB_HRS_only-> " + s.getMSISDN() + " :" + s));
    }

    public static void main(String[] args) {
        System.out.println(isNumeric(""));
        String base = "C:\\myfiles\\data\\HRSTools\\October_2023\\HRS_BASE.csv";
        String vod_1 = "C:\\myfiles\\data\\HRSTools\\October_2023\\VODAFONE ELRA_MOB.csv";
        String vod_2 = "C:\\myfiles\\data\\HRSTools\\October_2023\\VODAFONE SB_HRS.csv";
        FileComparator myFileComparator = new FileComparator(base, 1, vod_1, 1, vod_2, 1, StandardCharsets.UTF_8);
        try {
            myFileComparator.loadFiles();
            myFileComparator.report();
        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
