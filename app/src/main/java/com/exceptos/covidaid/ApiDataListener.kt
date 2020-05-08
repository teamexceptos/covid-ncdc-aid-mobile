package com.exceptos.covidaid
import com.exceptos.covidaid.models.ng_highlights
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model

interface OnAPIDataGotten {

    fun state_json_loaded(stateModel: state_model)
    fun ng_json_loaded(ngModel: ng_model)
    fun highlights_json_loaded(ngHighlights: ng_highlights)
    fun empty_json(string: String)
}