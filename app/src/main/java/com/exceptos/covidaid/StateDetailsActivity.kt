package com.exceptos.covidaid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_state_details.*
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.exceptos.covidaid.models.ng_highlights
import java.text.NumberFormat
import java.util.*


class StateDetailsActivity : AppCompatActivity() {

    val mActivity: Activity = this@StateDetailsActivity

    lateinit var discharged_cases_bar: CustomProgressBar
    lateinit var s_active_cases_bar: CustomProgressBar
    lateinit var total_comfirmed_bar: CustomProgressBar
    lateinit var deaths_bar: CustomProgressBar

    var ngModel: ng_model? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        setContentView(R.layout.activity_state_details)

        val state = intent.getStringExtra("state")


        if(intent.getSerializableExtra("ng") != null) {

            ngModel = intent.getSerializableExtra("ng") as ng_model

        }

        if(haveNetworkConnection()) {

            val serviceAPI = ServiceAPI(state!!, object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                    state_details_progress.visibility = View.GONE

                    discharged_cases_bar = findViewById(R.id.discharged_cases_bar)
                    s_active_cases_bar = findViewById(R.id.s_active_cases_bar)
                    total_comfirmed_bar = findViewById(R.id.total_comfirmed_bar)
                    deaths_bar = findViewById(R.id.deaths_bar)

                    discharged_cases_bar.setMaxValue(ngModel!!.No_Discharged!!.trim().replace(",", "").toInt())
                    s_active_cases_bar.setMaxValue(ngModel!!.No_Samples_Tested!!.trim().replace(",", "").toInt())
                    total_comfirmed_bar.setMaxValue(ngModel!!.No_Confirmed_cases!!.trim().replace(",", "").toInt())
                    deaths_bar.setMaxValue(ngModel!!.No_of_Deaths!!.trim().replace(",", "").toInt())

                    discharged_cases_bar.setProgressColor(getColor(android.R.color.holo_green_dark))
                    s_active_cases_bar.setProgressColor(getColor(android.R.color.holo_purple))
                    total_comfirmed_bar.setProgressColor(getColor(android.R.color.holo_orange_light))
                    deaths_bar.setProgressColor(getColor(android.R.color.holo_red_light))

                    discharged_cases_bar.setValue(stateModel.No_Discharged!!.trim().replace(",", "").toInt())
                    s_active_cases_bar.setValue(stateModel.No_of_Active_Cases!!.trim().replace(",", "").toInt())
                    total_comfirmed_bar.setValue(stateModel.No_of_Lab_Confirmed_cases!!.trim().replace(",", "").toInt())
                    deaths_bar.setValue(stateModel.No_of_Deaths!!.trim().replace(",", "").toInt())

                    if(state == "fct") {
                        searched_state.text = "FCT - Abuja"
                    } else {
                        searched_state.text = stateModel.State
                    }

                    s_discharged.text = stateModel.No_Discharged!!.trim()
                    s_active_cases.text = stateModel.No_of_Active_Cases!!.trim()
                    s_lab_confirmed_cases.text = stateModel.No_of_Lab_Confirmed_cases!!.trim()
                    s_deaths.text = stateModel.No_of_Deaths!!.trim()
                    latest_date.text = stateModel.Date!!.trim()

                }

                override fun ng_json_loaded(ngModel: ng_model) {


                }

                override fun highlights_json_loaded(ngHighlights: ng_highlights) {

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

        reduce_spread_guide.setOnClickListener {
            startActivity(Intent(mActivity, StoppingtheSpreadActivity::class.java))
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