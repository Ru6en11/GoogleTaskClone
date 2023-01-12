package com.example.googletaskclonepro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foundation.ActivityScopeViewModel
import com.example.foundation.navigator.IntermediateNavigator
import com.example.foundation.navigator.StackFragmentNavigator
import com.example.foundation.uiactions.AndroidUiActions
import com.example.foundation.utils.viewModeCreator
import com.example.foundation.views.FragmentHolder
import com.example.googletaskclonepro.databinding.ActivityMainBinding
import com.example.googletaskclonepro.views.tasks.TasksFragment

class MainActivity : AppCompatActivity(), FragmentHolder {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigator: StackFragmentNavigator

    private val activityViewModel by viewModeCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiAction = AndroidUiActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragment_container,
            defaultTitle = getString(R.string.main_title),
            animRes = StackFragmentNavigator.Animation(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            ),
            initialScreenCreator = { TasksFragment.Screen() }
        )

        navigator.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        super.onPause()
        activityViewModel.navigator.setTarget(null)
    }

    override fun notifyScreenUpdates() {
        navigator.notifyScreenUpdates()
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }


}