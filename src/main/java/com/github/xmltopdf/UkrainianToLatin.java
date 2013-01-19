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

import java.util.HashMap;
import java.util.Map;

/**
 * UkrainianToLatin utility class.
 *
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class UkrainianToLatin {
    private static final int INDEX_0 = 0;
    private static final int INDEX_1 = 1;
    private static final int INDEX_2 = 2;
    private static final int INDEX_3 = 3;
    private static final int INDEX_4 = 4;
    private static final int INDEX_8 = 8;
    private static final int LENGTH_2 = 2;
    private static final int LENGTH_3 = 3;
    private static final int LENGTH_4 = 4;
    private static final int LENGTH_8 = 8;

    private enum Convert {
        AA("Аа"),
        BB("Бб"),
        VV("Вв"),
        HH("Гг"),
        GG("Ґґ"),
        DD("Дд"),
        EE("Ее"),
        YeIe("Єє"),
        ZhZh("Жж"),
        ZZ("Зз"),
        YY("Ии"),
        II("Іі"),
        YiI("Її"),
        YI("Йй"),
        KK("Кк"),
        LL("Лл"),
        MM("Мм"),
        NN("Нн"),
        OO("Оо"),
        PP("Пп"),
        RR("Рр"),
        SS("Сс"),
        TT("Тт"),
        UU("Уу"),
        FF("Фф"),
        KhKh("Хх"),
        TsTs("Цц"),
        ChCh("Чч"),
        ShSh("Шш"),
        ShchShch("Щщ"),
        YuIu("Юю"),
        YaIa("Яя");
        private String cyrilic;
        private Convert(String cyrilic) {
            this.cyrilic = cyrilic;
        }
        /**
         * Gets cyrilic.
         * @return the cyrilic
         */
        public String getCyrilic() {
            return cyrilic;
        }

    }
    private static Map<String, ConvertCase> cyrToLat;

    private static class ConvertCase {
        private final Convert convert;
        private final boolean lowcase;
        public ConvertCase(Convert convert, boolean lowcase) {
            this.convert = convert;
            this.lowcase = lowcase;
        }
        public Convert getConvert() {
            return convert;
        }
        public boolean isLowcase() {
            return lowcase;
        }
    }

    static {
        cyrToLat = new HashMap<String, ConvertCase>();
        for (Convert convert : Convert.values()) {
            cyrToLat.put(convert.getCyrilic().substring(INDEX_0, INDEX_1), new ConvertCase(convert, false));
            cyrToLat.put(convert.getCyrilic().substring(INDEX_1, INDEX_2), new ConvertCase(convert, true));
            if (convert == Convert.EE) {
                cyrToLat.put("Ё", new ConvertCase(convert, false));
                cyrToLat.put("ё", new ConvertCase(convert, true));
            }
        }
    }
    private UkrainianToLatin() {
        super();
    }

    /**
     * Generates latinic from cyrilic.
     * @param name the name
     * @return the result
     */
    public static String generateLat(String name) {
        StringBuffer result = new StringBuffer();
        ConvertCase prevConvertCase = null;
        for (int index = 0; index < name.length(); index += 1) {
            String curChar = name.substring(index, index + INDEX_1);
            String nextChar = index == name.length() - 1 ? null : name.substring(index + INDEX_1, index + INDEX_2);
            if (curChar.matches("[-'’,]")) {
                continue;
            }
            if (cyrToLat.get(curChar) == null) {
                if (" ".equals(curChar)) {
                    prevConvertCase = null;
                    result.append(' ');
                }
                continue;
            }
            ConvertCase convertCase = cyrToLat.get(curChar);
            if (prevConvertCase == null) {
                checkFirstChar(result, convertCase, cyrToLat.get(nextChar) == null ? convertCase : cyrToLat
                        .get(nextChar));
            } else {
                checkMiddleChar(result, convertCase, cyrToLat.get(nextChar) == null ? convertCase : cyrToLat
                        .get(nextChar));
            }
            prevConvertCase = convertCase;
        }
        return result.toString();
    }

    /**
     * @param result
     * @param convertCase
     * @param prevChar
     */
    private static void checkFirstChar(StringBuffer result, ConvertCase convertCase, ConvertCase nextConvertCase) {
        String latName = convertCase.getConvert().name();
        switch (latName.length()) {
        case LENGTH_2:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_1).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_0, INDEX_1) : latName.substring(INDEX_0, INDEX_1)
                    .toUpperCase());
            if (convertCase.getConvert() == Convert.ZZ && nextConvertCase.getConvert() == Convert.HH) {
                result.append(nextConvertCase.isLowcase() ? "g" : "G");
            }
            break;
        case LENGTH_3:
        case LENGTH_4:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_2).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_0, INDEX_2) : latName.substring(INDEX_0, INDEX_2)
                    .toUpperCase());
            break;
        case LENGTH_8:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_4).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_0, INDEX_4) : latName.substring(INDEX_0, INDEX_4)
                    .toUpperCase());
            break;
        default:
            break;
        }
    }

    /**
     * @param result
     * @param convertCase
     * @param prevChar
     */
    private static void checkMiddleChar(StringBuffer result, ConvertCase convertCase, ConvertCase nextConvertCase) {
        String latName = convertCase.getConvert().name();
        switch (latName.length()) {
        case LENGTH_2:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_1, INDEX_2).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_1, INDEX_2) : latName.substring(INDEX_1, INDEX_2)
                    .toUpperCase());
            if (convertCase.getConvert() == Convert.ZZ && nextConvertCase.getConvert() == Convert.HH) {
                result.append(nextConvertCase.isLowcase() ? "g" : "G");
            }
            break;
        case LENGTH_3:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_2, INDEX_3).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_2, INDEX_3) : latName.substring(INDEX_2, INDEX_3)
                    .toUpperCase());
            break;
        case LENGTH_4:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_2, INDEX_4).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_2, INDEX_4) : latName.substring(INDEX_2, INDEX_4)
                    .toUpperCase());
            break;
        case LENGTH_8:
            result.append(convertCase.isLowcase() ? latName.substring(INDEX_4, INDEX_8).toLowerCase() : nextConvertCase
                    .isLowcase() ? latName.substring(INDEX_4, INDEX_8) : latName.substring(INDEX_4, INDEX_8)
                    .toUpperCase());
            break;
        default:
            break;
        }
    }
}
