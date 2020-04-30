package com.exceptos.covidaid.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class state_model : Serializable {

    @SerializedName("Date")
    var Date: String? = ""

    @SerializedName("No_of_Deaths")
    var No_of_Deaths: String? = ""

    @SerializedName("No_of_Active_Cases")
    var No_of_Active_Cases: String? = ""

    @SerializedName("No_of_Lab_Confirmed_cases")
    var No_of_Lab_Confirmed_cases: String? = ""

    @SerializedName("No_Discharged")
    var No_Discharged: String? = null

    @SerializedName("State")
    var State: String? = null

}