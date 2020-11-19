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

import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.SmooksUtil;
import org.smooks.cdr.ParameterAccessor;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.Filter;
import org.smooks.payload.JavaResult;

import javax.xml.transform.stream.StreamSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:maurice.zeijen@smies.com">maurice.zeijen@smies.com</a>
 */
public class FixedLenghtReaderTest {
    @Test
    public void test_01_xml_map_binding() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-01-map.xml"));
        test_01_map_binding(smooks);
    }

    @Test
    public void test_01_programmatic_map_binding() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "firstname[10]?right_trim,lastname[10].trim.capitalize,$ignore$[2],gender[1],age[3],country[3]lower_case")
                .setBinding(
                        new FixedLengthBinding("people", HashMap.class, FixedLengthBindingType.MAP)
                                .setKeyField("firstname")));
        
        test_01_map_binding(smooks);
    }

    private void test_01_map_binding(Smooks smooks) {
        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("/input-message-01.txt")), result);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> people = (Map<String, Map<String, String>>) result.getBean("people");
        Map<String, String> person;

        person = people.get("Maurice");
        assertEquals("Maurice", person.get("firstname"));
        assertEquals("Zeijen", person.get("lastname"));
        assertEquals("M", person.get("gender"));
        assertEquals("026", person.get("age"));
        assertEquals("nld", person.get("country"));

        person = people.get("Sanne");
        assertEquals("Sanne", person.get("firstname"));
        assertEquals("Fries", person.get("lastname"));
        assertEquals("F", person.get("gender"));
        assertEquals("022", person.get("age"));
        assertEquals("nld", person.get("country"));
    }

    @Test
    public void test_01_xml_list_binding() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-01-list.xml"));
        test_01_list_binding(smooks);
    }

    @Test
    public void test_01_programmatic_list_binding() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "firstname[10].right_trim,lastname[10].trim.capitalize,$ignore$[2],gender[1],age[3],country[3]lower_case")
                .setBinding(new FixedLengthBinding("people", HashMap.class, FixedLengthBindingType.LIST)));
        
        test_01_list_binding(smooks);
    }

    private void test_01_list_binding(Smooks smooks) {
        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("/input-message-01.txt")), result);

        @SuppressWarnings("unchecked")
        List<Map<String, String>> people = (List<Map<String, String>>) result.getBean("people");
        Map<String, String> person;

        person = people.get(0);
        assertEquals("Maurice", person.get("firstname"));
        assertEquals("Zeijen", person.get("lastname"));
        assertEquals("M", person.get("gender"));
        assertEquals("026", person.get("age"));
        assertEquals("nld", person.get("country"));

        person = people.get(1);
        assertEquals("Sanne", person.get("firstname"));
        assertEquals("Fries", person.get("lastname"));
        assertEquals("F", person.get("gender"));
        assertEquals("022", person.get("age"));
        assertEquals("nld", person.get("country"));
    }

    @Test
    public void test_01_xml_single_binding() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-01-single.xml"));
        test_01_single_binding(smooks);
    }

    @Test
    public void test_01_programmatic_single_binding() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "firstname[10].right_trim,lastname[10].trim.capitalize,$ignore$[2],gender[1],age[3],country[3]lower_case")
                .setBinding(new FixedLengthBinding("person", HashMap.class, FixedLengthBindingType.SINGLE)));
        
        test_01_single_binding(smooks);
    }

    private void test_01_single_binding(Smooks smooks) {
        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("/input-message-01.txt")), result);

        @SuppressWarnings("unchecked")
        Map<String, String> person = (Map<String, String>) result.getBean("person");

        assertEquals("Sanne", person.get("firstname"));
        assertEquals("Fries", person.get("lastname"));
        assertEquals("F", person.get("gender"));
        assertEquals("022", person.get("age"));
        assertEquals("nld", person.get("country"));
    }

    @Test
    public void test_02_xml_skip_lines_line_number() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-02.xml"));
        test_02(smooks);
    }

    @Test
    public void test_02_programmatic_skip_lines_line_number() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
                .setSkipLines(2)
                .setLineNumber(true));
        
        test_02(smooks);
    }

    private void test_02(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-02.txt"), smooks);

        String expected = "<set><record number=\"3\"><first>aa</first><second>bbb</second><third>cccc</third></record><record number=\"4\"><first>dd</first><second>eee</second><third>ffff</third></record></set>";
        assertEquals(expected, result);
    }

    @Test
    public void test_03_xml_element_names() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-03.xml"));
        test_03(smooks);
    }

    @Test
    public void test_03_programmatic_element_names() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
                .setRootElementName("root-element")
                .setRecordElementName("record-element")
        );
        
        test_03(smooks);
    }

    private void test_03(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-03.txt"), smooks);

        String expected = "<root-element><record-element><first>aa</first><second>bbb</second><third>cccc</third></record-element><record-element><first>dd</first><second>eee</second><third>ffff</third></record-element></root-element>";
        assertEquals(expected, result);
    }

    @Test
    public void test_04_xml_truncate() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-04-truncate.xml"));
        test_04_truncate(smooks);
    }

    @Test
    public void test_04_programmatic_truncate() throws Exception {
        Smooks smooks = new Smooks();
        ParameterAccessor.setParameter(Filter.CLOSE_EMPTY_ELEMENTS, "true", smooks);

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
                .setStrict(false)
        );
        
        test_04_truncate(smooks);
    }

    private void test_04_truncate(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-04.txt"), smooks);

        String expected = "<set><record><first>aa</first><second>bbb</second><third>cccc</third></record><record truncated=\"true\"><first>dd</first><second truncated=\"true\" /><third truncated=\"true\" /></record></set>";
        assertEquals(expected, result);
    }

    @Test
    public void test_04_xml_truncate_line_number_diff_name() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-04-truncate-line-number-diff-name.xml"));
        test_04_truncate_line_number_diff_name(smooks);
    }

    @Test
    public void test_04_programmatic_truncate_line_number_diff_name() throws Exception {
        Smooks smooks = new Smooks();
        ParameterAccessor.setParameter(Filter.CLOSE_EMPTY_ELEMENTS, "true", smooks);
        
        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
                .setStrict(false)
                .setLineNumber(true)
                .setLineNumberAttributeName("nr")
                .setTruncatedAttributeName("trunc")
        );
        
        test_04_truncate_line_number_diff_name(smooks);
    }

    private void test_04_truncate_line_number_diff_name(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-04.txt"), smooks);

        String expected = "<set><record nr=\"1\"><first>aa</first><second>bbb</second><third>cccc</third></record><record nr=\"2\" trunc=\"true\"><first>dd</first><second trunc=\"true\" /><third trunc=\"true\" /></record></set>";
        assertEquals(expected, result);
    }

    @Test
    public void test_04_xml_strict() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-04-strict.xml"));
        test_04_strict(smooks);
    }

    @Test
    public void test_04_programmatic_strict() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
        );
        
        test_04_strict(smooks);
    }

    private void test_04_strict(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-04.txt"), smooks);

        String expected = "<set><record><first>aa</first><second>bbb</second><third>cccc</third></record></set>";
        assertEquals(expected, result);
    }

    @Test
    public void test_05_xml_indent() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-05.xml"));
        test_05_indent(smooks);
    }

    @Test
    public void test_05_programmatic_indent() throws Exception {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new FixedLengthReaderConfigurator(
                "first[2],second[3],third[4]")
                .setIndent(true)
        );
        
        test_05_indent(smooks);
    }

    private void test_05_indent(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-05.txt"), smooks);

        String expected =
                "<set>\n" +
                        "\t<record>\n" +
                        "\t\t<first>aa</first>\n" +
                        "\t\t<second>bbb</second>\n" +
                        "\t\t<third>cccc</third>\n" +
                        "\t</record>\n" +
                        "\t<record>\n" +
                        "\t\t<first>dd</first>\n" +
                        "\t\t<second>eee</second>\n" +
                        "\t\t<third>ffff</third>\n" +
                        "\t</record>\n" +
                        "</set>";

        assertEquals(expected, result);
    }

    @Test
    public void test_06_xml_profiles() throws Exception {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("/smooks-config-06.xml"));
        test_06_profiles(smooks);
    }

    private void test_06_profiles(Smooks smooks) {
        ExecutionContext context = smooks.createExecutionContext("A");

        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-06.txt"), smooks);

        String expected = "<set><record><first>aa</first><second>bbb</second></record></set>";
        assertEquals(expected, result);

        context = smooks.createExecutionContext("B");

        result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("/input-message-06.txt"), smooks);

        expected = "<set><record truncated=\"true\"><first>aa</first><second>bbb</second><third truncated=\"true\" /></record></set>";
        assertEquals(expected, result);
    }
}
