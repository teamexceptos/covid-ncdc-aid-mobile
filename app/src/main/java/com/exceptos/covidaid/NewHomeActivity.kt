package com.exceptos.covidaid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.exceptos.covidaid.SharedprefManager.state_change_1
import com.exceptos.covidaid.SharedprefManager.state_change_2
import com.exceptos.covidaid.SharedprefManager.state_change_3
import com.exceptos.covidaid.SharedprefManager.state_change_4
import com.exceptos.covidaid.SharedprefManager.state_change_5
import com.exceptos.covidaid.models.ng_highlights
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_new_home.*

class NewHomeActivity : AppCompatActivity() {

    val mActivity: Activity = this@NewHomeActivity

    var ng : ng_model? = null

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.backgroundColor)

        setContentView(R.layout.activity_new_home)

        val Prefs = customPreference(this, "covid_data")

        if(haveNetworkConnection()) {

            val serviceAPI = ServiceAPI("", object : OnAPIDataGotten {

                override fun state_json_loaded(stateModel: state_model) {

                }

                override fun ng_json_loaded(ngModel: ng_model) {

                    if(!ngModel.No_Confirmed_cases.isNullOrEmpty()) {

                        progressBar.visibility = View.GONE

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

                                comfired_cases_perc.text = increase_decrease_format_with_perc((difference(new = turntoInt(ngModel.No_Confirmed_cases!!), old = Prefs.total_comfired_cases).toDouble()/100))
                                Prefs.total_comfired_cases_perc = comfired_cases_perc.text.toString()

                            } else {

                                comfired_cases_perc.text = Prefs.total_comfired_cases_perc
                            }

                            if(Prefs.total_active_cases != turntoInt(ngModel.Active_cases!!)) {

                                active_cases_perc.text = increase_decrease_format_with_perc((difference(new = turntoInt(ngModel.Active_cases!!), old = Prefs.total_active_cases).toDouble()/100))
                                Prefs.total_active_cases_perc = active_cases_perc.text.toString()

                            } else {

                                active_cases_perc.text = Prefs.total_active_cases_perc
                            }

                            if(Prefs.total_discharged != turntoInt(ngModel.No_Discharged!!)) {

                                discharged_perc.text = increase_decrease_format_with_perc((difference(new = turntoInt(ngModel.No_Discharged!!), old = Prefs.total_discharged).toDouble()/100))
                                Prefs.total_discharged_perc = discharged_perc.text.toString()

                            } else {

                                discharged_perc.text = Prefs.total_discharged_perc
                            }

                            if(Prefs.total_sample_tested != turntoInt(ngModel.No_Samples_Tested!!)) {

                                samples_tested_perc.text = increase_decrease_format_with_perc((difference(new = turntoInt(ngModel.No_Samples_Tested!!), old = Prefs.total_sample_tested).toDouble()/100))
                                Prefs.total_sample_tested_perc = samples_tested_perc.text.toString()

                            } else {

                                samples_tested_perc.text = Prefs.total_sample_tested_perc
                            }

                            if(Prefs.total_deaths != turntoInt(ngModel.No_of_Deaths!!)) {

                                death_perc.text = increase_decrease_format_with_perc((difference(new = turntoInt(ngModel.No_of_Deaths!!), old = Prefs.total_deaths).toDouble()/100))
                                Prefs.total_deaths_perc = death_perc.text.toString()

                            } else {

                                death_perc.text = Prefs.total_deaths_perc
                            }
                        }

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

            topStates("Lagos", state_1_new, state_1, Prefs!!.state_change_1)
            topStates("Kano", state_2_new, state_2, Prefs.state_change_2)
            topStates("FCT", state_3_new, state_3, Prefs.state_change_3)
            topStates("Katsina", state_4_new, state_4, Prefs.state_change_4)
            topStates("Borno", state_5_new, state_5, Prefs.state_change_5)

            home_highlights.setOnClickListener {

            showBottomSheetforHighlights()

            }

            all_states.setOnClickListener {
                startActivity(Intent(mActivity, AllStatesActivity::class.java))
            }

        } else {

            Toast.makeText(mActivity, "No internet connection, Connect and restart application", Toast.LENGTH_LONG).show()
        }

    }

    fun difference(old: Int, new: Int) : Int {
        return new - old
    }

    fun turntoInt(value : String) : Int {
        return value.trim().replace(",", "").toInt()
    }

    fun increase_decrease_format_with_perc(value: Double) : String {

        return if(value >= 0.0) {

            "+$value%"

        } else {

            "$value%"
        }
    }

    fun increase_decrease_format(value: Int) : String {

        return if(value >= 0) {

            "+$value"

        } else {

            "$value"
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun topStates(state: String, textView: TextView, labeltextView: TextView, oldValue: Int) {

        val Prefs = customPreference(this, "covid_data")

        val serviceAPI = ServiceAPI(state, object : OnAPIDataGotten {

            @SuppressLint("SetTextI18n")
            override fun state_json_loaded(stateModel: state_model) {

                labeltextView.text = state

                when (state) {
                    "Lagos" -> {

                        val diffValue = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)

                        if(diffValue != 0) {

                            Prefs!!.state_change_1 = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)
                            textView.text = increase_decrease_format(diffValue)

                            Log.i("lagos_data_change", diffValue.toString() + " "+ turntoInt(stateModel.No_of_Lab_Confirmed_cases!!))

                        } else {

                            textView.text = increase_decrease_format(diffValue)
                        }

                    }
                    "Kano" -> {

                        val diffValue = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)

                        if(diffValue != 0) {

                            Prefs!!.state_change_2 = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)
                            textView.text = increase_decrease_format(diffValue)

                        } else {

                            textView.text = increase_decrease_format(diffValue)
                        }

                    }
                    "FCT" -> {

                        val diffValue = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)

                        if(diffValue != 0) {

                            Prefs!!.state_change_3 = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)
                            textView.text = increase_decrease_format(diffValue)

                        } else {

                            textView.text = increase_decrease_format(diffValue)
                        }
                    }
                    "Katsina" -> {

                        val diffValue = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)

                        if(diffValue != 0) {

                            Prefs!!.state_change_4 = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)
                            textView.text = increase_decrease_format(diffValue)

                        } else {

                            textView.text = increase_decrease_format(diffValue)
                        }
                    }
                    "Borno" -> {

                        val diffValue = difference(new = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!), old = oldValue)

                        if(diffValue != 0) {

                            Prefs!!.state_change_5 = turntoInt(stateModel.No_of_Lab_Confirmed_cases!!)
                            textView.text = increase_decrease_format(diffValue)

                        } else {

                            textView.text = increase_decrease_format(diffValue)
                        }
                    }
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
                bottomSheetDialog.show(this@NewHomeActivity.mActivity.supportFragmentManager, ModalBottomSheet::class.java.simpleName)

            }

            override fun empty_json(string: String) {

            }
        })
        serviceAPIforHighlights.execute()

    }

}