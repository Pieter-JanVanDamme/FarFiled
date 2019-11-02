package be.pjvandamme.farfiled.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.pjvandamme.farfiled.databinding.ListItemRelationBinding
import be.pjvandamme.farfiled.model.Relation

/**
 * The RelationsListAdapter will take a list of Relations and adapt them into a Recyclerview to
 * show in the RelationsListFragment.
 */
class RelationsListAdapter(val clickListener: RelationsListListener):
        ListAdapter<Relation, RelationsListAdapter.ViewHolder>(RelationsListDiffCallback()){

    // Create the contentView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Fill the item with content and set a click listener.
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, clickListener)
    }

    /**
     * The class ViewHolder represents a ViewHolder for a Relation in the RelationsList
     * RecyclerView.
     */
    class ViewHolder private constructor (val binding: ListItemRelationBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRelationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        // bind the viewholder to the Relation
        fun bind(item: Relation, clickListener: RelationsListListener) {
            binding.relation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 */
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