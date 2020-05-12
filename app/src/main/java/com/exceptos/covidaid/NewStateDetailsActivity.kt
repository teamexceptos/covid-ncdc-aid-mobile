package com.exceptos.covidaid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_new_state_details.*
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.exceptos.covidaid.models.ng_highlights


class NewStateDetailsActivity : AppCompatActivity() {

    val mActivity: Activity = this@NewStateDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        setContentView(R.layout.activity_new_state_details)

        val state = intent.getStringExtra("state")

        if(state == "fct") {

            searched_state.text = "FCT - Abuja"

        } else {
            searched_state.text = state
        }

        if(haveNetworkConnection()) {

            val serviceAPI = ServiceAPI(state!!, object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                    state_details_progress.visibility = View.GONE

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