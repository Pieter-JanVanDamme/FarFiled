package be.pjvandamme.farfiled.presentation.relationdetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class EditRelationParcelable(
    val editName: String,
    val editSynopsis: String,
    val editNow: String,
    val editSelf: String,
    val editWork: String,
    val editHome: String,
    val editCircle: String,
    val editFun: String
): Parcelable