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
            (holder as SpecialViewHolder).bind(
                specialsPage!!.getUnitSize(getNarrowDimensionSize()),
                specialsPage!!.managerSpecials[position - 1] // offset -1 due to header
            )
        }
    } // this won't get called if size=0 or specialsPage is null

    /**
     * Returns the attached RecyclerView's narrowest dimension, i.e. width in portrait and height in landscape.
     * We'll use this to calculate the unit size of our UI, and assign the tiles' dimensions from there.
     */
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
/*
    I don't much like how Kotlin makes this pattern look, but essentially I've done this because the ViewHolder
    constructor requires the View tree's root element, calling the super-constructor is required before doing anything
    else, and attempting to in-line the view creation in the super-constructor call is even messier.

    The way this pattern works is to make the constructor private and force creation through a companion function,
    similar to Java's static methods. The companion function takes only the parent view, and constructs a fully-
    functional element child, thus encapsulating all of the "view" logic within the ViewHolder, all it needs is the
    element data provided to it. Then it passes the constructed view through the ViewHolder constructor, satisfying the
    API requirements.

    While doing things this way instead of making the Adapter inflate the View tree for a cleaner ViewHolder class makes
    somewhat less sense for this adapter than others, this pattern becomes a life-saver if you're working with 3+ data
    types all in the same recycler, such as a feed with text-only, photo, and video elements that each require separate
    handling logic.
 */
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
        binding.special = special
    }
}
/*
    At least the super-simple text-only ViewHolders are nice and clean.  :D
 */
class HeaderViewHolder private constructor(root: View) : RecyclerView.ViewHolder(root) {

    companion object {
        fun create(parent: ViewGroup): HeaderViewHolder {
            return HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            )
        }
    }
}
