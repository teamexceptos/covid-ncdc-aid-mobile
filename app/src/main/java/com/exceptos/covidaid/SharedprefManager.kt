package com.exceptos.covidaid

import android.content.Context
import android.content.SharedPreferences

object SharedprefManager {

    val TCC = "TC"
    val TAC = "TAC"
    val TD = "TD"
    val TDT = "TDT"
    val TS = "TS"

    fun customPreference(context: Context, name: String):
            SharedPreferences? = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    var SharedPreferences.total_comfired_cases
        get() = getInt(TCC, 0)
        set(value) {
            editMe {
                it.putInt(TCC, value)
            }
        }

    var SharedPreferences.total_comfired_cases_perc
        get() = getString("cc_perc", "+0%")
        set(value) {
            editMe {
                it.putString("cc_perc", value)
            }
        }

    var SharedPreferences.total_active_cases
        get() = getInt(TAC, 0)
        set(value) {
            editMe {
                it.putInt(TAC, value)
            }
        }

    var SharedPreferences.total_active_cases_perc
        get() = getString("ac_perc", "+0%")
        set(value) {
            editMe {
                it.putString("ac_perc", value)
            }
        }

    var SharedPreferences.total_discharged
        get() = getInt(TD, 0)
        set(value) {
            editMe {
                it.putInt(TD, value)
            }
        }

    var SharedPreferences.total_discharged_perc
        get() = getString("dc_perc", "+0%")
        set(value) {
            editMe {
                it.putString("dc_perc", value)
            }
        }

    var SharedPreferences.total_sample_tested
        get() = getInt(TS, 0)
        set(value) {
            editMe {
                it.putInt(TS, value)
            }
        }

    var SharedPreferences.total_sample_tested_perc
        get() = getString("sc_perc", "+0%")
        set(value) {
            editMe {
                it.putString("sc_perc", value)
            }
        }

    var SharedPreferences.total_deaths
        get() = getInt(TDT, 0)
        set(value) {
            editMe {
                it.putInt(TDT, value)
            }
        }

    var SharedPreferences.total_deaths_perc
        get() = getString("dtc_perc", "+0%")
        set(value) {
            editMe {
                it.putString("dtc_perc", value)
            }
        }

    var SharedPreferences.state_change_1
        get() = getInt("sc_1", 0)
        set(value) {
            editMe {
                it.putInt("sc_1", value)
            }
        }

    var SharedPreferences.state_change_2
        get() = getInt("sc_2", 0)
        set(value) {
            editMe {
                it.putInt("sc_2", value)
            }
        }

    var SharedPreferences.state_change_3
        get() = getInt("sc_3", 0)
        set(value) {
            editMe {
                it.putInt("sc_3", value)
            }
        }

    var SharedPreferences.state_change_4
        get() = getInt("sc_4", 0)
        set(value) {
            editMe {
                it.putInt("sc_4", value)
            }
        }

    var SharedPreferences.state_change_5
        get() = getInt("sc_5", 0)
        set(value) {
            editMe {
                it.putInt("sc_5", value)
            }
        }

    var SharedPreferences.state_change_6
        get() = getInt("sc_6", 0)
        set(value) {
            editMe {
                it.putInt("sc_6", value)
            }
        }

    var SharedPreferences.state_change_7
        get() = getInt("sc_7", 0)
        set(value) {
            editMe {
                it.putInt("sc_7", value)
            }
        }

    var SharedPreferences.state_change_8
        get() = getInt("sc_8", 0)
        set(value) {
            editMe {
                it.putInt("sc_8", value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {

                it.clear()
            }
        }
}