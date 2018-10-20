package yocxli.flipreminder.settings


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import yocxli.flipreminder.BuildConfig
import yocxli.flipreminder.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val appVersion = preferenceScreen.findPreference("app_version")
        appVersion?.summary = BuildConfig.VERSION_NAME
    }

}
