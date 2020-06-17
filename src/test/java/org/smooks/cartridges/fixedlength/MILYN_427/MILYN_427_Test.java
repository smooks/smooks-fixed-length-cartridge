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
package org.smooks.cartridges.fixedlength.MILYN_427;

import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.sax.SAXElement;
import org.smooks.delivery.sax.SAXVisitAfter;
import org.smooks.javabean.context.BeanContext;
import org.smooks.payload.StringResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Contributed by Clemens Fuchslocher.
 *  
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class MILYN_427_Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(MILYN_427_Test.class);

        @Test
	public void test() throws IOException, SAXException {
		Smooks smooks = null;

		try {
			smooks = new Smooks(getClass().getResourceAsStream("/MILYN_427/smooks-config.xml"));
			smooks.addVisitor(new SAXVisitAfter() {
				private Integer n = 0;

				public void visitAfter(final SAXElement element, final ExecutionContext execution) throws IOException {
					n++;

					LOGGER.info("n == " + n);
					LOGGER.info("element.getAttribute(\"number\") == " + element.getAttribute("number"));
					LOGGER.info("element.getAttribute(\"truncated\") == " + element.getAttribute("truncated"));

					Attributes attributes = element.getAttributes();
					assertNotNull(attributes);

					LOGGER.info("attributes.getIndex(\"number\") == " + attributes.getIndex("number"));
					LOGGER.info("attributes.getIndex(\"truncated\") == " + attributes.getIndex("truncated"));

					for (int n = 0; n < attributes.getLength(); n++) {
						LOGGER.info("attributes.getURI(" + n + ") == " + attributes.getURI(n));
						LOGGER.info("attributes.getLocalName(" + n + ") == " + attributes.getLocalName(n));
						LOGGER.info("attributes.getQName(" + n + ") == " + attributes.getQName(n));
						LOGGER.info("attributes.getType(" + n + ") == " + attributes.getType(n));
						LOGGER.info("attributes.getValue(" + n + ") == " + attributes.getValue(n));
					}

					BeanContext beans = execution.getBeanContext();
					assertNotNull(beans);

					Data data = (Data) beans.getBean("Data");
					assertNotNull(data);

					LOGGER.info("data.getNumber() == " + data.getNumber());
					LOGGER.info("data.getTruncated() == " + data.getTruncated());

					String number = element.getAttribute("number");
					assertNotNull("number == null", number);
					assertTrue("number.length() == 0", number.length() != 0);
					assertEquals(number, n.toString());

					String truncated = element.getAttribute("truncated");
					assertNotNull("truncated == null", truncated);
					if (n == 1 || n == 4 || n == 7) {
						assertEquals(truncated, "true");
					} else {
						assertEquals(truncated, "");
					}
				}
			}, "record");

			StringResult result = new StringResult();
			smooks.filterSource(smooks.createExecutionContext(), getSource(), result);
			LOGGER.info(result.toString());
		} finally {
			if (smooks != null) {
				smooks.close();
			}
		}
	}

	private Source getSource() {
		return new StreamSource(getClass().getResourceAsStream("/MILYN_427/data.flf"));
	}


}
