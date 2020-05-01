package com.exceptos.covidaid

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.exceptos.covidaid.models.ng_highlights
import com.exceptos.covidaid.models.ng_model
import com.exceptos.covidaid.models.state_model
import com.exceptos.covidaid.models.total_model
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ServiceAPI(search_value: String, private val listener: OnAPIDataGotten): AsyncTask<Void, Void, String>() {

    val url = com.exceptos.covidaid.url
    val s_value = search_value

    override fun doInBackground(vararg params: Void?): String? {

        var result = ""
        val Url: URL?

        try {

            Url = if(s_value == "highlights") {

                URL("$url/$s_value")

            } else {

                if (s_value.isNotEmpty()) {

                    URL("$url/search/$s_value")

                } else {

                    URL("$url")
                }
            }

            val connection = Url.openConnection() as HttpURLConnection
            val `is` = connection.inputStream
            val br = BufferedReader(InputStreamReader(`is`, "utf-8"), 8)
            val sBuilder = StringBuilder()

            sBuilder.append(br.readLine())

            `is`.close()
            result = sBuilder.toString()

        } catch (e: Exception) {

            e.printStackTrace()
        }

        return result
    }

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        Log.i("api_result", result.toString())

        if(s_value.isNotEmpty()) {

            if(result!!.isNotEmpty()) {

                when (s_value) {

                    "total" -> {

                        val totalModel: total_model = Gson().fromJson(result.toString(), total_model::class.java)
                        listener.total_json_loaded(totalModel)
                    }

                    "highlights" -> {

                        val ngHighlights: ng_highlights = Gson().fromJson(result.toString(), ng_highlights::class.java)
                        listener.highlights_json_loaded(ngHighlights)
                    }

                    else -> {

                        val stateModel: state_model = Gson().fromJson(result.toString(), state_model::class.java)
                        listener.state_json_loaded(stateModel)
                    }
                }

            } else {

                listener.empty_json("No case has been reported")
            }

        } else {

            if(result!!.isNotEmpty()) {

                val ngModel: ng_model = Gson().fromJson(result.toString(), ng_model::class.java)
                listener.ng_json_loaded(ngModel)

            } else {

                listener.empty_json("No response made yet")
            }

        }

    }
}
