package be.pjvandamme.farfiled.model

import com.squareup.moshi.Json

class AdorableAvatarFeatures(
    @Json(name = "face") val features: FacialFeatures
){
    override fun toString(): String{
        return features.toString()
    }
}

class FacialFeatures(
    val eyes: List<String>,
    val nose: List<String>,
    val mouth: List<String>
){
        val COLOR_PALETTE = listOf(
            /* source: https://www.color-hex.com/color-palette/83425 */
            "FFE476",
            "84BEFF",
            "9BC382",
            "3A9870",
            "4D4746"
        )
    override fun toString(): String{
        var response: String = "Available facial features for avatar are:\n\tEyes – "
        eyes.forEach{
            response += "$it "
        }

        response += "\n\tNoses – "
        nose.forEach{
            response += "$it "
        }

        response+= "\n\tMouths – "
        mouth.forEach{
            response += "$it "
        }
        return response
    }
}

