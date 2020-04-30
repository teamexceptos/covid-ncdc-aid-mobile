package com.exceptos.covidaid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_state.*

class StatesActivity : AppCompatActivity() {

    val mActivity: Activity = this@StatesActivity
    private var mAdapter: MainAdapter? = null
    var ng : ng_model? = null

    val ng_states = listOf(
        "Abia",
        "Adamawa",
        "Akwa Ibom",
        "Anambra",
        "Bauchi",
        "Bayelsa",
        "Benue",
        "Borno",
        "Rivers",
        "Delta",
        "Ebonyi",
        "Edo",
        "Ekiti",
        "Enugu",
        "Abuja",
        "Gombe",
        "Imo",
        "Jigawa",
        "Kaduna",
        "Kano",
        "Katsina",
        "Kebbi",
        "Kogi",
        "Kwara",
        "Lagos",
        "Nasarawa",
        "Niger",
        "Ogun",
        "Ondo",
        "Osun",
        "Oyo",
        "Plateau",
        "Rivers",
        "Sokoto",
        "Taraba",
        "Yobe",
        "Zamfara"
    )

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)

        setContentView(R.layout.activity_state)

        if(haveNetworkConnection()) {


            val serviceAPI = ServiceAPI("", object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                }

                override fun ng_json_loaded(ngModel: ng_model) {

                    ng = ngModel

                    progress_ng_data.visibility = View.GONE
                    ng_data_view.visibility = View.VISIBLE

                    current_update_date.text = ngModel.Date
                    discharged_cases.text = ngModel.No_Discharged!!
                    total_tested_sample.text = ngModel.No_Samples_Tested!!
                    total_comfirmed.text = ngModel.No_Confirmed_cases!!
                    deaths.text = ngModel.No_of_Deaths!!

                }

                override fun empty_json(string: String) {

                }
            })
            serviceAPI.execute()

        } else {

            Toast.makeText(mActivity, "No internet connection, Connect and restart application", Toast.LENGTH_LONG).show()
        }

        initialize_rv(ArrayList(ng_states))

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

    private fun initialize_rv(array: ArrayList<String>) {

        state_lists.visibility = View.VISIBLE
        state_lists.setHasFixedSize(true)
        state_lists.layoutManager = LinearLayoutManager(baseContext)
        mAdapter = MainAdapter(baseContext, array)

        state_lists.addOnItemTouchListener(RecyclerItemClickListener(baseContext, state_lists, object : RecyclerItemClickListener.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                if(progress_ng_data.visibility != View.VISIBLE) {

                    val intent = Intent(mActivity, StateDetailsActivity::class.java)
                    intent.putExtra("ng", ng)
                    intent.putExtra("state", ArrayList(ng_states)[position])
                    startActivity(intent)
                } else {

                    Toast.makeText(mActivity, "NCDC Data is still loading, Check your connection or wait alittle", Toast.LENGTH_LONG).show()
                }

            }

            override fun onLongItemClick(view: View, position: Int) {

            }
        }))

        state_lists.adapter = mAdapter
    }

}