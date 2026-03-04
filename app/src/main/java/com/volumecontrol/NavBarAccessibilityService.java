package com.volumecontrol;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

/**
 * NavBarAccessibilityService
 *
 * हे service Android च्या Accessibility feature वापरून
 * navigation bar मध्ये app shortcut देते.
 *
 * Enable करण्यासाठी:
 * Settings → Accessibility → Volume Control Nav Button → Enable करा
 *
 * त्यानंतर navigation bar मध्ये shortcut button दिसेल.
 */
public class NavBarAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Navigation events handle करतो
    }

    @Override
    public void onInterrupt() {
        // Service interrupted
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // Service connected - ready to use
    }

    /**
     * जेव्हा accessibility button press होतो तेव्हा हे trigger होते
     * (Android 9+ मध्ये accessibility button navigation bar मध्ये येतो)
     */
    @Override
    public void onAccessibilityButtonClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
