package dev.aerin.managerspecials

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dev.aerin.managerspecials.databinding.ActivityMainBinding
import dev.aerin.managerspecials.network.SpecialsService
import dev.aerin.managerspecials.views.SpecialAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SpecialAdapter

    private val specialsService by lazy {
        ViewModelProviders.of(this).get(SpecialsService::class.java)
    }
    private lateinit var errorPipe: Disposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // The one downside to RecyclerViews and LayoutManagers is you can't style the LayoutManager directly in XML.
        val layoutManager = binding.productList.layoutManager as FlexboxLayoutManager
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER

        adapter = SpecialAdapter()
        binding.productList.adapter = adapter

        // Every time we get an updated payload, push it to the adapter
        specialsService.getSpecialsData().observe(this, Observer { specialsPage ->
            binding.refreshLayout.isRefreshing = false
            adapter.specialsPage = specialsPage
        })

        // Just takes network error messages and surfaces them through a dialog.
        errorPipe = specialsService.getErrorPipe().subscribeOn(AndroidSchedulers.mainThread()).subscribe { error ->
            binding.refreshLayout.isRefreshing = false
            AlertDialog.Builder(this)
                .setTitle(R.string.error_title)
                .setMessage(error)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }

        // This listener firing means the user pulled to refresh the specials list
        binding.refreshLayout.setOnRefreshListener {
            specialsService.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        errorPipe.dispose()
    }
}
