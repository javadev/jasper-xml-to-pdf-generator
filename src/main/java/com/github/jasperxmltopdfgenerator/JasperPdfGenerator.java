package com.github.jasperxmltopdfgenerator;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRPrintPage;

public class JasperPdfGenerator {

    private void createPDFJasper(List<String> templateNames, ByteArrayOutputStream os) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        InputStream fileIs = null;
        InputStream stringIs = null;
        try {
            String decodedPath = ".";
            try {
                for (String templateName : templateNames) {
                    fileIs = new FileInputStream(decodedPath + "/templates/" + templateNames.get(0));
                    String contents = applyVelocityTemplate(IOUtils.toString(fileIs, "UTF-8"), decodedPath + "/templates");
                    stringIs = IOUtils.toInputStream(contents, "UTF-8");
                    JasperReport jasperReport = JasperCompileManager.compileReport(stringIs);
                    jasperPrints.add(JasperFillManager.fillReport(
                        jasperReport, new HashMap(), new JREmptyDataSource()));
                }
                JasperPrint jasperPrint = jasperPrints.get(0);
                for (int index = 1; index < jasperPrints.size(); index += 1) {
                    List<JRPrintPage> pages = jasperPrints.get(index).getPages();
                    for (JRPrintPage page : pages) {
                        jasperPrint.addPage(page);
                    }
                }
                JasperExportManager.exportReportToPdfStream(jasperPrint, os);
            } finally {
                IOUtils.closeQuietly(fileIs);
                IOUtils.closeQuietly(stringIs);
            }
        } catch (Exception ex) {
            LOG.error(this, ex, ex.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    private String applyVelocityTemplate(String templateData, String basePath) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        p.setProperty("userdirective", "");
        Velocity.init(p);

        StringResourceRepository repo = StringResourceLoader.getRepository();
        repo.putStringResource("template", templateData);
        Template template = Velocity.getTemplate("template", "UTF-8");
        StringWriter writer = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("basePath", basePath.replace("\\", "/"));
        template.merge(context, writer);
        writer.flush();
        writer.close();
        return writer.toString();
    }
}
