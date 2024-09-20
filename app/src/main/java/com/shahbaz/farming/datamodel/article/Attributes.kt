import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attributes(
    val binomial_name: String = "",
    val common_names: List<String> = listOf(),
    val description: String = "",
    val growing_degree_days: Int = 0,
    val guides_count: Int = 0,
    val height: Int = 0,
    val main_image_path: String = "", // Default value provided
    val name: String = "", // Default value provided
    val processing_pictures: Int = 0,
    val row_spacing: Int = 0,
    val slug: String = "",
    val sowing_method: String = "",
    val spread: Int = 0,
    val sun_requirements: String = "",
    val svg_icon: String = "",
    val tags_array: List<String> = listOf(),
    val taxon: String = ""
): Parcelable
