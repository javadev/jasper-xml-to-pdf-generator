/*
 * $Id$
 *
 * Copyright 2012 Valentyn Kolesnikov
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * UkrainianToLatin unit test.
 *
 * @author Valentyn Kolesnikov
 * @version $Revision$ $Date$
 */
public class UkrainianToLatinTest {
    /**
     * Checks string converter.
     */
    @Test public void generateLat() {
        assertEquals("", UkrainianToLatin.generateLat(""));
        assertEquals("abvhd", UkrainianToLatin.generateLat("абвгд"));
        assertEquals("a", UkrainianToLatin.generateLat("а"));
        assertEquals("B", UkrainianToLatin.generateLat("Б"));
        assertEquals("abvhd kh", UkrainianToLatin.generateLat("абвгд х"));
        assertEquals("abVhd KH", UkrainianToLatin.generateLat("абВгд Х"));
        assertEquals("abVhKH", UkrainianToLatin.generateLat("абВгХ"));
        assertEquals("abKhhKH", UkrainianToLatin.generateLat("абХгХ"));
        assertEquals("abvhd kh yulia", UkrainianToLatin.generateLat("абвгд х юля"));
        assertEquals("yizhak", UkrainianToLatin.generateLat("їжак"));
        assertEquals("Yizhak", UkrainianToLatin.generateLat("Їжак"));
        assertEquals("YI", UkrainianToLatin.generateLat("Ї"));
        assertEquals("aI", UkrainianToLatin.generateLat("аЇ"));
        assertEquals("SHCH", UkrainianToLatin.generateLat("Щ"));
        assertEquals("aSHCH", UkrainianToLatin.generateLat("аЩ"));
        assertEquals("ashchB", UkrainianToLatin.generateLat("ащБ"));
        assertEquals("ashchb", UkrainianToLatin.generateLat("ащб"));
        assertEquals("aSHCHB", UkrainianToLatin.generateLat("аЩБ"));
        assertEquals("aShchb", UkrainianToLatin.generateLat("аЩб"));
        assertEquals("shchB", UkrainianToLatin.generateLat("щБ"));
        assertEquals("SHCHB", UkrainianToLatin.generateLat("ЩБ"));
        assertEquals("yiZhak", UkrainianToLatin.generateLat("їЖак"));
        assertEquals("aIzhak", UkrainianToLatin.generateLat("аЇжак"));
        assertEquals("yizhaksiryi", UkrainianToLatin.generateLat("їжак-сірий"));
        assertEquals("Rozghon", UkrainianToLatin.generateLat("Розгон"));
        assertEquals("Zghorany", UkrainianToLatin.generateLat("Згорани"));
        assertEquals("ZGHorany", UkrainianToLatin.generateLat("ЗГорани"));
        assertEquals("aZGHorany", UkrainianToLatin.generateLat("аЗГорани"));
        assertEquals("Zghorany", UkrainianToLatin.generateLat("Згорани'"));
        assertEquals("Zghorany", UkrainianToLatin.generateLat("Згорани’"));
        assertEquals("Zghorany\nkh", UkrainianToLatin.generateLat("Згорани’\nх"));
        assertEquals("aZghorany\nkh", UkrainianToLatin.generateLat("аЗгорани’\nх"));
        new UkrainianToLatin();
    }

    private void equal(String actual, String expected) {
        assertEquals(expected, UkrainianToLatin.generateLat(actual));
    }

    /**.*/
    @Test public void ukrainianToLatin() {
//Аа Aа
        equal("Алушта Андрій", "Alushta Andrii");
//Бб Bb
        equal("Борщагівка Борисенко", "Borshchahivka Borysenko");
//Вв Vv
        equal("Вінниця Володимир", "Vinnytsia Volodymyr");
//Гг Hh
        equal("Гадяч Богдан Згурський", "Hadiach Bohdan Zghurskyi");
//Ґґ Gg
        equal("Ґалаґан Ґорґани", "Galagan Gorgany");
//Дд Dd
        equal("Донецьк Дмитро", "Donetsk Dmytro");
//Ее Eе
        equal("Рівне Олег Есмань", "Rivne Oleh Esman");
//Єє Ye ie на початку слова в інших позиціях
        equal("Єнакієве Гаєвич Короп’є", "Yenakiieve Haievych Koropie");
//Жж Zh zh
        equal("Житомир Жанна Жежелів", "Zhytomyr Zhanna Zhezheliv");
//Зз Zz
        equal("Закарпаття Казимирчук", "Zakarpattia Kazymyrchuk");
//Ии Yy
        equal("Медвин Михайленко", "Medvyn Mykhailenko");
//Іі Ii
        equal("Іванків Іващенко", "Ivankiv Ivashchenko");
//Її Yi i на початку слова в інших позиціях
        equal("Їжакевич Кадиївка Мар’їне", "Yizhakevych Kadyivka Marine");
//Йй Y i на початку слова в інших позиціях
        equal("Йосипівка Стрий Олексій", "Yosypivka Stryi Oleksii");
//Кк Kk
        equal("Київ Коваленко", "Kyiv Kovalenko");
//Лл Ll
        equal("Лебедин Леонід", "Lebedyn Leonid");
//Мм Mm
        equal("Миколаїв Маринич", "Mykolaiv Marynych");
//Нн Nn
        equal("Ніжин Наталія", "Nizhyn Nataliia");
//Оо Oo
        equal("Одеса Онищенко", "Odesa Onyshchenko");
//Пп Pp
        equal("Полтава Петро", "Poltava Petro");
//Рр Rr
        equal("Решетилівка Рибчинський", "Reshetylivka Rybchynskyi");
//Сс Ss
        equal("Суми Соломія", "Sumy Solomiia");
//Тт Tt
        equal("Тернопіль Троць", "Ternopil Trots");
//Уу Uu
        equal("Ужгород Уляна", "Uzhhorod Uliana");
//Фф Ff
        equal("Фастів Філіпчук", "Fastiv Filipchuk");
//Хх Kh kh
        equal("Харків Христина", "Kharkiv Khrystyna");
//Цц Ts ts
        equal("Біла Церква Стеценко", "Bila Tserkva Stetsenko");
//Чч Ch ch
        equal("Чернівці Шевченко", "Chernivtsi Shevchenko");
//Шш Sh sh
        equal("Шостка Кишеньки", "Shostka Kyshenky");
//Щщ Shch shch
        equal("Щербухи Гоща Гаращенко", "Shcherbukhy Hoshcha Harashchenko");
//Юю Yu іu на початку слова в інших позиціях
        equal("Юрій Корюківка", "Yurii Koriukivka");
//Яя Ya ia на початку слова в інших позиціях
        equal("Яготин Ярошенко Костянтин Знам’янка Феодосія", "Yahotyn Yaroshenko Kostiantyn Znamianka Feodosiia");
    }

    @Test
    public void main() {
        UkrainianToLatin.main(new String[] {});
    }
}
