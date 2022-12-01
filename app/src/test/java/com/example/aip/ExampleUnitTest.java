package com.example.aip;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.aip.buy.ScanBuyActivity;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 );
    }

    @Test
    public void testRegex() {
        String details = "03~|<MWI009DXGMJT5<<<<<<<<<<<<<<<<<<<~0101309M2701301MW|<<<<<<<<<<<8~NG'OMA<<CHIMANGO<<<<<<<<~NG'OMA" +
                "~09DX6MJT~CHIMANGO~~Male~30 Jan 2001~19 Jul 2017~";
        assertEquals("09DX6MJT", new ScanBuyActivity().getUsefulStrings2(details)[0]);

    }
}