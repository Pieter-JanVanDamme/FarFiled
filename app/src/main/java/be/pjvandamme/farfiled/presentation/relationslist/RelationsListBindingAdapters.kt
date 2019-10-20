package be.pjvandamme.farfiled.presentation.relationslist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.pjvandamme.farfiled.domain.Relation

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