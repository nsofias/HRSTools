package tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import model.Customer;

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
    public static List<String[]> baseOnly(String base, int base_ndex, String vod_1, int vod_1_index,String vod_2, int vod_2_index) throws IOException {
        Map<String, String[]> base_Lines = Files.readAllLines(Paths.get(base), StandardCharsets.ISO_8859_1)
                .stream().filter(s->s.split(";").length>1).collect(Collectors.toMap(l -> l.split(";")[1], l -> l.split(";")));
        Map<String, String[]> vod_1_Lines = Files.readAllLines(Paths.get(vod_1), StandardCharsets.ISO_8859_1)
                .stream().filter(s->s.split(";").length>1).collect(Collectors.toMap(l -> l.split(";")[1], l -> l.split(";")));
        Map<String, String[]> vod_2_Lines = Files.readAllLines(Paths.get(vod_2), StandardCharsets.ISO_8859_1)
                .stream().filter(s->s.split(";").length>1).collect(Collectors.toMap(l -> l.split(";")[1], l -> l.split(";")));
        return base_Lines.entrySet().stream().filter(e -> !vod_1_Lines.containsKey(e.getKey())&&!vod_2_Lines.containsKey(e.getKey())).map(e -> e.getValue()).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String base = "C:\\myFiles\\data\\access7\\HRSTools\\data\\base.csv";
        String vod_1 = "C:\\myFiles\\data\\access7\\HRSTools\\data\\ELRA_POST_1-9-2023.csv";
        String vod_2 = "C:\\myFiles\\data\\access7\\HRSTools\\data\\SB_HRS_September_2023.csv";
        try {
            List<String[]> res = FileComparator.baseOnly(base,1,vod_1,1,vod_2,1);
            System.out.println(res);
        } catch (IOException ex) {
            Logger.getLogger(FileComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
