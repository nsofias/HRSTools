package org.apache.jsp.WEB_002dINF;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
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
      out.write("<!DOCTYPE html>\n");

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

      out.write("\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>JSP Page</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <h1>Hello World!</h1>\n");
      out.write("        <form action=\"params.jsp\" method=\"POST\" id=\"myForm\">\n");
      out.write("            <input type=\"submit\" name=compare\" value=\"Compare\" />\n");
      out.write("\n");
      out.write("            <input type=\"button\" id=\"loadFileXml\" value=\"Load parameters from file\" onclick=\"document.getElementById('propertiesFile').click();\" />\n");
      out.write("            <input type=\"file\" onchange=\"document.getElementById('myForm').submit();\" style=\"display:none;\" id=\"propertiesFile\" accept=\".properties\" name=\"propertiesFile\"/>\n");
      out.write("\n");
      out.write("            <table>\n");
      out.write("                <tr><td>\n");
      out.write("                        <p>CHARSET  : <input type=\"text\" size=\"10\" name=\"CHARSET\" value=\"ISO-8859-7\" />\n");
      out.write("                        <p>SPLITTER  : <input type=\"text\" size=\"1\" name=\"SPLITTER\" size=\"3\" value=\";\" />\n");
      out.write("                        <p>MAX_ACTIVATION_DATE  : <input type=\"text\" size=\"2\" name=\"MAX_ACTIVATION_DATE\" value=\"\" />\n");
      out.write("                        <p>List ignoreList  : <input type=\"text\" size =\"60\" name=\"ATLANTIS_MSISDN_index\" value=\"13,6,69,72,23,75,57,4,33,24,20,19,76\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <p><h1> HRS Database files</h1>                \n");
      out.write("                        <p><h3>ATLANTIS_filename : <input type=\"file\" accept=\".csv\" accept=\".csv\" name=\"ATLANTIS_filename\" /></h3>\n");
      out.write("                        <p>ATLANTIS_MSISDN_index : <input type=\"text\" size=\"2\" size=\"2\" name=\"ATLANTIS_MSISDN_index\" value=\"\" />\n");
      out.write("                        <p>ATLANTIS_MSISDN2_index  : <input type=\"text\" size=\"2\" name=\"ATLANTIS_MSISDN2_index\" value=\"\" />\n");
      out.write("                        <p>ATLANTIS_DATE_index : <input type=\"text\" size=\"2\" name=\"ATLANTIS_DATE_index\" value=\"\" />\n");
      out.write("                        <p>ATLANTIS_STATUS_index  : <input type=\"text\" size=\"2\" name=\"ATLANTIS_STATUS_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1><h3> Vodafon database SB_HRS</h1>\n");
      out.write("                        <p><h3>SB_HRS_filename : <input type=\"file\" accept=\".csv\" name=\"SB_HRS_filename\" /></h3>\n");
      out.write("                        <p>SB_HRS_MSISDN_index : <input type=\"text\" size=\"2\" name=\"SB_HRS_MSISDN_index\" value=\"\" />\n");
      out.write("                        <p>SB_HRS_DATE_index  : <input type=\"text\" size=\"2\" name=\"SB_HRS_DATE_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon database ELRA</h1>\n");
      out.write("                        <p><h3>ELRA_filename  : <input type=\"file\" accept=\".csv\" name=\"ELRA_filename\" /></h3>\n");
      out.write("                        <p>ELRA_MSISDN_index : <input type=\"text\" size=\"2\" name=\"ELRA_MSISDN_index\" value=\"\" />\n");
      out.write("                        <p>ELRA_DATE_index  : <input type=\"text\" size=\"2\" name=\"ELRA_DATE_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon database ELRA_PREPAY</h1>\n");
      out.write("                        <p><h3>ELRA_PREPAY_filename : <input type=\"file\" accept=\".csv\" name=\"ELRA_PREPAY_filename\" /></h3>\n");
      out.write("                        <p>ELRA_PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"ELRA_PREPAY_MSISDN_index\" value=\"\" />\n");
      out.write("                        <p>ELRA_PREPAY_DATE_index  : <input type=\"text\" size=\"2\" name=\"ELRA_PREPAY_DATE_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing SPLIT</h1>\n");
      out.write("                        <p>VODAFONE_SPLIT_filename : <input type=\"file\" accept=\".csv\" name=\"VODAFONE_SPLIT_filename\" /></h3>\n");
      out.write("                        <p>VODAFONE_SPLIT_MSISDN_index : <input type=\"text\" size=\"2\" name=\"VODAFONE_SPLIT_MSISDN_index\" value=\"\"/>\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΚΑΡΤΟΚΙΝΗΤΗ</h1>\n");
      out.write("                        <p><h3>VODAFONE_PREPAY_filename  : <input type=\"file\" accept=\".csv\" name=\"VODAFONE_PREPAY_filename\" /></h3>\n");
      out.write("                        <p>VODAFONE_PREPAY_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_PREPAY_MSISDN_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΚΙΝΗΤΗ</h1>\n");
      out.write("                        <p><h3>VODAFONE_MOBILE_filename  : <input type=\"file\" accept=\".csv\" name=\"VODAFONE_MOBILE_filename\" /></h3>\n");
      out.write("                        <p>VODAFONE_MOBILE_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_MOBILE_MSISDN_index\" value=\"\" />\n");
      out.write("                    </td></tr><tr><td>\n");
      out.write("                        <h1> Vodafon billing ΣΤΑΘΕΡΗ</h1>\n");
      out.write("                        <p><h3>VODAFONE_FIX_filename  : <input type=\"file\" accept=\".csv\" name=\"VODAFONE_FIX_filename\" /></h3>\n");
      out.write("                        <p>VODAFONE_FIX_MSISDN_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_FIX_MSISDN_index\" value=\"\" />\n");
      out.write("                        <p>VODAFONE_FIX_CIRCUIT_index  : <input type=\"text\" size=\"2\" name=\"VODAFONE_FIX_CIRCUIT_index\" value=\"\" />       \n");
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
      out.write("    List ignoreList \n");
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
