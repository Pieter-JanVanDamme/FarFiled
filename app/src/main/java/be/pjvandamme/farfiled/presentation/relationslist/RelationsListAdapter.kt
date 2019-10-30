package be.pjvandamme.farfiled.presentation.relationslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.pjvandamme.farfiled.databinding.ListItemRelationBinding
import be.pjvandamme.farfiled.models.Relation

class RelationsListAdapter(val clickListener: RelationsListListener):
        ListAdapter<Relation, RelationsListAdapter.ViewHolder>(RelationsListDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor (val binding: ListItemRelationBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRelationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Relation, clickListener: RelationsListListener) {
            binding.relation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class RelationsListDiffCallback: DiffUtil.ItemCallback<Relation>(){
    override fun areItemsTheSame(oldItem: Relation, newItem: Relation): Boolean {
        return oldItem.relationId == newItem.relationId
    }

    override fun areContentsTheSame(oldItem: Relation, newItem: Relation): Boolean {
        return oldItem == newItem
    }
}

class RelationsListListener(val clickListener: (relationId: Long) -> Unit){
    fun onClick(relation: Relation) = clickListener(relation.relationId)
}