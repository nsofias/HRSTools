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
            //-------------- compare -----------------
            if (request.getParameter("compare") != null) {
                params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
                        .forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
                FileWriter myFileWriter = new FileWriter(main_dir + directory + "\\parameters.properties", StandardCharsets.UTF_8);
                myProperties.store(myFileWriter, "");
                //**************************
                FileComparator_billing myFileComparator = new FileComparator_billing(myProperties);
                try {
                    myFileComparator.loadFiles();
                    myFileComparator.report(out);
                } catch (IOException ex) {
                    Logger.getLogger(FileComparator_billing.class.getName()).log(Level.SEVERE, null, ex);
                }
                //****************************
                return;
            }
            if (request.getParameter("save") != null) {
                params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
                        .forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
                FileWriter myFileWriter = new FileWriter(main_dir + directory + "\\parameters.properties", StandardCharsets.UTF_8);
                out.println("saved to:" + main_dir + directory + "\\parameters.properties");
                myProperties.store(myFileWriter, "");
            }
            //-- read csv files of directory 
            File dir = new File(main_dir + directory);

            FilenameFilter filter = ( d,   name) -> name.endsWith(".csv");
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
            <input type="submit" name="compare" value="Compare" />
            <input type="submit" name="save" value="Save parameters" />
            <input type="hidden" name="directory" value="<%=directory%>" />
            <table>
                <tr><td>
                        <p>CHARSET  : <input type="text" size="10" name="CHARSET" value="<%=myProperties.getProperty("CHARSET")%>" />
                        <p>SPLITTER  : <input type="text" size="1" name="SPLITTER" size="3" value="<%=myProperties.getProperty("SPLITTER")%>" />
                        <p>MAX_ACTIVATION_DATE  : <input type="text" size="30" name="MAX_ACTIVATION_DATE" value="<%=myProperties.getProperty("MAX_ACTIVATION_DATE")%>" />
                        <p>List IGNORE_LIST  : <input type="text" size ="60" name="IGNORE_LIST" value="<%=myProperties.getProperty("IGNORE_LIST")%>" />
                    </td></tr><tr><td>
                        <p><h1> HRS Database files</h1>                
                        <p><b>ATLANTIS_filename : </b>                        
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
                    </td></tr><tr><td>
                        <h1><h3> Vodafon database SB_HRS</h1>                        
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
                        <h1> Vodafon database ELRA</h1>                        
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
                        <h1> Vodafon database ELRA_PREPAY</h1>                        
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
                        <h1> Vodafon billing SPLIT</h1>                        
                        <select name="VODAFONE_SPLIT_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_SPLIT_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>VODAFONE_SPLIT_MSISDN_index : <input type="text" size="2" name="VODAFONE_SPLIT_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_SPLIT_MSISDN_index")%>"/>
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΚΑΡΤΟΚΙΝΗΤΗ</h1>                        
                        <select name="VODAFONE_PREPAY_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_PREPAY_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>VODAFONE_PREPAY_MSISDN_index  : <input type="text" size="2" name="VODAFONE_PREPAY_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_PREPAY_MSISDN_index")%>" />
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΚΙΝΗΤΗ</h1>                        
                        <select name="VODAFONE_MOBILE_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_MOBILE_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>VODAFONE_MOBILE_MSISDN_index  : <input type="text" size="2" name="VODAFONE_MOBILE_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_MOBILE_MSISDN_index")%>" />
                    </td></tr><tr><td>
                        <h1> Vodafon billing ΣΤΑΘΕΡΗ</h1>                        
                        <select name="VODAFONE_FIX_filename">    
                            <%
                                out.println("<option>" + myProperties.getProperty("VODAFONE_FIX_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            %>
                        </select>                        
                        <p>VODAFONE_FIX_MSISDN_index  : <input type="text" size="2" name="VODAFONE_FIX_MSISDN_index" value="<%=myProperties.getProperty("VODAFONE_FIX_MSISDN_index")%>" />
                        <p>VODAFONE_FIX_CIRCUIT_index  : <input type="text" size="2" name="VODAFONE_FIX_CIRCUIT_index" value="<%=myProperties.getProperty("VODAFONE_FIX_CIRCUIT_index")%>" />       
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
    List IGNORE_LIST 
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
