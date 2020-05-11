package com.exceptos.covidaid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.exceptos.covidaid.SharedprefManager.customPreference
import com.exceptos.covidaid.SharedprefManager.total_active_cases
import com.exceptos.covidaid.SharedprefManager.total_comfired_cases
import com.exceptos.covidaid.SharedprefManager.total_sample_tested
import com.exceptos.covidaid.SharedprefManager.total_discharged
import com.exceptos.covidaid.SharedprefManager.total_deaths
import com.exceptos.covidaid.SharedprefManager.total_active_cases_perc
import com.exceptos.covidaid.SharedprefManager.total_comfired_cases_perc
import com.exceptos.covidaid.SharedprefManager.total_sample_tested_perc
import com.exceptos.covidaid.SharedprefManager.total_discharged_perc
import com.exceptos.covidaid.SharedprefManager.total_deaths_perc
import com.exceptos.covidaid.models.ng_highlights
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_new_home.*

class StatesActivity : AppCompatActivity() {

    val mActivity: Activity = this@StatesActivity

    var ng : ng_model? = null

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.holo_green_light)

        setContentView(R.layout.activity_new_home)

        val Prefs = customPreference(this, "covid_data")

        if(haveNetworkConnection()) {

            val serviceAPI = ServiceAPI("", object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                }

                override fun ng_json_loaded(ngModel: ng_model) {

                    if(!ngModel.No_Confirmed_cases.isNullOrEmpty()) {

                        ng = ngModel

                        if(Prefs!!.total_comfired_cases == 0) {

                            Prefs.total_active_cases = turntoInt(ngModel.Active_cases!!)
                            Prefs.total_discharged = turntoInt(ngModel.No_Discharged!!)
                            Prefs.total_sample_tested = turntoInt(ngModel.No_Samples_Tested!!)
                            Prefs.total_comfired_cases = turntoInt(ngModel.No_Confirmed_cases!!)
                            Prefs.total_deaths = turntoInt(ngModel.No_of_Deaths!!)

                            comfired_cases_perc.text = Prefs.total_comfired_cases_perc
                            active_cases_perc.text = Prefs.total_active_cases_perc
                            discharged_perc.text = Prefs.total_discharged_perc
                            samples_tested_perc.text = Prefs.total_sample_tested_perc
                            death_perc.text = Prefs.total_deaths_perc

                        } else {

                            if(Prefs.total_comfired_cases != turntoInt(ngModel.No_Confirmed_cases!!)) {

                                comfired_cases_perc.text = ((difference(new = turntoInt(ngModel.No_Confirmed_cases!!), old = Prefs.total_comfired_cases))/100).toString() + "%"
                                Prefs.total_comfired_cases_perc = comfired_cases_perc.text.toString()

                            } else {

                                comfired_cases_perc.text = Prefs.total_comfired_cases_perc
                            }

                            if(Prefs.total_active_cases != turntoInt(ngModel.Active_cases!!)) {

                                active_cases_perc.text = ((difference(new = turntoInt(ngModel.Active_cases!!), old = Prefs.total_active_cases))/100).toString() + "%"
                                Prefs.total_active_cases_perc = active_cases_perc.text.toString()

                            } else {

                                active_cases_perc.text = Prefs.total_active_cases_perc
                            }

                            if(Prefs.total_discharged != turntoInt(ngModel.No_Discharged!!)) {

                                discharged_perc.text = ((difference(new = turntoInt(ngModel.No_Discharged!!), old = Prefs.total_discharged))/100).toString() + "%"
                                Prefs.total_discharged_perc = discharged_perc.text.toString()

                            } else {

                                discharged_perc.text = Prefs.total_discharged_perc
                            }

                            if(Prefs.total_sample_tested != turntoInt(ngModel.No_Samples_Tested!!)) {

                                samples_tested_perc.text = ((difference(new = turntoInt(ngModel.No_Samples_Tested!!), old = Prefs.total_sample_tested))/100).toString() + "%"
                                Prefs.total_sample_tested_perc = samples_tested_perc.text.toString()

                            } else {

                                samples_tested_perc.text = Prefs.total_sample_tested_perc
                            }

                            if(Prefs.total_deaths != turntoInt(ngModel.No_of_Deaths!!)) {

                                death_perc.text = ((difference(new = turntoInt(ngModel.No_of_Deaths!!), old = Prefs.total_deaths))/100).toString() + "%"
                                Prefs.total_deaths_perc = death_perc.text.toString()

                            } else {

                                death_perc.text = Prefs.total_deaths_perc
                            }

                        }

//                        progress_ng_data.visibility = View.GONE
//                        total_ng_data.visibility = View.VISIBLE

                        current_update_date.text = ngModel.Date
                        discharged.text = ngModel.No_Discharged!!
                        samples_tested.text = ngModel.No_Samples_Tested!!
                        comfired_cases.text = ngModel.No_Confirmed_cases!!
                        death.text = ngModel.No_of_Deaths!!
                        active_cases.text = ngModel.Active_cases!!

                    }
                }

                override fun highlights_json_loaded(ngHighlights: ng_highlights) {

                }

                override fun empty_json(string: String) {

                }
            })
            serviceAPI.execute()

        } else {

            Toast.makeText(mActivity, "No internet connection, Connect and restart application", Toast.LENGTH_LONG).show()
        }

//        highlights.setOnClickListener {
//            showBottomSheetforHighlights()
//        }

    }

    fun difference(old: Int, new: Int) : Int {
        return new - old
    }

    fun turntoInt(value : String) : Int {
        return value.trim().replace(",", "").toInt()
    }

    fun topStates(state: String, textView: TextView, oldValue: Int) : Int {

        var newDifference = 0

        val serviceAPI = ServiceAPI(state, object : OnAPIDataGotten {

            override fun state_json_loaded(stateModel: state_model) {

                turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)

                if(oldValue == 0 ) {

                    newDifference = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)

                } else {
                    newDifference = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)
                }
            }

            override fun ng_json_loaded(ngModel: ng_model) {

            }

            override fun highlights_json_loaded(ngHighlights: ng_highlights) {

            }

            override fun empty_json(string: String) {

            }
        })
        serviceAPI.execute()

        return newDifference

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

    private fun showBottomSheetforHighlights() {

        val serviceAPIforHighlights = ServiceAPI("highlights", object : OnAPIDataGotten {

            override fun state_json_loaded(stateModel: state_model) {

            }

            override fun ng_json_loaded(ngModel: ng_model) {

            }

            override fun highlights_json_loaded(ngHighlights: ng_highlights) {

                mActivity as FragmentActivity

                val bottomSheetDialog = ModalBottomSheet(ngHighlights.highlights!!)
                bottomSheetDialog.show(this@StatesActivity.mActivity.supportFragmentManager, ModalBottomSheet::class.java.simpleName)

            }

            override fun empty_json(string: String) {

            }
        })
        serviceAPIforHighlights.execute()

    }

}