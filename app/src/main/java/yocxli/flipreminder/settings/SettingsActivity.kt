package yocxli.flipreminder.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import yocxli.flipreminder.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SettingsFragment()).commit()

        OssLicensesMenuActivity.setActivityTitle(getString(R.string.settings_oss_license))
    }
}
