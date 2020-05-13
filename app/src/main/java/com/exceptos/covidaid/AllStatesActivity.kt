package com.exceptos.covidaid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceptos.covidaid.models.ng_model
import kotlinx.android.synthetic.main.all_states.*

class AllStatesActivity : AppCompatActivity() {

    val mActivity: Activity = this@AllStatesActivity
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

        setContentView(R.layout.all_states)

        initialize_rv(ArrayList(ng_states))

    }

    private fun initialize_rv(array: ArrayList<String>) {

        state_lists.visibility = View.VISIBLE
        state_lists.setHasFixedSize(true)
        state_lists.layoutManager = LinearLayoutManager(baseContext)
        mAdapter = MainAdapter(baseContext, array)

        state_lists.addOnItemTouchListener(RecyclerItemClickListener(baseContext, state_lists, object : RecyclerItemClickListener.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                val intent = Intent(mActivity, NewStateDetailsActivity::class.java)

                if(ArrayList(ng_states)[position] == "FCT - Abuja") {

                    intent.putExtra("state", "fct")
                    startActivity(intent)

                } else  {

                    intent.putExtra("state", ArrayList(ng_states)[position])
                    startActivity(intent)
                }

            }

            override fun onLongItemClick(view: View, position: Int) {

            }
        }))

        state_lists.adapter = mAdapter
    }

}