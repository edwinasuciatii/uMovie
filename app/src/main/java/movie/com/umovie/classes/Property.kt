package movie.com.umovie.classes

import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


class PropertyStatusBar{
    @Suppress("DEPRECATION")
    fun changeColor(color: Int, activity: FragmentActivity){
        val window: Window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(activity, color)
        }
    }
}
class PropertyToast{
    fun toast(fragmentActivity: FragmentActivity?, setToast: String?){
        return android.widget.Toast.makeText(
            fragmentActivity,
            setToast.toString(),
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}