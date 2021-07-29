package com.mapsoft.sh;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapsoft.bean.ComBean;
import com.mapsoft.serial_port.R;
import com.mapsoft.utils.FuncUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;


/**
 * @author Administrator
 */
public class SerialPortActivity extends FragmentActivity {

    private RecyclerView recy;
    private Spinner spSerial;
    private EditText edInput;
    private Button btSend;
    private SerialPortFinder serialPortFinder;
    private AbstractSerialHelper serialHelper;
    private Spinner spBote;
    private Button btOpen, btSave, btClear;
    private AbstractCommonAdapter logListAdapter;
    private List<ComBean> logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        recy = findViewById(R.id.recy);
        spSerial = findViewById(R.id.sp_serial);
        edInput = findViewById(R.id.ed_input);
        btSend = findViewById(R.id.bt_send);
        spBote = findViewById(R.id.sp_bote);
        btOpen = findViewById(R.id.bt_open);
        btClear = findViewById(R.id.bt_clear);


        logs = new ArrayList<>();
        findViewById(R.id.bt_save).setOnClickListener(v -> {
            save(logs);
        });
        logListAdapter = new AbstractCommonAdapter<ComBean>(this, R.layout.item_layout, logs) {
            @Override
            public void convert(BaseVH holder, ComBean bean, int postion) {
                TextView tv = holder.getView(R.id.textView);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.LEFT);
                tv.setText(bean.sRecTime + ":   " + FuncUtil.ByteArrToHex(bean.bRec));
            }
        };
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(logListAdapter);
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
       


        iniview();

        findViewById(R.id.textView2).setOnClickListener(v -> save(logs));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialHelper.close();
       /* serialHelper = new AbstractSerialHelper(bote) {
            @Override
            protected void onDataReceived(ComBean var1) {

            }
        };
        try {
            serialHelper.open(true);
        } catch (IOException pE) {
            pE.printStackTrace();
            Tip.popTip(App.get(), "打开串口失败:" + pE.toString());
        }
        */
        //H.restartAppByAlarm(this, 300);
    }

    private void iniview() {
        serialPortFinder = new SerialPortFinder();
        final String[] ports = serialPortFinder.getAllDevicesPath();
        SpAdapter spAdapter = new SpAdapter(this);
        spAdapter.setDatas(ports);
        spSerial.setAdapter(spAdapter);
        spSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //serialHelper.close();
                serialHelper.setPort(ports[position]);
                btOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final String[] boteArr = new String[]{"0", "50", "75", "110", "134", "150", "200", "300", "600",
                "1200", "1800", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400",
                "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000",
                "2500000", "3000000", "3500000", "4000000"};
        SpAdapter spAdapter2 = new SpAdapter(this);
        spAdapter2.setDatas(boteArr);
        spBote.setAdapter(spAdapter2);
        //spBote.setSelection(13);
        spBote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //serialHelper.close();
                serialHelper.setBaudRate(boteArr[position]);
                btOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeInputMethod(SerialPortActivity.this, edInput);
                    if (TextUtils.isEmpty(serialHelper.getPort())||serialHelper.getBaudRate()==0){
                        return;
                    }
                    serialHelper.close();
                    serialHelper = new AbstractSerialHelper(serialHelper.getPort(),serialHelper.getBaudRate()) {

                        @Override
                        protected void onDataReceived(ComBean comBean) {
                            logs.add(comBean);
                            Log.i(serialHelper.toString() + ":收到串口数据: ", "" + FuncUtil.ByteArrToHex(comBean.bRec));
                            recy.smoothScrollToPosition(logListAdapter.getItemCount() - 1);
                            logListAdapter.notifyDataSetChanged();
                        }
                    };
                    serialHelper.open(false);
                    btOpen.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logs.clear();
                logListAdapter.notifyDataSetChanged();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1) {
                    btSend.setText("发送Text");
                    if (edInput.getText().toString().length() > 0) {
                        if (serialHelper.isOpen()) {
                            serialHelper.sendTxt(edInput.getText().toString());
                        } else {
                            Toast.makeText(getBaseContext(), "串口还没打开", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "填数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (edInput.getText().toString().length() > 0) {
                        if (serialHelper.isOpen()) {
                            serialHelper.sendHex(edInput.getText().toString());
                        } else {
                            Toast.makeText(getBaseContext(), "串口还没打开", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "填数据", Toast.LENGTH_SHORT).show();
                    }
                    btSend.setText("发送Hex");
                }
            }
        });
        closeInputMethod(this, btSend);
    }

    /**
     * 关闭软键盘
     */
    private static void closeInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    public void save(List<ComBean> s) {
        //todo
    }
}
