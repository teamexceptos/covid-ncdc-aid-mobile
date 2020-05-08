package com.exceptos.covidaid.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ng_model : Serializable {

    @SerializedName("Date")
    var Date: String? = ""

    @SerializedName("Death")
    var No_of_Deaths: String? = ""

    @SerializedName("Total_Samples_Tested")
    var No_Samples_Tested: String? = ""

    @SerializedName("Total_Confirmed_cases")
    var No_Confirmed_cases: String? = ""

    @SerializedName("Discharged")
    var No_Discharged: String? = ""

    @SerializedName("Active_Cases")
    var Active_cases: String? = ""

    @SerializedName("message")
    var msg: String? = ""

}