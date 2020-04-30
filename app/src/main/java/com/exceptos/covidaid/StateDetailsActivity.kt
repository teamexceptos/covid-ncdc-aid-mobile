package com.exceptos.covidaid

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_state_details.*
import java.util.Collections.replaceAll
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.exceptos.covidaid.models.total_model


class StateDetailsActivity : AppCompatActivity() {

    val mActivity: Activity = this@StateDetailsActivity

    lateinit var discharged_cases_bar: CustomProgressBar
    lateinit var s_active_cases_bar: CustomProgressBar
    lateinit var total_comfirmed_bar: CustomProgressBar
    lateinit var deaths_bar: CustomProgressBar

    var ngModel: ng_model? = null
    var totalModel: total_model? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        setContentView(R.layout.activity_state_details)

        val state = intent.getStringExtra("state")


        if(intent.getSerializableExtra("ng") != null) {

            ngModel = intent.getSerializableExtra("ng") as ng_model

        } else {

            totalModel = intent.getSerializableExtra("ttl") as total_model

        }

        if(haveNetworkConnection()) {

            val serviceAPI = ServiceAPI(state!!, object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                    discharged_cases_bar = findViewById(R.id.discharged_cases_bar)
                    s_active_cases_bar = findViewById(R.id.s_active_cases_bar)
                    total_comfirmed_bar = findViewById(R.id.total_comfirmed_bar)
                    deaths_bar = findViewById(R.id.deaths_bar)

                    if(ngModel != null) {

                        discharged_cases_bar.setMaxValue(ngModel!!.No_Discharged!!.toInt())
                        s_active_cases_bar.setMaxValue(ngModel!!.No_Samples_Tested!!.trim().replace(",", "").split(" ")[1].toInt())
                        total_comfirmed_bar.setMaxValue(ngModel!!.No_Confirmed_cases!!.toInt())
                        deaths_bar.setMaxValue(ngModel!!.No_of_Deaths!!.toInt())

                    } else {

                        discharged_cases_bar.setMaxValue(totalModel!!.No_Discharged!!.trim().toInt())
                        s_active_cases_bar.setMaxValue(totalModel!!.No_of_Active_Cases!!.trim().replace(",", "").toInt())
                        total_comfirmed_bar.setMaxValue(totalModel!!.No_of_Lab_Confirmed_cases!!.trim().toInt())
                        deaths_bar.setMaxValue(totalModel!!.No_of_Deaths!!.toInt())
                    }


                    discharged_cases_bar.setProgressColor(getColor(android.R.color.holo_green_dark))
                    s_active_cases_bar.setProgressColor(getColor(android.R.color.holo_purple))
                    total_comfirmed_bar.setProgressColor(getColor(android.R.color.holo_orange_light))
                    deaths_bar.setProgressColor(getColor(android.R.color.holo_red_light))

                    discharged_cases_bar.setValue(stateModel.No_Discharged!!.trim().toInt())
                    s_active_cases_bar.setValue(stateModel.No_of_Active_Cases!!.trim().toInt())
                    total_comfirmed_bar.setValue(stateModel.No_of_Lab_Confirmed_cases!!.trim().toInt())
                    deaths_bar.setValue(stateModel.No_of_Deaths!!.trim().toInt())

                    searched_state.text = stateModel.State
                    s_discharged.text = stateModel.No_Discharged!!.trim()
                    s_active_cases.text = stateModel.No_of_Active_Cases!!.trim()
                    s_lab_confirmed_cases.text = stateModel.No_of_Lab_Confirmed_cases!!.trim()
                    s_deaths.text = stateModel.No_of_Deaths!!.trim()
                    latest_date.text = stateModel.Date!!.trim()

                }

                override fun ng_json_loaded(ngModel: ng_model) {


                }

                override fun total_json_loaded(totalModel: total_model) {

                }

                override fun empty_json(string: String) {

                    Toast.makeText(mActivity, string, Toast.LENGTH_LONG).show()

                }
            })
            serviceAPI.execute()

        } else {

            Toast.makeText(mActivity, "No internet connection, Connect and restart application", Toast.LENGTH_LONG).show()
        }

        back_pressed.setOnClickListener {

            onBackPressed()
            finish()
        }

    }

    fun haveNetworkConnection(): Boolean {

        var haveConnectedWifi = false
        var haveConnectedMobile = false

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedMobile = true
        }

        return haveConnectedWifi || haveConnectedMobile
    }

}