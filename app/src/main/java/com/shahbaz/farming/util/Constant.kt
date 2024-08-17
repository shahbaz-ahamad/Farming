package com.shahbaz.farming.util

class Constant {

    companion object{

        const val ONBOARDING_PREFERENCES ="my_pref"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val WAETHER_API_KEY="a4b16fd186efbbcad51d1fbcb7353798"
        const val WEATHER_BASE_URL="https://api.openweathermap.org/"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001


         val CATEGORY_LIST = ArrayList<String>(
             listOf(
                 "All",
                 "Fertilizers",
                 "Pesticides",
                 "Seeds",
                 "Machinery"
             )
         )

    }
}