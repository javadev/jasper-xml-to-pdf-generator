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

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**.
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class DateDirective extends Directive {
    private java.text.SimpleDateFormat defaultFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
        java.util.Locale.US);
    private java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.US);

    public String getName() {
        return "date";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node) 
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        //setting default params
        String dateValue = null;

        //reading params
        if (node.jjtGetChild(0) != null) {
            dateValue = String.valueOf(node.jjtGetChild(0).value(context));
        }

        //truncate and write result to writer
        writer.write(date(dateValue));
        return true;

    }

    public String date(String dateValue) {
        if (dateValue == null) {
            return "";
        }
        try {
        return format.format(defaultFormat.parse(dateValue));
        } catch (Exception ex) {
            return "";
        }
    }
}
