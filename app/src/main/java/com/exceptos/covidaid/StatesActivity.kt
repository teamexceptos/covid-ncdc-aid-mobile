package com.exceptos.covidaid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceptos.covidaid.models.ng_highlights
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import kotlinx.android.synthetic.main.activity_state.*
import kotlinx.android.synthetic.main.ng_total_details_counts.*
import org.jetbrains.anko.doAsync

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
        "FCT - Abuja",
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

                    if(!ngModel.No_Confirmed_cases.isNullOrEmpty()) {

                        ng = ngModel

                        progress_ng_data.visibility = View.GONE
                        total_ng_data.visibility = View.VISIBLE

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

        initialize_rv(ArrayList(ng_states))

        highlights.setOnClickListener {
            showBottomSheetforHighlights()
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

    private fun initialize_rv(array: ArrayList<String>) {

        state_lists.visibility = View.VISIBLE
        state_lists.setHasFixedSize(true)
        state_lists.layoutManager = LinearLayoutManager(baseContext)
        mAdapter = MainAdapter(baseContext, array)

        state_lists.addOnItemTouchListener(RecyclerItemClickListener(baseContext, state_lists, object : RecyclerItemClickListener.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                if(progress_ng_data.visibility != View.VISIBLE) {

                    val intent = Intent(mActivity, StateDetailsActivity::class.java)

                    if(ng != null) {
                        intent.putExtra("ng", ng)
                    }

                    if(ArrayList(ng_states)[position] == "FCT - Abuja") {

                        intent.putExtra("state", "fct")
                        startActivity(intent)

                    } else  {

                        intent.putExtra("state", ArrayList(ng_states)[position])
                        startActivity(intent)
                    }

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