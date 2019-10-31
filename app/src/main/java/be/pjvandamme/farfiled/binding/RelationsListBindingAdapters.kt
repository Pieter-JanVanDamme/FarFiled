package be.pjvandamme.farfiled.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.pjvandamme.farfiled.model.Relation

@BindingAdapter("relationName")
fun TextView.setRelationName(item: Relation?){
    item?.let{
        text = item.name
    }
}

@BindingAdapter("relationSynopsis")
fun TextView.setRelationSynopsis(item: Relation?){
    item?.let{
        text = item.synopsis
    }
}