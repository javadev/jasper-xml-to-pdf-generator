/*
 * $Id$
 *
 * Copyright 2014 Valentyn Kolesnikov
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

import org.junit.Assert;
import org.junit.Test;

public class LogTest {

    @Test
    public void debug() throws Exception {
        LOG.debug("");
    }

    @Test
    public void info() throws Exception {
        LOG.info("");
    }

    @Test
    public void warn() throws Exception {
        LOG.warn("");
        LOG.warn(new Exception(""), "");
    }

    @Test
    public void error() throws Exception {
        LOG.error("");
        LOG.error(new Exception(""), "");
        LOG.error(new Exception("", new Exception("")), "");
        LOG.error(new Exception(""), null);
        LOG.error(null);
        LOG.error(new Exception((String) null), null);
    }
}
