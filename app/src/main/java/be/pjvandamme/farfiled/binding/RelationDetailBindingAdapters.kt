package be.pjvandamme.farfiled.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import be.pjvandamme.farfiled.R
import be.pjvandamme.farfiled.model.LifeArea
import be.pjvandamme.farfiled.model.RelationLifeArea
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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