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
package org.smooks.cartridges.fixedlength;

import org.smooks.api.SmooksConfigException;
import org.smooks.api.resource.config.ReaderConfigurator;
import org.smooks.api.resource.config.ResourceConfig;
import org.smooks.assertion.AssertArgument;
import org.smooks.engine.resource.config.GenericReaderConfigurator;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Fixed Length Reader configurator.
 * <p/>
 * Supports programmatic {@link FixedLengthReader} configuration on a {@link org.smooks.Smooks#setReaderConfig(org.smooks.ReaderConfigurator) Smooks} instance.
 *
 * @author <a href="mailto:maurice.zeijen@smies.com">maurice.zeijen@smies.com</a>
 */
public class FixedLengthReaderConfigurator implements ReaderConfigurator {

    private String fields;
    private boolean lineNumber = false;
    private int skipLines = 0;
    private Charset encoding = Charset.forName("UTF-8");
    private String rootElementName = "set";
    private String recordElementName = "record";
    private String lineNumberAttributeName = "number";
    private String truncatedAttributeName = "truncated";
    private FixedLengthBinding binding;
    private String targetProfile;
    private boolean indent = false;
    private boolean strict = true;

    public FixedLengthReaderConfigurator(String fields) {
        AssertArgument.isNotNullAndNotEmpty(fields, "fields");
        this.fields = fields;
    }

    public FixedLengthReaderConfigurator setSkipLines(int skipLines) {
        this.skipLines = skipLines;
        return this;
    }

    public FixedLengthReaderConfigurator setEncoding(Charset encoding) {
        AssertArgument.isNotNull(encoding, "encoding");
        this.encoding = encoding;
        return this;
    }

    public FixedLengthReaderConfigurator setRootElementName(String rootElementName) {
        AssertArgument.isNotNullAndNotEmpty(rootElementName, "rootElementName");
        this.rootElementName = rootElementName;
        return this;
    }

    public FixedLengthReaderConfigurator setRecordElementName(String recordElementName) {
        AssertArgument.isNotNullAndNotEmpty(recordElementName, "recordElementName");
        this.recordElementName = recordElementName;
        return this;
    }

    public FixedLengthReaderConfigurator setLineNumberAttributeName(String lineNumberAttributeName) {
        AssertArgument.isNotNullAndNotEmpty(lineNumberAttributeName, "lineNumberAttributeName");
        this.lineNumberAttributeName = lineNumberAttributeName;
        return this;
    }

    public FixedLengthReaderConfigurator setTruncatedAttributeName(String truncatedAttributeName) {
        AssertArgument.isNotNullAndNotEmpty(truncatedAttributeName, "truncatedAttributeName");
        this.truncatedAttributeName = truncatedAttributeName;
        return this;
    }

    public FixedLengthReaderConfigurator setIndent(boolean indent) {
        this.indent = indent;
        return this;
    }

    public FixedLengthReaderConfigurator setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public FixedLengthReaderConfigurator setBinding(FixedLengthBinding binding) {
        this.binding = binding;
        return this;
    }

    public FixedLengthReaderConfigurator setLineNumber(boolean lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public FixedLengthReaderConfigurator setTargetProfile(String targetProfile) {
        AssertArgument.isNotNullAndNotEmpty(targetProfile, "targetProfile");
        this.targetProfile = targetProfile;
        return this;
    }

    public List<ResourceConfig> toConfig() {
        GenericReaderConfigurator configurator = new GenericReaderConfigurator(FixedLengthReader.class);

        configurator.getParameters().setProperty("fields", fields);
        configurator.getParameters().setProperty("lineNumber", Boolean.toString(lineNumber));
        configurator.getParameters().setProperty("skipLines", Integer.toString(skipLines));
        configurator.getParameters().setProperty("encoding", encoding.name());
        configurator.getParameters().setProperty("rootElementName", rootElementName);
        configurator.getParameters().setProperty("recordElementName", recordElementName);
        configurator.getParameters().setProperty("lineNumberAttributeName", lineNumberAttributeName);
        configurator.getParameters().setProperty("truncatedAttributeName", truncatedAttributeName);
        configurator.getParameters().setProperty("indent", Boolean.toString(indent));
        configurator.getParameters().setProperty("strict", Boolean.toString(strict));

        if(binding != null) {
            configurator.getParameters().setProperty("bindBeanId", binding.getBeanId());
            configurator.getParameters().setProperty("bindBeanClass", binding.getBeanClass().getName());
            configurator.getParameters().setProperty("bindingType", binding.getBindingType().toString());
            if(binding.getBindingType() == FixedLengthBindingType.MAP) {
                if(binding.getKeyField() == null) {
                    throw new SmooksConfigException("Fixed length 'MAP' Binding must specify a 'keyField' property on the binding configuration.");
                }
                configurator.getParameters().setProperty("bindMapKeyField", binding.getKeyField());
            }
        }

        configurator.setTargetProfile(targetProfile);

        return configurator.toConfig();
    }
}
