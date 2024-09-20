package com.shahbaz.farming.util

import android.content.Context
import com.shahbaz.farming.R
import com.shahbaz.farming.datamodel.Article
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class Constant @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {

        const val ONBOARDING_PREFERENCES = "my_pref"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val WAETHER_API_KEY = "a4b16fd186efbbcad51d1fbcb7353798"
        const val WEATHER_BASE_URL = "https://api.openweathermap.org/"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val MARKET_PRICE_BASE_URL = "https://api.data.gov.in/"
        const val API_KEY_FOR_MARKET_PRICE =
            "579b464db66ec23bdd000001987c65666f9c49656f0f9ef4fa3650e7"
        const val NOTIFICATION_URL = "https://fcm.googleapis.com/v1/projects/kotlin-project-aef9a/"
        const val ARTICLE_BASE_URL = "https://openfarm.cc/api/v1/"
        const val GOOGLE_GEMINI_API_KEY="AIzaSyAcvWs7W-Uc0NPedJjtT3MbcXvw_mwhma4"


        val CATEGORY_LIST = ArrayList<String>(
            listOf(
                "All",
                "Fertilizers",
                "Pesticides",
                "Seeds",
                "Machinery"
            )
        )

        val articleList = ArrayList<Article>(
            listOf(
                Article(
                    id = UUID.randomUUID().toString(),
                    title = "Tomato(टमाटर)",
                    image = R.drawable.tomato
                ),
                Article(
                    id = UUID.randomUUID().toString(),
                    title = "Cabbage(पत्तागोभी)",
                    image = R.drawable.cabbage
                ),
                Article(
                    id = UUID.randomUUID().toString(),
                    title = "Potato(आलू)",
                    image = R.drawable.potato
                )
            )
        )

    }
}


