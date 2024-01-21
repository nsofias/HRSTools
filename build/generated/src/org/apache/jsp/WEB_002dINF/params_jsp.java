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

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        ");

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
        
      out.write("      \n");
      out.write("        <form action=\"params.jsp\" method=\"GET\" id=\"myForm\" enctype=\"multipart/form-data\" accept-charset=\"UTF-8\">\n");
      out.write("            Files Directory for files:\n");
      out.write("            <select onchange = \"form.submit()\"name=\"directory\">\n");
      out.write("                out.println(\"<option></option>\");\n");
      out.write("                ");

                    for (String filename : filenames) {
                        out.println("<option>" + filename + "</option>");
                    }
                
      out.write("\n");
      out.write("            </select>\n");
      out.write("        </form>\n");
      out.write("        ");

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


        
      out.write("\n");
      out.write("\n");
      out.write("        <h1>directory: ");
      out.print(directory);
      out.write("</h1>\n");
      out.write("        <form action=\"params.jsp\" method=\"POST\" id=\"myForm\" >\n");
      out.write("            <input type=\"submit\" name=\"compare\" value=\"Compare\" />\n");
      out.write("            <input type=\"submit\" name=\"save\" value=\"Save parameters\" />\n");
      out.write("            <input type=\"hidden\" name=\"directory\" value=\"");
      out.print(directory);
      out.write("\" />\n");
      out.write("            <table>\n");
      out.write("                <tr><td>\n");
      out.write("                        <p>CHARSET  : <input type=\"text\" size=\"10\" name=\"CHARSET\" value=\"");
      out.print(myProperties.getProperty("CHARSET"));
      out.write("\" />\n");
      out.write("                        <p>SPLITTER  : <input type=\"text\" size=\"1\" name=\"SPLITTER\" size=\"3\" value=\"");
      out.print(myProperties.getProperty("SPLITTER"));
      out.write("\" />\n");
      out.write("                        <p>MAX_ACTIVATION_DATE  : <input type=\"text\" size=\"30\" name=\"MAX_ACTIVATION_DATE\" value=\"");
      out.print(myProperties.getProperty("MAX_ACTIVATION_DATE"));
      out.write("\" />\n");
      out.write("                        <p>List IGNORE_LIST  : <input type=\"text\" size =\"60\" name=\"IGNORE_LIST\" value=\"");
      out.print(myProperties.getProperty("IGNORE_LIST"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <p><h1> HRS Database files</h1>                \n");
      out.write("                        <p><b>ATLANTIS_filename : </b>                        \n");
      out.write("                            <select name=\"ATLANTIS_filename\">    \n");
      out.write("                                ");

                                    out.println("<option>" + myProperties.getProperty("ATLANTIS_filename") + "</option>");
                                    for (String filename : filenames) {
                                        out.println("<option value='" + filename + "'>" + filename + "</option>");
                                    }
                                
      out.write("\n");
      out.write("                            </select>                      \n");
      out.write("                        <p>ATLANTIS_MSISDN_index : <input type=\"text\" size=\"2\" size=\"2\" name=\"ATLANTIS_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("ATLANTIS_MSISDN_index"));
      out.write("\" />\n");
      out.write("                        <p>ATLANTIS_MSISDN2_index  : <input type=\"text\" size=\"2\" name=\"ATLANTIS_MSISDN2_index\" value=\"");
      out.print(myProperties.getProperty("ATLANTIS_MSISDN2_index"));
      out.write("\" />\n");
      out.write("                        <p>ATLANTIS_DATE_index : <input type=\"text\" size=\"2\" name=\"ATLANTIS_DATE_index\" value=\"");
      out.print(myProperties.getProperty("ATLANTIS_DATE_index"));
      out.write("\" />\n");
      out.write("                        <p>ATLANTIS_STATUS_index  : <input type=\"text\" size=\"2\" name=\"ATLANTIS_STATUS_index\" value=\"");
      out.print(myProperties.getProperty("ATLANTIS_STATUS_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1><h3> Vodafon database SB_HRS</h1>                        \n");
      out.write("                        <select name=\"SB_HRS_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("SB_HRS_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                         \n");
      out.write("                        <p>SB_HRS_MSISDN_index : <input type=\"text\" size=\"2\" name=\"SB_HRS_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("SB_HRS_MSISDN_index"));
      out.write("\" />\n");
      out.write("                        <p>SB_HRS_DATE_index  : <input type=\"text\" size=\"2\" name=\"SB_HRS_DATE_index\" value=\"");
      out.print(myProperties.getProperty("SB_HRS_DATE_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon database ELRA</h1>                        \n");
      out.write("                        <select name=\"ELRA_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("ELRA_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                         \n");
      out.write("                        <p>ELRA_MSISDN_index : <input type=\"text\" size=\"2\" name=\"ELRA_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("ELRA_MSISDN_index"));
      out.write("\" />\n");
      out.write("                        <p>ELRA_DATE_index  : <input type=\"text\" size=\"2\" name=\"ELRA_DATE_index\" value=\"");
      out.print(myProperties.getProperty("ELRA_DATE_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon database ELRA_PREPAY</h1>                        \n");
      out.write("                        <select name=\"ELRA_PREPAY_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("ELRA_PREPAY_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                        \n");
      out.write("                        <p>ELRA_PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"ELRA_PREPAY_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("ELRA_PREPAY_MSISDN_index"));
      out.write("\" />\n");
      out.write("                        <p>ELRA_PREPAY_DATE_index  : <input type=\"text\" size=\"2\" name=\"ELRA_PREPAY_DATE_index\" value=\"");
      out.print(myProperties.getProperty("ELRA_PREPAY_DATE_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing SPLIT</h1>                        \n");
      out.write("                        <select name=\"VODAFONE_SPLIT_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_SPLIT_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                        \n");
      out.write("                        <p>VODAFONE_SPLIT_MSISDN_index : <input type=\"text\" size=\"2\" name=\"VODAFONE_SPLIT_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("VODAFONE_SPLIT_MSISDN_index"));
      out.write("\"/>\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΚΑΡΤΟΚΙΝΗΤΗ</h1>                        \n");
      out.write("                        <select name=\"VODAFONE_PREPAY_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_PREPAY_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                        \n");
      out.write("                        <p>VODAFONE_PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_PREPAY_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("VODAFONE_PREPAY_MSISDN_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΚΙΝΗΤΗ</h1>                        \n");
      out.write("                        <select name=\"VODAFONE_MOBILE_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_MOBILE_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                        \n");
      out.write("                        <p>VODAFONE_MOBILE_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_MOBILE_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("VODAFONE_MOBILE_MSISDN_index"));
      out.write("\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΣΤΑΘΕΡΗ</h1>                        \n");
      out.write("                        <select name=\"VODAFONE_FIX_filename\">    \n");
      out.write("                            ");

                                out.println("<option>" + myProperties.getProperty("VODAFONE_FIX_filename") + "</option>");
                                for (String filename : filenames) {
                                    out.println("<option value='" + filename + "'>" + filename + "</option>");
                                }
                            
      out.write("\n");
      out.write("                        </select>                        \n");
      out.write("                        <p>VODAFONE_FIX_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_FIX_MSISDN_index\" value=\"");
      out.print(myProperties.getProperty("VODAFONE_FIX_MSISDN_index"));
      out.write("\" />\n");
      out.write("                        <p>VODAFONE_FIX_CIRCUIT_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_FIX_CIRCUIT_index\" value=\"");
      out.print(myProperties.getProperty("VODAFONE_FIX_CIRCUIT_index"));
      out.write("\" />       \n");
      out.write("                    </td></tr>\n");
      out.write("            </table>\n");
      out.write("\n");
      out.write("\n");
      out.write("        </form>\n");
      out.write("\n");
      out.write("    </body>\n");
      out.write("</html>\n");
      out.write("\n");
      out.write("\n");
      out.write("<!--\n");
      out.write("        #\n");
      out.write("    CHARSET \n");
      out.write("    SPLITTER = \n");
      out.write("    MAX_ACTIVATION_DATE \n");
      out.write("    #\n");
      out.write("    List IGNORE_LIST \n");
      out.write("    # HRS files</h1>\n");
      out.write("    ATLANTIS_filename \n");
      out.write("    ATLANTIS_MSISDN_index \n");
      out.write("    ATLANTIS_MSISDN2_index \n");
      out.write("    ATLANTIS_DATE_index\n");
      out.write("    ATLANTIS_STATUS_index \n");
      out.write("    #-------- Vodafon files\n");
      out.write("    SB_HRS_filename \n");
      out.write("    SB_HRS_MSISDN_index\n");
      out.write("    SB_HRS_DATE_index \n");
      out.write("    #\n");
      out.write("    ELRA_filename \n");
      out.write("    ELRA_MSISDN_index\n");
      out.write("    ELRA_DATE_index \n");
      out.write("    #\n");
      out.write("    ELRA_PREPAY_filename\n");
      out.write("    ELRA_PREPAY_MSISDN_index \n");
      out.write("    ELRA_PREPAY_DATE_index \n");
      out.write("        #\n");
      out.write("    VODAFONE_SPLIT_filename\n");
      out.write("    VODAFONE_SPLIT_MSISDN_index\n");
      out.write("    #\n");
      out.write("    VODAFONE_PREPAY_filename \n");
      out.write("    VODAFONE_PREPAY_MSISDN_index \n");
      out.write("    #\n");
      out.write("    VODAFONE_MOBILE_filename \n");
      out.write("    VODAFONE_MOBILE_MSISDN_index \n");
      out.write("    #\n");
      out.write("    VODAFONE_FIX_filename \n");
      out.write("    VODAFONE_FIX_MSISDN_index \n");
      out.write("    VODAFONE_FIX_CIRCUIT_index \n");
      out.write("\n");
      out.write("\n");
      out.write("-->\n");
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
