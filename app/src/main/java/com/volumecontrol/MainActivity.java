package com.volumecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private AudioManager audioManager;
    private LinearLayout btnRinging, btnVibration, btnSilent, btnDND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        showVolumeControlDialog();
    }

    private void showVolumeControlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.RoundedDialog);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_volume_control, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        // Power buttons
        LinearLayout btnAirplane = dialogView.findViewById(R.id.btnAirplane);
        LinearLayout btnReboot = dialogView.findViewById(R.id.btnReboot);
        LinearLayout btnShutdown = dialogView.findViewById(R.id.btnShutdown);
        btnAirplane.setOnClickListener(v -> toggleAirplaneMode());
        btnReboot.setOnClickListener(v -> rebootDevice());
        btnShutdown.setOnClickListener(v -> shutdownDevice());

        // Ringer mode buttons
        btnRinging = dialogView.findViewById(R.id.btnRinging);
        btnVibration = dialogView.findViewById(R.id.btnVibration);
        btnSilent = dialogView.findViewById(R.id.btnSilent);
        btnDND = dialogView.findViewById(R.id.btnDND);
        updateModeButtons();
        btnRinging.setOnClickListener(v -> { audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL); updateModeButtons(); });
        btnVibration.setOnClickListener(v -> { audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE); updateModeButtons(); });
        btnSilent.setOnClickListener(v -> { audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT); updateModeButtons(); });
        btnDND.setOnClickListener(v -> openDNDSettings());

        // Volume sliders
        setupSlider(dialogView, R.id.seekRinger, R.id.tvRingerVal, AudioManager.STREAM_RING);
        setupSlider(dialogView, R.id.seekNotification, R.id.tvNotifVal, AudioManager.STREAM_NOTIFICATION);
        setupSlider(dialogView, R.id.seekMedia, R.id.tvMediaVal, AudioManager.STREAM_MUSIC);
        setupSlider(dialogView, R.id.seekAlarm, R.id.tvAlarmVal, AudioManager.STREAM_ALARM);
        setupSlider(dialogView, R.id.seekCall, R.id.tvCallVal, AudioManager.STREAM_VOICE_CALL);
        setupSlider(dialogView, R.id.seekSystem, R.id.tvSystemVal, AudioManager.STREAM_SYSTEM);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setDimAmount(0.6f);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int)(getResources().getDisplayMetrics().widthPixels * 0.92f);
            dialog.getWindow().setAttributes(lp);
        }
        dialog.setOnDismissListener(d -> finish());
        dialog.show();
    }

    private void setupSlider(View root, int seekId, int tvId, int streamType) {
        SeekBar seekBar = root.findViewById(seekId);
        TextView tvVal = root.findViewById(tvId);
        int max = audioManager.getStreamMaxVolume(streamType);
        int current = audioManager.getStreamVolume(streamType);
        seekBar.setMax(max);
        seekBar.setProgress(current);
        tvVal.setText((int)((current / (float)max) * 100) + "%");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean user) {
                if (user) {
                    audioManager.setStreamVolume(streamType, p, 0);
                    tvVal.setText((int)((p / (float)max) * 100) + "%");
                }
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private void updateModeButtons() {
        int mode = audioManager.getRingerMode();
        setActive(btnRinging, mode == AudioManager.RINGER_MODE_NORMAL);
        setActive(btnVibration, mode == AudioManager.RINGER_MODE_VIBRATE);
        setActive(btnSilent, mode == AudioManager.RINGER_MODE_SILENT);
        setActive(btnDND, false);
    }

    private void setActive(LinearLayout btn, boolean active) {
        btn.setBackgroundResource(active ? R.drawable.mode_btn_active : R.drawable.mode_btn_normal);
        int color = active ? 0xFFFFFFFF : 0xFF444444;
        for (int i = 0; i < btn.getChildCount(); i++) {
            View c = btn.getChildAt(i);
            if (c instanceof TextView) ((TextView)c).setTextColor(color);
        }
    }

    private void toggleAirplaneMode() {
        startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
    }

    private void rebootDevice() {
        new AlertDialog.Builder(this)
            .setTitle("Reboot करायचे का?")
            .setMessage("Device restart होईल.")
            .setPositiveButton("Reboot", (d, w) -> {
                try {
                    ((PowerManager)getSystemService(POWER_SERVICE)).reboot(null);
                } catch (Exception e) {
                    try { Runtime.getRuntime().exec(new String[]{"su","-c","reboot"}); }
                    catch (Exception ex) { Toast.makeText(this, "Root access आवश्यक आहे", Toast.LENGTH_SHORT).show(); }
                }
            })
            .setNegativeButton("Cancel", null).show();
    }

    private void shutdownDevice() {
        new AlertDialog.Builder(this)
            .setTitle("Shutdown करायचे का?")
            .setMessage("Device बंद होईल.")
            .setPositiveButton("Shutdown", (d, w) -> {
                try { Runtime.getRuntime().exec(new String[]{"su","-c","poweroff"}); }
                catch (Exception e) { Toast.makeText(this, "Root access आवश्यक आहे", Toast.LENGTH_SHORT).show(); }
            })
            .setNegativeButton("Cancel", null).show();
    }

    private void openDNDSettings() {
        try { startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)); }
        catch (Exception e) { Toast.makeText(this, "DND Settings उघडत आहे...", Toast.LENGTH_SHORT).show(); }
    }
}
