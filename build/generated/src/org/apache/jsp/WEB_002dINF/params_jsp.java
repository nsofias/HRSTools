package org.apache.jsp.WEB_002dINF;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import tools.FileComparator_billing;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.io.FileFilter;
import nsofiasLib.fileIO.JFileFilter;
import java.io.FilenameFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Map;
import java.util.Enumeration;

public final class params_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

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

      out.write("     \r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("        <title>JSP Page</title>\r\n");
      out.write("        <style>\r\n");
      out.write("            table, th, td {\r\n");
      out.write("                border: 1px solid white;\r\n");
      out.write("                border-collapse: collapse;\r\n");
      out.write("            }\r\n");
      out.write("            th, td {\r\n");
      out.write("                background-color: #96D4D4;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        <form action=\"params.jsp\" method=\"GET\" id=\"myForm\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">\r\n");
      out.write("            Files Directory for files:\r\n");
      out.write("            <select onchange = \"form.submit()\"name=\"directory\">\r\n");
      out.write("                out.println(\"<option></option>\");\r\n");
      out.write("                ");

                    for (String filename : filenames) {
                        out.println("<option>" + filename + "</option>");
                    }
                
      out.write("\r\n");
      out.write("            </select>\r\n");
      out.write("        </form>\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");

        return;
    }
    //---------- dir is not null ----------
    String directory = request.getParameter("directory");
    //out.println("<a href='javascript:history.back()'>Go Back</a>");
//---------save-----------
    if (request.getParameter("save") != null) {
        params.entrySet().stream().filter(entry -> !entry.getKey().equals("compare"))
                .forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
        FileWriter myFileWriter = new FileWriter(main_dir + directory + "\\parameters.properties", StandardCharsets.UTF_8);
        out.println("saved to:" + main_dir + directory + "\\parameters.properties");
        myProperties.store(myFileWriter, "");
        return;
    }
    String report_type = request.getParameter("report_type");
    if (report_type != null) {
        params.entrySet().stream().forEach(entry -> myProperties.put(entry.getKey(), entry.getValue()[0]));
        FileComparator_billing myFileComparator = new FileComparator_billing(myProperties);
        try {
            myFileComparator.loadFiles();
        } catch (IOException ex) {
            out.println(ex);
        }
        //-------------- compare -----------------

        if (request.getParameter("run") != null) {

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/csv;charset=UTF-8\">\r\n");
      out.write("        <title>JSP Page</title>\r\n");
      out.write("        <style>\r\n");
      out.write("            table, th, td {\r\n");
      out.write("                border: 1px solid white;\r\n");
      out.write("                border-collapse: collapse;\r\n");
      out.write("            }\r\n");
      out.write("            th, td {\r\n");
      out.write("                background-color: #96D4D4;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head><body>\r\n");
      out.write("        ");

            if (report_type.equals("report_HRS_DB_YES_HRS_BILLING_NO")) {
                myFileComparator.report_HRS_DB_YES_HRS_BILLING_NO(out);
            } else if (report_type.equals("report_HRS_DB_NO_HRS_BILLING_YES")) {
                myFileComparator.report_HRS_DB_NO_HRS_BILLING_YES(out);
            } else if (report_type.equals("report_VOD_DB_YES_VOD_BILLING_NO")) {
                myFileComparator.report_VOD_DB_YES_VOD_BILLING_NO(out);
            } else if (report_type.equals("report_VOD_DB_NO_VOD_BILLING_YES")) {
                myFileComparator.report_VOD_DB_NO_VOD_BILLING_YES(out);
            } else if (report_type.equals("report_HRS_BILLING_YES_VOD_BILLING_NO")) {
                myFileComparator.report_HRS_BILLING_YES_VOD_BILLING_NO(out);
            } else if (report_type.equals("report_HRS_BILLING_NO_VOD_BILLING_YES")) {
                myFileComparator.report_HRS_BILLING_NO_VOD_BILLING_YES(out);
            } else if (report_type.equals("report_HRS_DB_YES_VOD_DB_NO")) {
                myFileComparator.report_HRS_DB_YES_VOD_DB_NO(out);
            } else if (report_type.equals("report_HRS_DB_NO_VOD_DB_YES")) {
                myFileComparator.report_HRS_DB_NO_VOD_DB_YES(out);
            }
        
      out.write("\r\n");
      out.write("    </body></html>\r\n");
      out.write("    ");

                return;
            }

        }
        //-- read csv files of directory 
        File dir = new File(main_dir + directory);

        FilenameFilter filter = ( d,     name) -> name.endsWith(".csv");
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


    
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("        <title>JSP Page</title>\r\n");
      out.write("        <style>\r\n");
      out.write("            table, th, td {\r\n");
      out.write("                border: 1px solid white;\r\n");
      out.write("                border-collapse: collapse;\r\n");
      out.write("            }\r\n");
      out.write("            th, td {\r\n");
      out.write("                background-color: #96D4D4;\r\n");
      out.write("            }\r\n");
      out.write("        </style>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>        \r\n");
      out.write("        <h1>directory: ");
      out.print(directory);
      out.write("</h1>\r\n");
      out.write("        <form action=\"params.jsp\" method=\"POST\" id=\"myForm\" target=\"_blank\" >\r\n");
      out.write("            <p><input type=\"submit\" name=\"save\" value=\"Save parameters\" />\r\n");
      out.write("\r\n");
      out.write("                <input type=\"hidden\" name=\"directory\" value=\"");
      out.print(directory);
      out.write("\" />\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("                <input type=\"submit\" name=\"run\" value=\"RUN\" />\r\n");
      out.write("\r\n");
      out.write("            <table >\r\n");
      out.write("                <tr><td>\r\n");
      out.write("                        <p><b>Select Report rype:</b> <select name=\"report_type\">\r\n");
      out.write("                                <option value=\"report_HRS_DB_YES_HRS_BILLING_NO\">Only in HRS Database not in HRS Billing</option>\r\n");
      out.write("                                <option value=\"report_HRS_DB_NO_HRS_BILLING_YES\">Only in HRS Billing not in HRS Database</option>\r\n");
      out.write("                                <option value=\"report_VOD_DB_YES_VOD_BILLING_NO\">Only in Vodafon DB not in Vodafon Billing</option>\r\n");
      out.write("                                <option value=\"report_VOD_DB_NO_VOD_BILLING_YES\">Only in Vodafon Billing not in Vodafon DB</option>\r\n");
      out.write("                                <option value=\"report_HRS_BILLING_YES_VOD_BILLING_NO\">Only in HRS Billing not in Vodafon Billing</option>\r\n");
      out.write("                                <option value=\"report_HRS_BILLING_NO_VOD_BILLING_YES\">Only in Vodafon Billing not in HRS Billing</option>\r\n");
      out.write("                                <option value=\"report_HRS_DB_YES_VOD_DB_NO\">Only in HRS DB not in Vodafon DB</option>\r\n");
      out.write("                                <option value=\"report_HRS_DB_NO_VOD_DB_YES\">Only in Vodafon DB not in HRS DB</option>\r\n");
      out.write("                            </select>                \r\n");
      out.write("                    </td></tr>\r\n");
      out.write("\r\n");
      out.write("                <tr><td>\r\n");
      out.write("                        <p>CHARSET  : <input type=\"text\" size=\"10\" name=\"");
      out.print(FileComparator_billing.FileComparator_CHARSET);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_CHARSET));
      out.write("\" />\r\n");
      out.write("                            &nbsp;SPLITTER: <input type=\"text\" size=\"1\" name=\"");
      out.print(FileComparator_billing.FileComparator_SPLITTER);
      out.write("\" size=\"3\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_SPLITTER));
      out.write("\" />\r\n");
      out.write("                            &nbsp;MAX_ACTIVATION_DATE: <input type=\"text\" size=\"30\" name=\"");
      out.print(FileComparator_billing.FileComparator_MAX_ACTIVATION_DATE);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_MAX_ACTIVATION_DATE));
      out.write("\" />\r\n");
      out.write("                            &nbsp;IGNORE: <input type=\"text\" size =\"60\" name=\"");
      out.print(FileComparator_billing.FileComparator_IGNORE_LIST);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_IGNORE_LIST));
      out.write("\" />\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <p><h1> HRS files</h1>                \r\n");
      out.write("                        <b>DATABASE_filename : </b>                        \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_ATLANTIS_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("ATLANTIS_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                      \r\n");
      out.write("                        <p>MSISDN_index : <input type=\"text\" size=\"2\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ATLANTIS_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ATLANTIS_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                        <p>MSISDN2_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ATLANTIS_MSISDN2_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ATLANTIS_MSISDN2_index));
      out.write("\" />\r\n");
      out.write("                        <p>DATE_index : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ATLANTIS_DATE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ATLANTIS_DATE_index));
      out.write("\" />\r\n");
      out.write("                        <p>STATUS_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ATLANTIS_STATUS_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ATLANTIS_STATUS_index));
      out.write("\" />\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("                        <P><b>BILLING_filename : </b>                        \r\n");
      out.write("                            <select name=\"");
      out.print(FileComparator_billing.FileComparator_HRS_BILLING_filename);
      out.write("\">    \r\n");
      out.write("                                ");

                                    out.println("<option>" + myProperties.getProperty("HRS_BILLING_filename") + "</option>");
                                    for (String filename : filenames) {
                                        out.println("<option value='" + filename + "'>" + filename + "</option>");
                                    }
                                
      out.write("\r\n");
      out.write("                            </select>                      \r\n");
      out.write("                        <p>HRS_BILLING_MSISDN_index : <input type=\"text\" size=\"2\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_HRS_BILLING_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_HRS_BILLING_MSISDN_index));
      out.write("\" />\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <!----------------- VODAFON DATABASE ------------------------------------------->\r\n");
      out.write("                        <h1>VODAFON DATABASE</h1> \r\n");
      out.write("                        <b>SB_HRS_filename : </b>       \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_SB_HRS_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("SB_HRS_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                         \r\n");
      out.write("                        <p>SB_HRS_MSISDN_index : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_SB_HRS_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_SB_HRS_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;SB_HRS_DATE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_SB_HRS_DATE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_SB_HRS_DATE_index));
      out.write("\" />\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <b>ELRA_filename : </b>                        \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("ELRA_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                         \r\n");
      out.write("                        <p>ELRA_MSISDN_index : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ELRA_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;ELRA_DATE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_DATE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ELRA_DATE_index));
      out.write("\" />\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <b>ELRA_PREPAY_filename : </b>                               \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_PREPAY_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty(FileComparator_billing.FileComparator_ELRA_PREPAY_filename) + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                        \r\n");
      out.write("                        <p>ELRA_PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_PREPAY_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ELRA_PREPAY_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;ELRA_PREPAY_DATE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_ELRA_PREPAY_DATE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_ELRA_PREPAY_DATE_index));
      out.write("\" />\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <!----------------- BILLING ------------------------------------------->\r\n");
      out.write("                        <h1> VODAFON BILLING</h1>   \r\n");
      out.write("                        <b>SPLIT_filename : </b>      \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_SPLIT_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_SPLIT_filename) + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                        \r\n");
      out.write("                        <p>SPLIT_MSISDN_index : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_SPLIT_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_SPLIT_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;SPLIT_VALUE_index : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_SPLIT_VALUE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_SPLIT_VALUE_index));
      out.write("\" />\r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <b>PREPAY_filename : </b>                        \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_PREPAY_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_PREPAY_filename) + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                        \r\n");
      out.write("                        <p>PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_PREPAY_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_PREPAY_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;PREPAY_VALUE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_PREPAY_VALUE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_PREPAY_VALUE_index));
      out.write("\" />    \r\n");
      out.write("                    </td></tr><tr><td>\r\n");
      out.write("                        <b>MOBILE_filename : </b>                         \r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_MOBILE_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_MOBILE_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                        \r\n");
      out.write("                        <p>MOBILE_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_MOBILE_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_MOBILE_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;MOBILE_VALUE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_MOBILE_VALUE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_MOBILE_VALUE_index));
      out.write("\" />    \r\n");
      out.write("                    </td></tr><tr><td>                     \r\n");
      out.write("                        <b>FIX_filename : </b>\r\n");
      out.write("                        <select name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_FIX_filename);
      out.write("\">    \r\n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_FIX_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\r\n");
      out.write("                        </select>                        \r\n");
      out.write("                        <p>FIX_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_FIX_MSISDN_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_MSISDN_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;FIX_VALUE_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_FIX_VALUE_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_VALUE_index));
      out.write("\" />\r\n");
      out.write("                            &nbsp;FIX_ERP_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_FIX_ERP_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_ERP_index));
      out.write("\" />   \r\n");
      out.write("                            &nbsp;FIX_CIRCUIT_index  : <input type=\"text\" size=\"2\" name=\"");
      out.print(FileComparator_billing.FileComparator_VODAFONE_FIX_CIRCUIT_index);
      out.write("\" value=\"");
      out.print(myProperties.getProperty(FileComparator_billing.FileComparator_VODAFONE_FIX_CIRCUIT_index));
      out.write("\" />       \r\n");
      out.write("                    </td></tr>\r\n");
      out.write("            </table>\r\n");
      out.write("        </form>\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!--\r\n");
      out.write("\r\n");
      out.write("-->\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
