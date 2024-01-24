<%-- 
    Document   : params
    Created on : Jan 20, 2024, 3:44:12 PM
    Author     : gsofi
--%>

<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.io.IOException"%>
<%@page import="tools.FileComparator_billing"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.io.FileFilter"%>
<%@page import="nsofiasLib.fileIO.JFileFilter"%>
<%@page import="java.io.FilenameFilter"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            final JspWriter out1 = out;
            Map<String, String[]> params = request.getParameterMap();
            Properties myProperties = new Properties();
            String main_dir = "C:\\myfiles\\data\\HRSTools\\data\\";
            //--
            params.entrySet().stream().filter(entry -> !entry.getKey().equals("save"))
                    .forEach(entry -> {
                        try {
                            //out1.println("<p>" + entry.getKey() + " = " + entry.getValue()[0]);
                        } catch (Exception e) {
                        }
                    });
            //
            if (request.getParameter("directory") == null) {
                File dir = new File(main_dir);
                List<String> filenames = Arrays.asList(dir.list());
        %>            
        <form action="params.jsp" method="GET" id="myForm" enctype="multipart/form-data" accept-charset="UTF-8">
            Files Directory for files:
            <select onchange = "form.submit()"name="directory">
                out.println("<option></option>");
                <%
                    for (String filename : filenames) {
                        out.println("<option>" + filename + "</option>");
                    }
                %>
            </select>
        </form>
        <%
                return;
            }
            //---------- dir is not null ----------
            String directory = request.getParameter("directory");
            out.println("<a href='javascript:history.back()'>Go Back</a>");

            if (request.getParameter("compare_or_save") != null) {
                params.entrySet().stream().forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
                FileComparator_billing myFileComparator = new FileComparator_billing(myProperties);
                try {
                    myFileComparator.loadFiles();
                } catch (IOException ex) {
                    out.println(ex);
                }
                //-------------- compare -----------------
                if (request.getParameter("report_HRS_DB_YES_HRS_BILLING_NO") != null) {
                    myFileComparator.report_HRS_DB_YES_HRS_BILLING_NO(out);
                    return;
                } else if (request.getParameter("report_HRS_DB_NO_HRS_BILLING_YES") != null) {
                    myFileComparator.report_HRS_DB_NO_HRS_BILLING_YES(out);
                    return;
                } else if (request.getParameter("report_VOD_DB_YES_VOD_BILLING_NO") != null) {
                    myFileComparator.report_VOD_DB_YES_VOD_BILLING_NO(out);
                    return;
                } else if (request.getParameter("report_VOD_DB_NO_VOD_BILLING_YES") != null) {
                    myFileComparator.report_VOD_DB_NO_VOD_BILLING_YES(out);
                    return;

                } else if (request.getParameter("report_HRS_BILLING_YES_VOD_BILLING_NO") != null) {
                    myFileComparator.report_HRS_BILLING_YES_VOD_BILLING_NO(out);
                    return;

                } else if (request.getParameter("report_HRS_BILLING_NO_VOD_BILLING_YES") != null) {
                    myFileComparator.report_HRS_BILLING_NO_VOD_BILLING_YES(out);
                    return;

                } else if (request.getParameter("report_HRS_DB_YES_VOD_DB_NO") != null) {
                    myFileComparator.report_HRS_DB_YES_VOD_DB_NO(out);
                    return;

                } else if (request.getParameter("report_HRS_DB_NO_VOD_DB_YES") != null) {
                    myFileComparator.report_HRS_DB_NO_VOD_DB_YES(out);
                    return;
                }
                //---------save-----------
                if (request.getParameter("save") != null) {
                    params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
                            .forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
                    FileWriter myFileWriter = new FileWriter(main_dir + directory + "\\parameters.properties", StandardCharsets.UTF_8);
                    out.println("saved to:" + main_dir + directory + "\\parameters.properties");
                    myProperties.store(myFileWriter, "");
                }
            }
            //-- read csv files of directory 
            File dir = new File(main_dir + directory);

            FilenameFilter filter = ( d,                       name) -> name.endsWith(".csv");
            List<String> filenames = Arrays.asList(dir.list(filter)).stream().map(s -> main_dir + directory + "\\" + s).collect(Collectors.toList());

            // -- read local properties ---------
            File paramsFile = new File(main_dir + directory + "\\parameters.properties");
            if (paramsFile.exists()) {
                myProperties.load(new FileReader(paramsFile, StandardCharsets.UTF_8));
            } else {
                myProperties.load(new FileReader("C:\\myfiles\\data\\HRSTools\\conf\\parameters.properties", StandardCharsets.UTF_8));
            }
            if (request.getParameter("propertiesFile") != null) {
                out1.println("<p>propertiesFile = " + request.getParameter("propertiesFile"));
                myProperties.load(new FileReader("propertiesFile"));
            }


        %>
        <h1>directory: <%=directory%></h1>
        <form action="params.jsp" method="POST" id="myForm" >
            <p><input type="submit" name="report_HRS_DB_YES_HRS_BILLING_NO" value="Only in HRS Database not in HRS Billing" />
                <input type="submit" name="report_HRS_DB_NO_HRS_BILLING_YES" value="Only in HRS Billing not in HRS Database" />
            <p><input type="submit" name="report_VOD_DB_YES_VOD_BILLING_NO" value="Only in Vodafon DB not in Vodafon Billing" />
                <input type="submit" name="report_VOD_DB_NO_VOD_BILLING_YES" value="Only in Vodafon Billing not in Vodafon DB" />
            <p><input type="submit" name="report_HRS_BILLING_YES_VOD_BILLING_NO" value="Only in HRS Billing not in Vodafon Billing" />
                <input type="submit" name="report_HRS_BILLING_NO_VOD_BILLING_YES" value="Only in Vodafon Billing not in HRS Billing" />
            <p><input type="submit" name="report_HRS_DB_YES_VOD_DB_NO" value="Only in HRS DB not in Vodafon DB" />
                <input type="submit" name="report_HRS_DB_NO_VOD_DB_YES" value="Only in Vodafon DB not in HRS DB" />
            <p><input type="submit" name="save" value="Save parameters" />
                <input type="hidden" name="directory" value="<%=directory%>" />
                <input type="hidden" name="compare_or_save" value="compare_or_save" />
            <table>
                <tr><td>
                        <p>CHARSET  : <input type="text" size="10" name="CHARSET" value="<%=myProperties.getProperty("CHARSET")%>" />
                        <p>SPLITTER  : <input type="text" size="1" name="SPLITTER" size="3" value="<%=myProperties.getProperty("SPLITTER")%>" />
                        <p>MAX_ACTIVATION_DATE  : <input type="text" size="30" name="MAX_ACTIVATION_DATE" value="<%=myProperties.getProperty("MAX_ACTIVATION_DATE")%>" />
                        <p>List IGNORE_LIST  : <input type="text" size ="60" name="IGNORE_LIST" value="<%=myProperties.getProperty("IGNORE_LIST")%>" />
                    </td></tr><tr><td>
                        <p><h1> HRS files</h1>                
                        <b>ATLANTIS_DATABASE_filename : </b>                        
                        <select name="ATLANTIS_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("ATLANTIS_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                      
                        <p>ATLANTIS_MSISDN_index : <input type="text" size="2" size="2" name="ATLANTIS_MSISDN_index" value="<%=myProperties.getProperty("ATLANTIS_MSISDN_index")%>" />
                        <p>ATLANTIS_MSISDN2_index  : <input type="text" size="2" name="ATLANTIS_MSISDN2_index" value="<%=myProperties.getProperty("ATLANTIS_MSISDN2_index")%>" />
                        <p>ATLANTIS_DATE_index : <input type="text" size="2" name="ATLANTIS_DATE_index" value="<%=myProperties.getProperty("ATLANTIS_DATE_index")%>" />
                        <p>ATLANTIS_STATUS_index  : <input type="text" size="2" name="ATLANTIS_STATUS_index" value="<%=myProperties.getProperty("ATLANTIS_STATUS_index")%>" />


                        <P><b>ATLANTIS_BILLING_filename : </b>                        
                            <select name="HRS_BILLING_filename">    
                                <%
                                    out.println("<option>" + myProperties.getProperty("HRS_BILLING_filename") + "</option>");
                                    for (String filename : filenames) {
                                        out.println("<option value='" + filename + "'>" + filename + "</option>");
                                    }
                                %>
                            </select>                      
                        <p>HRS_BILLING_MSISDN_index : <input type="text" size="2" size="2" name="HRS_BILLING_MSISDN_index" value="<%=myProperties.getProperty("HRS_BILLING_MSISDN_index")%>" />


                    </td></tr><tr><td>
                        <!----------------- VODAFON DATABASE ------------------------------------------->
                        <h1>VODAFON DATABASE</h1> 
                        <b>SB_HRS_filename : </b>       
                        <select name="SB_HRS_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("SB_HRS_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                         
                        <p>SB_HRS_MSISDN_index : <input type="text" size="2" name="SB_HRS_MSISDN_index" value="<%=myProperties.getProperty("SB_HRS_MSISDN_index")%>" />
                        <p>SB_HRS_DATE_index  : <input type="text" size="2" name="SB_HRS_DATE_index" value="<%=myProperties.getProperty("SB_HRS_DATE_index")%>" />
                    </td></tr><tr><td>
                        <b>ELRA_filename : </b>                        
                        <select name="ELRA_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("ELRA_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                         
                        <p>ELRA_MSISDN_index : <input type="text" size="2" name="ELRA_MSISDN_index" value="<%=myProperties.getProperty("ELRA_MSISDN_index")%>" />
                        <p>ELRA_DATE_index  : <input type="text" size="2" name="ELRA_DATE_index" value="<%=myProperties.getProperty("ELRA_DATE_index")%>" />
                    </td></tr><tr><td>
                        <b>ELRA_PREPAY_filename : </b>                               
                        <select name="ELRA_PREPAY_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("ELRA_PREPAY_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>ELRA_PREPAY_MSISDN_index  : <input type="text" size="2" name="ELRA_PREPAY_MSISDN_index" value="<%=myProperties.getProperty("ELRA_PREPAY_MSISDN_index")%>" />
                        <p>ELRA_PREPAY_DATE_index  : <input type="text" size="2" name="ELRA_PREPAY_DATE_index" value="<%=myProperties.getProperty("ELRA_PREPAY_DATE_index")%>" />
                    </td></tr><tr><td>
                        <!----------------- BILLING ------------------------------------------->
                        <h1> VODAFON BILLING</h1>   
                        <b>SPLIT_filename : </b>      
                        <select name="VODAFONE_SPLIT_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_SPLIT_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>SPLIT_MSISDN_index : <input type="text" size="2" name="VODAFONE_SPLIT_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_SPLIT_MSISDN_index")%>"/>
                    </td></tr><tr><td>
                        <b>PREPAY_filename : </b>                        
                        <select name="VODAFONE_PREPAY_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_PREPAY_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>PREPAY_MSISDN_index  : <input type="text" size="2" name="VODAFONE_PREPAY_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_PREPAY_MSISDN_index")%>" />
                    </td></tr><tr><td>
                        <b>MOBILE_filename : </b>                         
                        <select name="VODAFONE_MOBILE_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_MOBILE_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>MOBILE_MSISDN_index  : <input type="text" size="2" name="VODAFONE_MOBILE_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_MOBILE_MSISDN_index")%>" />
                    </td></tr><tr><td>                     
                        <b>FIX_filename : </b>
                        <select name="VODAFONE_FIX_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_FIX_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>FIX_MSISDN_index  : <input type="text" size="2" name="VODAFONE_FIX_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_FIX_MSISDN_index")%>" />
                        <p>FIX_CIRCUIT_index  : <input type="text" size="2" name="<%=FileComparator_billing.FileComparator_VODAFONE_FIX_ERP_index%>" value="<%=myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_ERP_index)%>" />   
                        <p>FIX_CIRCUIT_index  : <input type="text" size="2" name="<%=FileComparator_billing.FileComparator_VODAFONE_FIX_CIRCUIT_index%>" value="<%=myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_CIRCUIT_index)%>" />       
                    </td></tr>
            </table>
        </form>
    </body>
</html>


<!--

-->
