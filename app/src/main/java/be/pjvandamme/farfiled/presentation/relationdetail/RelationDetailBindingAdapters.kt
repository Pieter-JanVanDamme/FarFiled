package be.pjvandamme.farfiled.presentation.relationdetail

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.models.LifeArea
import be.pjvandamme.farfiled.models.RelationLifeArea
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_relation_detail.*

@BindingAdapter("relationLifeAreaNow")
fun TextView.setLifeAreaNowText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.EPHEMERA){
                text = it.content
            }
        }
    }
}

@BindingAdapter("relationLifeAreaSelf")
fun TextView.setLifeAreaSelfText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.PERSONAL){
                text = it.content
            }
        }
    }
}

@BindingAdapter("relationLifeAreaWork")
fun TextView.setLifeAreaWorkText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.VOCATION){
                text = it.content
            }
        }
    }
}

@BindingAdapter("relationLifeAreaHome")
fun TextView.setLifeAreaHomeText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.DOMESTIC){
                text = it.content
            }
        }
    }
}

@BindingAdapter("relationLifeAreaCircle")
fun TextView.setLifeAreaCircleText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.COMMUNITY){
                text = it.content
            }
        }
    }
}

@BindingAdapter("relationLifeAreaFun")
fun TextView.setLifeAreaFunText(item: List<RelationLifeArea?>?){
    item?.let{
        item.forEach{
            if(it?.lifeArea == LifeArea.LEISURE){
                text = it.content
            }
        }
    }
}

@BindingAdapter("avatarUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let{
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

//@BindingAdapter("editTextChangedListener")
//fun bindTextWatcher(editText: EditText, number: Int){
//    editText.addTextChangedListener(object: TextWatcher{
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
//        override fun afterTextChanged(s: Editable?) {
//            // de bindingadapter kent het viewmodel niet...
//            relationDetailViewModel.onEditRelation(
//                relationNameEditText.text.toString(),
//                relationSynopsisEditText.text.toString(),
//                lifeAreaNowEditText.text.toString(),
//                lifeAreaSelfEditText.text.toString(),
//                lifeAreaWorkEditText.text.toString(),
//                lifeAreaHomeEditText.text.toString(),
//                lifeAreaCircleEditText.text.toString(),
//                lifeAreaFunEditText.text.toString()
//            )
//        }
//    })
//}