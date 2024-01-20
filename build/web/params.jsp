<%-- 
    Document   : params
    Created on : Jan 20, 2024, 3:44:12 PM
    Author     : gsofi
--%>

<%@page import="java.io.FileWriter"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setCharacterEncoding("utf8");
    final JspWriter out1 = out;
    Map<String, String[]> params = request.getParameterMap();
//--
    params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
            .forEach(entry -> {
                try {
                    // out1.println("<p>" + entry.getKey() + " = " + entry.getValue()[0]);
                } catch (Exception e) {
                }
            });
    //
    if (request.getParameter("propertiesFile") != null) {
        out1.println("<p>propertiesFile = " + request.getParameter("propertiesFile"));
    }
    if (request.getParameter("compare") != null) {
        Properties myProperties = new Properties();
        params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
                .forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
        FileWriter myFileWriter = new FileWriter("C:\\myfiles\\data\\HRSTools\\conf\\params_.properties");
        myProperties.store(myFileWriter, "utf-8");
        myFileWriter.close();
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="params.jsp" method="POST" id="myForm">
            <input type="submit" name=compare" value="Compare" />

            <input type="button" id="loadFileXml" value="Load parameters from file" onclick="document.getElementById('propertiesFile').click();" />
            <input type="file" onchange="document.getElementById('myForm').submit();" style="display:none;" id="propertiesFile" accept=".properties" name="propertiesFile"/>

            <table>
                <tr><td>
                        <p>CHARSET  : <input type="text" size="10" name="CHARSET" value="ISO-8859-7" />
                        <p>SPLITTER  : <input type="text" size="1" name="SPLITTER" size="3" value=";" />
                        <p>MAX_ACTIVATION_DATE  : <input type="text" size="2" name="MAX_ACTIVATION_DATE" value="" />
                        <p>List ignoreList  : <input type="text" size ="60" name="ATLANTIS_MSISDN_index" value="13,6,69,72,23,75,57,4,33,24,20,19,76" />
                    </td></tr><tr><td>
                        <p><h1> HRS Database files</h1>                
                        <p><h3>ATLANTIS_filename : <input type="file" accept=".csv" accept=".csv" name="ATLANTIS_filename" /></h3>
                        <p>ATLANTIS_MSISDN_index : <input type="text" size="2" size="2" name="ATLANTIS_MSISDN_index" value="" />
                        <p>ATLANTIS_MSISDN2_index  : <input type="text" size="2" name="ATLANTIS_MSISDN2_index" value="" />
                        <p>ATLANTIS_DATE_index : <input type="text" size="2" name="ATLANTIS_DATE_index" value="" />
                        <p>ATLANTIS_STATUS_index  : <input type="text" size="2" name="ATLANTIS_STATUS_index" value="" />
                    </td></tr><tr><td>
                        <h1><h3> Vodafon database SB_HRS</h1>
                        <p><h3>SB_HRS_filename : <input type="file" accept=".csv" name="SB_HRS_filename" /></h3>
                        <p>SB_HRS_MSISDN_index : <input type="text" size="2" name="SB_HRS_MSISDN_index" value="" />
                        <p>SB_HRS_DATE_index  : <input type="text" size="2" name="SB_HRS_DATE_index" value="" />
                    </td></tr><tr><td>
                        <h1> Vodafon database ELRA</h1>
                        <p><h3>ELRA_filename  : <input type="file" accept=".csv" name="ELRA_filename" /></h3>
                        <p>ELRA_MSISDN_index : <input type="text" size="2" name="ELRA_MSISDN_index" value="" />
                        <p>ELRA_DATE_index  : <input type="text" size="2" name="ELRA_DATE_index" value="" />
                    </td></tr><tr><td>
                        <h1> Vodafon database ELRA_PREPAY</h1>
                        <p><h3>ELRA_PREPAY_filename : <input type="file" accept=".csv" name="ELRA_PREPAY_filename" /></h3>
                        <p>ELRA_PREPAY_MSISDN_index  : <input type="text" size="2" name="ELRA_PREPAY_MSISDN_index" value="" />
                        <p>ELRA_PREPAY_DATE_index  : <input type="text" size="2" name="ELRA_PREPAY_DATE_index" value="" />
                    </td></tr><tr><td>
                        <h1> Vodafon billing SPLIT</h1>
                        <p>VODAFONE_SPLIT_filename : <input type="file" accept=".csv" name="VODAFONE_SPLIT_filename" /></h3>
                        <p>VODAFONE_SPLIT_MSISDN_index : <input type="text" size="2" name="VODAFONE_SPLIT_MSISDN_index" value=""/>
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΚΑΡΤΟΚΙΝΗΤΗ</h1>
                        <p><h3>VODAFONE_PREPAY_filename  : <input type="file" accept=".csv" name="VODAFONE_PREPAY_filename" /></h3>
                        <p>VODAFONE_PREPAY_MSISDN_index  : <input type="text" size="2" name="VODAFONE_PREPAY_MSISDN_index" value="" />
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΚΙΝΗΤΗ</h1>
                        <p><h3>VODAFONE_MOBILE_filename  : <input type="file" accept=".csv" name="VODAFONE_MOBILE_filename" /></h3>
                        <p>VODAFONE_MOBILE_MSISDN_index  : <input type="text" size="2" name="VODAFONE_MOBILE_MSISDN_index" value="" />
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΣΤΑΘΕΡΗ</h1>
                        <p><h3>VODAFONE_FIX_filename  : <input type="file" accept=".csv" name="VODAFONE_FIX_filename" /></h3>
                        <p>VODAFONE_FIX_MSISDN_index  : <input type="text" size="2" name="VODAFONE_FIX_MSISDN_index" value="" />
                        <p>VODAFONE_FIX_CIRCUIT_index  : <input type="text" size="2" name="VODAFONE_FIX_CIRCUIT_index" value="" />       
                    </td></tr>
            </table>


        </form>

    </body>
</html>


<!--
        #
    CHARSET 
    SPLITTER = 
    MAX_ACTIVATION_DATE 
    #
    List ignoreList 
    # HRS files</h1>
    ATLANTIS_filename 
    ATLANTIS_MSISDN_index 
    ATLANTIS_MSISDN2_index 
    ATLANTIS_DATE_index
    ATLANTIS_STATUS_index 
    #-------- Vodafon files
    SB_HRS_filename 
    SB_HRS_MSISDN_index
    SB_HRS_DATE_index 
    #
    ELRA_filename 
    ELRA_MSISDN_index
    ELRA_DATE_index 
    #
    ELRA_PREPAY_filename
    ELRA_PREPAY_MSISDN_index 
    ELRA_PREPAY_DATE_index 
        #
    VODAFONE_SPLIT_filename
    VODAFONE_SPLIT_MSISDN_index
    #
    VODAFONE_PREPAY_filename 
    VODAFONE_PREPAY_MSISDN_index 
    #
    VODAFONE_MOBILE_filename 
    VODAFONE_MOBILE_MSISDN_index 
    #
    VODAFONE_FIX_filename 
    VODAFONE_FIX_MSISDN_index 
    VODAFONE_FIX_CIRCUIT_index 


-->
