package com.shahbaz.farming.util

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class OuthToken {

    companion object {
        private const val firebaseMessagingScope =
            "https://www.googleapis.com/auth/firebase.messaging"

        fun getAccessToken(): String? {
            try {
                Log.d("Access Token", "started")
                val jsonString = "{\n" +
                        "  \"type\": \"service_account\",\n" +
                        "  \"project_id\": \"kotlin-project-aef9a\",\n" +
                        "  \"private_key_id\": \"7658e1303fb93a6a89e7fd4df2b524b5f199ae06\",\n" +
                        "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwPQR4fhEQz7Wz\\nuz0U0GCKrbCDBLPN1sjhfg4586Sk+zCvyTxc/jMprGHy+RgyI8iZp+B0+BwwtSSl\\naJkFzLy1sTYUxYVptNpeMNAkdr+7hVFO7lDx9zQ/YDTdD+xJzn/rayYXZLnBJ+CX\\nGOLJ9AMx99qi+yVnMmuYVBUCOluQ0aAlmMvMIRV+oc48SXz4PjCKnDKTH08G6tIp\\nmPpIAmhq/H46h626QLnSyLlOQ0JeCi1d4NcJu8yZTvTBrLxnr0GNSqWJHIB0QG/V\\nZXPVqX1sUKL1uP/SXY8iNvEnZzYngPIfF93vZoSIT4s9ZKi21FYDRPI3jUXpIjy8\\noxBlClWjAgMBAAECggEAOOm7g93STQeGuiABPbN9OcuVcPoXxqVSAd/fYD+aL90e\\nf+YQpEdC5nZQu1JNygNcSKK6DYwCLoiQ6Qm6QPE5D8PGnzaaOuS4kCpbTSs8B6ap\\n9z9mzHz0VnMPH0SBHeSd0ryrdGqGwiXKlYDn3Cf80ComZPONxKG3tV2R20JVR5Kf\\nvJcR+LAtPq+mAQzoKSzfqqYga2w5nbxyGCoQt/dNm9i6LAY/KuGcCSlf4du5JMOo\\npumckie+LunMYlNraeKGxCRBkG9yS7/547VsnlhuBgfjJD64YAyPiJNZ1BApfqF5\\na6xWrkUf83NgBZu7b91fLOFG2J4A7byv6GbgMzWXoQKBgQD35vk0D3w+8EsZKKvL\\n3C5stpajEbRJMjJWQgtJIKuEnIe4sqmAXH5w8a1zz9l5K0wybcflecDFQNNzYDof\\nIenlG0OifLHENB1hCSeXeUs3tINNCDCJFP98eA7ooNe7SN/ZLb25BP+uTJvP8Nht\\nGfnSrOmHJdm4ZGFtbb7oNpUalQKBgQC1/sVZhJbU7Lmk9M77A+fKH7iXwq4q0i4C\\nyrbz8o1Z7BZZk6skTgZTZL4SN0sfXUMRTv/jDM7A0WT3iHKX4tm5EY+5fxMs8ls2\\nPBDDTPgtcqNfXHSFqCn35QE/pGOsbfSPLfTASywSXhx9TqGbILC4iFUVT0izvr7/\\n68Rcgf7ZVwKBgCctPFLK47K6nNOkBspXyIprrJCmupwar7PMCiaLYC+74QMCUSE0\\n/NJyjxvPTCoG+qq5iK6uOBW0MfZ4zZaf8GvBjEXAeQNtzPrJzJT2mTl7cIia87im\\nNd4AxzJiQNWbJa0XunTj4lg0SSdqg3lycnWl194A0FCApJj7kAD4p0WlAoGBAKtF\\nPp3W3Ec3HCHzzNGHRFv/7CzNpjBgKbKw6Om8am+kcQgNovKAhw118jWcWZMDpnkk\\nekVHKU9l/3m/q+/qKXEz6KjtwLL8xP/OGQxiY/AJQVwd2v5pWk2S8tDcuHxt4cfY\\nvMs2Mxx5+p+mHwmpAwY2LrPmMAoPyRl43AsevyHzAoGBAMebiF5TYjcl8AzV33Uz\\nS5vyss3sH+PUcdzeA4pUfu8oW3c6DUZ4sGhYsWcjajUlGuc+V08cdTXdjoSmPxtI\\nCBIhp8wI8wJ8shYHbUTiKoRsaXqgiuGO+KcLhykzhxtZ/ZqyNa70+7p2817T6l+R\\n1BnPhSRAMOYk4vynBZL+a10C\\n-----END PRIVATE KEY-----\\n\",\n" +
                        "  \"client_email\": \"firebase-adminsdk-c1os4@kotlin-project-aef9a.iam.gserviceaccount.com\",\n" +
                        "  \"client_id\": \"114768839506794481707\",\n" +
                        "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                        "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                        "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                        "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-c1os4%40kotlin-project-aef9a.iam.gserviceaccount.com\",\n" +
                        "  \"universe_domain\": \"googleapis.com\"\n" +
                        "}\n"


                Log.d("Access Token", "jsonString: $jsonString")
                val stream: InputStream =
                    ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
                val googleCredentails =
                    GoogleCredentials.fromStream(stream).createScoped(listOf(firebaseMessagingScope))
                googleCredentails.refresh()
                Log.d("Access Token value", googleCredentails.accessToken.tokenValue)
                return googleCredentails.accessToken.tokenValue
            } catch (e: Exception) {
                Log.e("AccessToken", "getAccessToken: ", e)
                return null
            }
        }
    }
}