package programmer.laboratore_6.Service;

/**
 * Created by Byambaa on 11/15/2015.
 */
import android.app.KeyguardManager;
import android.os.PowerManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class ScreenNotificationListenerService extends NotificationListenerService {

    /** Wake the device lock screen for 5 seconds. */
    private static final long WAKE_TIME = 5000L;

    /**
     * When receiving a new notification, if lock screen is activated and power save mode is not on,
     * wake the screen of the device to show the newly posted notification for a specified time.
     */
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isPowerSaveMode()) {
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "ScreenNotificationsLock");
                wakeLock.acquire(WAKE_TIME);
            }
        }
        super.onNotificationPosted(statusBarNotification);
    }

}