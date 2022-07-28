package com.exs.medivelskinmeasure.Device.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.exs.medivelskinmeasure.R;
import com.exs.medivelskinmeasure.UI.Main.Connection.DeviceInfoActivity;
import com.exs.medivelskinmeasure.common.CommonUtil;

public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {

    public static String ArgDeviceAddress = "deviceAddress";
    public static String ArgDeviceName = "deviceName";

    private DeviceInfoActivity mActivity;

    private enum Connected {False, Pending, True}

    private String deviceAddress;
    private String deviceName;

    private SerialService service;

    private TextView receiveText;
    private TextView sendText;
    private TextUtil.HexWatcher hexWatcher;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;
    private boolean pendingNewline = false;
    private String newline = TextUtil.newline_crlf;


    private String mStrData;

    /*
     * Lifecycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("Debug", "onCreate");
        mActivity = (DeviceInfoActivity) getActivity();

        setHasOptionsMenu(true);
        setRetainInstance(true);
        deviceAddress = getArguments().getString(ArgDeviceAddress);
        deviceName = getArguments().getString(ArgDeviceName);
    }

    @Override
    public void onDestroy() {
        Log.e("Debug", "onDestroy");

        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        Log.e("Debug", "onStart");

        super.onStart();
        if (service != null)
            service.attach(this);
        else
            getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        Log.e("Debug", "onStop");

        if (service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @SuppressWarnings("deprecation")
    // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(@NonNull Activity activity) {
        Log.e("Debug", "onAttach");

        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        Log.e("Debug", "onDetach");

        try {
            getActivity().unbindService(this);
        } catch (Exception ignored) {
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        Log.e("Debug", "onResume");

        super.onResume();
        if (initialStart && service != null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Log.e("Debug", "onserviceConnected");

        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e("Debug", "onserviceDisconnected");
        service = null;
    }

    /*
     * UI
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Debug", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        receiveText = view.findViewById(R.id.receive_text);                          // TextView performance decreases with number of spans
        receiveText.setTextColor(getResources().getColor(R.color.colorRecieveText)); // set as default color to reduce number of spans
        receiveText.setMovementMethod(ScrollingMovementMethod.getInstance());

        sendText = view.findViewById(R.id.send_text);
        hexWatcher = new TextUtil.HexWatcher(sendText);
        hexWatcher.enable(hexEnabled);
        sendText.addTextChangedListener(hexWatcher);
        sendText.setHint(hexEnabled ? "HEX mode" : "");

        if (mStrData != null && !mStrData.isEmpty()) {
            sendText.setText(mStrData);
        }

        View sendBtn = view.findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(v -> send(sendText.getText().toString()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        Log.e("Debug", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_terminal, menu);
        menu.findItem(R.id.hex).setChecked(hexEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("Debug", "onOptionsItemSelected");

        int id = item.getItemId();
        if (id == R.id.clear) {
            receiveText.setText("");
            return true;
        } else if (id == R.id.newline) {
//            String[] newlineNames = getResources().getStringArray(R.array.newline_names);
//            String[] newlineValues = getResources().getStringArray(R.array.newline_values);
//            int pos = java.util.Arrays.asList(newlineValues).indexOf(newline);
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("Newline");
//            builder.setSingleChoiceItems(newlineNames, pos, (dialog, item1) -> {
//                newline = newlineValues[item1];
//                dialog.dismiss();
//            });
//            builder.create().show();
            return true;
        } else if (id == R.id.hex) {
            hexEnabled = !hexEnabled;
            sendText.setText("");
            hexWatcher.enable(hexEnabled);
            sendText.setHint(hexEnabled ? "HEX mode" : "");
            item.setChecked(hexEnabled);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void setSendText(String text) {

        // 시 전송 로직을 바로 실행해야함.
        mStrData = text;

        if (sendText != null) {
            sendText.setText(text);

            connect();
        }
    }

    /*
     * Serial + UI
     */
    private void connect() {
        Log.e("Debug", "connect");

        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");
            mActivity.setBtnState("연결 중", false);

            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        Log.e("Debug", "disconnect");

        connected = Connected.False;
        service.disconnect();
    }

    private void send(String str) {
        Log.e("Debug", "send");


        if (connected != Connected.True) {
            mActivity.setBtnState("연결 중", false);
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mActivity.setBtnState("네트워크 데이터 전송 중", false);
            String msg;
            byte[] data;
            if (hexEnabled) {
                StringBuilder sb = new StringBuilder();
                TextUtil.toHexString(sb, TextUtil.fromHexString(str));
                TextUtil.toHexString(sb, newline.getBytes());
                msg = sb.toString();
                data = TextUtil.fromHexString(msg);
            } else {
                msg = str;
                data = (str + newline).getBytes();
            }
            SpannableStringBuilder spn = new SpannableStringBuilder(msg + '\n');
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            receiveText.append(spn);
            service.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void receive(byte[] data) {
        Log.e("Debug", "receive");

        if (hexEnabled) {
            receiveText.append(TextUtil.toHexString(data) + '\n');
        } else {
            String msg = new String(data);
            if (newline.equals(TextUtil.newline_crlf) && msg.length() > 0) {
                // don't show CR as ^M if directly before LF
                msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);
                // special handling if CR and LF come in separate fragments
                if (pendingNewline && msg.charAt(0) == '\n') {
                    Editable edt = receiveText.getEditableText();
                    if (edt != null && edt.length() > 1)
                        edt.replace(edt.length() - 2, edt.length(), "");
                }
                pendingNewline = msg.charAt(msg.length() - 1) == '\r';
            }
            receiveText.append(TextUtil.toCaretString(msg, newline.length() != 0));

            /**
             * 결과 확인
             */
            if(msg.charAt(5) == '0' && msg.charAt(6) == '0') {

                String[] msgSplit = msg.split("00");

                if(msgSplit.length >= 2) {
                    CommonUtil.INSTANCE.setPreferenceString(getActivity(),mActivity.getString(R.string.pref_key_device_wifi_ip),msgSplit[1]);
                }

                String[] nameSplit = deviceName.split("WAVU-");

                if(nameSplit.length >= 2) {
                    CommonUtil.INSTANCE.setPreferenceString(getActivity(),mActivity.getString(R.string.pref_key_device_serial),nameSplit[1]);
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, "네트워크 데이터 전달 완료", Toast.LENGTH_SHORT).show();
                        finishFragment();
                    }
                });

            } else {
                Toast.makeText(getActivity(), "데이터 전송 오류", Toast.LENGTH_SHORT).show();
                mActivity.setBtnState("기기 연결", true);
            }
        }
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);
    }

    private void finishFragment() {
        mActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().setResult(200);
        getActivity().finish();
    }

    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {
        status("connected");

        mActivity.setBtnState("연결 중", false);

        connected = Connected.True;
        // 여기서 Activity가 데이터를 전달하도록 유도해야함.

//        Thread th = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        th.run();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                send(sendText.getText().toString());
            }
        });

    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        mActivity.setBtnState("장치 연결", true);

        disconnect();
        // Activity의 연결 버튼이 다시 활성화
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        mActivity.setBtnState("장치 연결", true);
        disconnect();
    }

}
