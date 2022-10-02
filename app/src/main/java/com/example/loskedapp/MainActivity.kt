package com.example.loskedapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isAccessibilityGranted(this)) {
            permissionAccessibility(this)
        } else {
            Log.d("ololo", "ooooooooo")
//                    startActivityForResult(AppLockActivity.instance(context, file.name, it), 10)
        }

            /*file.packageName?.let { it ->
                startActivityForResult(AppLockActivity.instance(context, file.name, it), 10)
            }*/

        val recyclerView: RecyclerView
        val adapter: RecyclerView.Adapter<*>
        val recyclerViewLayoutManager: RecyclerView.LayoutManager
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView

        // Passing the column number 1 to show online one column in each row.

        // Passing the column number 1 to show online one column in each row.
        recyclerViewLayoutManager = GridLayoutManager(this@MainActivity, 1)

        recyclerView.setLayoutManager(recyclerViewLayoutManager)

        adapter = AppsAdapter(
            this@MainActivity,
            ApkInfoExtractor(this@MainActivity).GetAllInstalledApkInfo()
        )

        recyclerView.setAdapter(adapter)
    }

    fun isAccessibilityGranted(context: Context): Boolean {
        var accessibilityEnabled = 0
        val service = context.packageName + "/" + AccessibilityService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                context.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                context.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun permissionAccessibility(context: Context) {
        AlertDialog.Builder(context, R.style.AlertDialogTheme)
            .setTitle("")
            .setView(
                LayoutInflater.from(context).inflate(
                    R.layout.view_dialog_permission_accessibility,
                    null,
                    false
                )
            )
            .setPositiveButton("R.string.give_permission") { dialog, which ->
                // Utils.reportEventClick("AppLock Screen", "AppLock_Permission_btn")
                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .create().show()
    }
}
