package com.exceptos.covidaid
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import com.exceptos.covidaid.models.total_model

interface OnAPIDataGotten {

    fun state_json_loaded(stateModel: state_model)
    fun ng_json_loaded(ngModel: ng_model)
    fun total_json_loaded(totalModel: total_model)
    fun empty_json(string: String)
}