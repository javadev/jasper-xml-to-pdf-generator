/*
 * $Id$
 *
 * Copyright 2013 Valentyn Kolesnikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.xmltopdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.List;

import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;
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

/**.
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class JasperPdfGenerator {
    private XMLTag xmlTag;

    public JasperPdfGenerator() {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private void createPDFJasper(List<String> templateNames, String xmlFileName, ByteArrayOutputStream os) {
        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        InputStream fileIs = null;
        InputStream stringIs = null;
        xmlTag = XMLDoc.from(new File(xmlFileName), true);
        try {
            try {
                for (String templateName : templateNames) {
                    fileIs = new FileInputStream(templateNames.get(0));
                    String contents = applyVelocityTemplate(IOUtils.toString(fileIs, "UTF-8"));
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

    private String applyVelocityTemplate(String templateData) throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "string");
        p.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        Velocity.init(p);

        StringResourceRepository repo = StringResourceLoader.getRepository();
        repo.putStringResource("template", templateData);
        Template template = Velocity.getTemplate("template", "UTF-8");
        StringWriter writer = new StringWriter();
        VelocityContext context = new VelocityContext();
        context.put("xml", xmlTag);
        template.merge(context, writer);
        writer.flush();
        writer.close();
        return writer.toString();
    }

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new JasperPdfGenerator().createPDFJasper(Arrays.asList("application-form-ukr.jrxml"), "in_dossier.xml", os);
        os.writeTo(new FileOutputStream("application-form-ukr.pdf"));
    }
}
