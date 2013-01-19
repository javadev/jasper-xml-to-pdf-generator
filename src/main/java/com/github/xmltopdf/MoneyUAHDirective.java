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
public class MoneyUAHDirective extends Directive {

    public String getName() {
        return "moneyUAH";
    }

    public int getType() {
        return LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {

        //setting default params
        String moneyValue = null;

        //reading params
        if (node.jjtGetChild(0) != null) {
            moneyValue = node.jjtGetChild(0).value(context) == null ? null : String.valueOf(node.jjtGetChild(0).value(context));
        }

        //truncate and write result to writer
        writer.write(moneyUAH(moneyValue));
        return true;

    }

    public String moneyUAH(String moneyValue) {
        if (moneyValue == null) {
            return "";
        }
        java.text.NumberFormat nf = java.text.NumberFormat.getNumberInstance(java.util.Locale.FRANCE);
        java.text.DecimalFormat df = (java.text.DecimalFormat) nf;
        df.applyPattern(",##0.00");
        return nf.format(new java.math.BigDecimal(moneyValue)) + " грн.";
    }
}
