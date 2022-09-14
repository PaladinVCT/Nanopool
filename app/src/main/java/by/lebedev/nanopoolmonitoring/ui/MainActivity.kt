package by.lebedev.nanopoolmonitoring.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.findNavController
import by.lebedev.nanopoolmonitoring.R
import com.airbnb.epoxy.EpoxyDataBindingPattern
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@EpoxyDataBindingPattern(rClass = R::class, layoutPrefix = "view_holder")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val navHostFragment =
            findViewById<FragmentContainerView>(R.id.nav_host_fragment).getFragment<Fragment>()
        if (navHostFragment.childFragmentManager.backStackEntryCount > 0) {
            navHostFragment.findNavController().navigateUp()
        } else {
            super.onBackPressed()
        }
    }
}