/*-
 * ========================LICENSE_START=================================
 * smooks-fixed-length-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
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
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cartridges.fixedlength.prog;

import org.smooks.Smooks;
import org.smooks.assertion.AssertArgument;
import org.smooks.cartridges.fixedlength.FixedLengthBinding;
import org.smooks.cartridges.fixedlength.FixedLengthBindingType;
import org.smooks.cartridges.fixedlength.FixedLengthReaderConfigurator;
import org.smooks.io.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

/**
 * Fixed Length {@link java.util.List} Binder class.
 * <p/>
 * Simple Fixed Length records to Object {@link java.util.List} binding class.
 * <p/>
 * Exmaple usage:
 * <pre>
 * public class PeopleBinder {
 *     // Create and cache the binder instance..
 *     private FixedLengthListBinder binder = new FixedLengthListBinder("firstname[10],lastname[10],gender[1],age[3],country[2]", Person.class);
 *
 *     public List&lt;Person&gt; bind(Reader fixedLengthStream) {
 *         return binder.bind(fixedLengthStream);
 *     }
 * }
 * </pre>
 *
 *
 * @author <a href="mailto:maurice.zeijen@smies.com">maurice.zeijen@smies.com</a>
 */
public class FixedLengthListBinder {

    private String beanId = UUID.randomUUID().toString();
    private Smooks smooks;

    public FixedLengthListBinder(String fields, Class recordType) {
        AssertArgument.isNotNullAndNotEmpty(fields, "fields");
        AssertArgument.isNotNull(recordType, "recordType");

        smooks = new Smooks();
        smooks.setReaderConfig(new FixedLengthReaderConfigurator(fields)
                .setBinding(new FixedLengthBinding(beanId, recordType, FixedLengthBindingType.LIST)));
    }

    public List bind(Reader fixedLengthStream) {
        AssertArgument.isNotNull(fixedLengthStream, "fixedLengthStream");

        JavaResult javaResult = new JavaResult();

        smooks.filterSource(new StreamSource(fixedLengthStream), javaResult);

        return (List) javaResult.getBean(beanId);
    }

    public List bind(InputStream fixedLengthStream) {
        return bind(new InputStreamReader(fixedLengthStream));
    }
}
