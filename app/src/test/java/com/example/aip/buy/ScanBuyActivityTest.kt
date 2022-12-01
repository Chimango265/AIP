package com.example.aip.buy

import android.view.View
import org.json.JSONArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanBuyActivityTest {

    @Test
    fun testExtractNameAndId() {
        val details =
            "03~|<MWI009DXGMJT5<<<<<<<<<<<<<<<<<<<~0101309M2701301MW|<<<<<<<<<<<8~NG'OMA<<CHIMANGO<<<<<<<<~NG'OMA" +
                    "~09DX6MJT~CHIMANGO~~Male~30 Jan 2001~19 Jul 2017~"
        assertEquals("NG'OMA CHIMANGO", (NameAndID().getUsefulStrings2(details)[0]).trim())
    }

}