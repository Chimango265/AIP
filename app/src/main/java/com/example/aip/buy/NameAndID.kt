package com.example.aip.buy

class NameAndID {

    fun getUsefulStrings(text: String): String {
        val pattern = "<*";

        val newText = text.replace(pattern.toRegex(), "")

        return newText

    }

    fun getUsefulStrings2(text2: String): Array<String> {
        var text = getUsefulStrings(text2)
        var i: Int = text.length
        var beneficiaryInfo = arrayOf("", " ")
        var name = " "
        var nationalId = ""
        var occur = 0

        for(j in 0..i) {
            if (text.get(j) == '~') {
                occur++;
            }
            if(occur == 4) {
                name += text[j]
            } else if(occur == 5) {
                nationalId += text[j]
            } else if (occur == 6) {
                name = " " + name + text[j]
            } else if (occur == 7) {
                break
            }
        }

        var pattern = "~+"
        name = name.replace(pattern.toRegex(), " ")
        nationalId = nationalId.replace(pattern.toRegex(), " ")

        beneficiaryInfo[0] = name
        beneficiaryInfo[1] = nationalId

        return beneficiaryInfo
    }
}