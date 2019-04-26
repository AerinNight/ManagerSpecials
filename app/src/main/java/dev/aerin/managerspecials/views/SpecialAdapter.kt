package dev.aerin.managerspecials.views

import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.aerin.managerspecials.R
import dev.aerin.managerspecials.databinding.ListItemSpecialBinding
import dev.aerin.managerspecials.models.Special
import dev.aerin.managerspecials.models.SpecialsPage

class SpecialAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HEADER = 0
        const val SPECIAL = 1
    }

    private var recycler: RecyclerView? = null

    private var idCounter = 0L
    private val idMap = HashMap<Special, Long>()

    var specialsPage: SpecialsPage? = null
        set(value) {
            field = value
            if (value != null) {
                for (special in value.managerSpecials) {
                    if (!idMap.containsKey(special)) {
                        idMap[special] = idCounter
                        idCounter++
                    }
                }
            }
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true) // This auto-enables some neat animations based on the IDs we're assigning and tracking
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recycler = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SPECIAL -> SpecialViewHolder.create(parent)
            else -> HeaderViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) { HEADER } else { SPECIAL }
    }

    override fun getItemCount(): Int {
        return (specialsPage?.managerSpecials?.size ?: 0) + 1 // default to 1 (header-only) if page is null
    }

    override fun getItemId(position: Int): Long {
        if (position == 0) return -1 // header

        return idMap[specialsPage!!.managerSpecials[position - 1]] ?: -2 // offset -1 due to header; -2 is an unused ID
    } // this won't get called if size=0 or specialsPage is null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == SPECIAL) {
            Log.d("Adapter", "Binding special: ${specialsPage!!.managerSpecials[position - 1]}")
            (holder as SpecialViewHolder).bind(
                specialsPage!!.getUnitSize(getNarrowDimensionSize()),
                specialsPage!!.managerSpecials[position - 1] // offset -1 due to header
            )
        }
    } // this won't get called if size=0 or specialsPage is null

    private fun getNarrowDimensionSize(): Int {
        if (recycler != null) {
            return if (recycler!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                 recycler!!.width
            } else { // == Configuration.ORIENTATION_LANDSCAPE
                recycler!!.height
            }
        }
        return 0
    }
}

class SpecialViewHolder private constructor(private val binding: ListItemSpecialBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): SpecialViewHolder {
            return SpecialViewHolder(
                ListItemSpecialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    fun bind(unitSize: Int, special: Special) {
        binding.width = unitSize * special.width
        binding.height = unitSize * special.height

        Log.d("Adapter", "Final bind size for special $special: ${binding.width} x ${binding.height}")

        binding.special = special
    }
}

class HeaderViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {

    companion object {
        fun create(parent: ViewGroup): HeaderViewHolder {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            )
        }
    }
}
