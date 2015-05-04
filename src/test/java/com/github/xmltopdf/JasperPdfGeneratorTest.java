/*
 * $Id$
 *
 * Copyright 2015 Valentyn Kolesnikov
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * JasperPdfGeneratorTest.
 *
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class JasperPdfGeneratorTest {

    private JasperPdfGenerator generator;

    @Before
    public void setUp() throws Exception {
        generator = new JasperPdfGenerator(
            Arrays.asList("src/test/resources/application-form-ukr.jrxml"),
            Arrays.asList("src/test/resources/in_dossier.xml"),
            Arrays.asList(JasperPdfGenerator.DocType.HTML));
    }

    @Test
    public void generate() throws Exception {
        final InputStream bookingInputStream = JasperPdfGeneratorTest.class.getClassLoader().getResourceAsStream("application-form-ukr.html");
        final BufferedReader bookingReader = new BufferedReader(new InputStreamReader(bookingInputStream));
        final List<String> bookingLines = new ArrayList<String>();
        try {
            String line = bookingReader.readLine();
            while (line != null) {
                bookingLines.add(line);
                line = bookingReader.readLine();
            }
        } finally {
            bookingReader.close();
        }
        List<Map.Entry<ByteArrayOutputStream, JasperPdfGenerator.DocType>> documents = generator.createDocuments();
        documents.get(0).getKey().writeTo(new FileOutputStream("application-form-ukr.html"));        
        final BufferedReader resultreader = new BufferedReader(new InputStreamReader(new FileInputStream("application-form-ukr.html")));
        final List<String> resultLines = new ArrayList<String>();
        try {
            String line = resultreader.readLine();
            while (line != null) {
                resultLines.add(line);
                line = resultreader.readLine();
            }
        } finally {
            resultreader.close();
        }
        Assert.assertArrayEquals(bookingLines.toArray(), resultLines.toArray());
    }
}
