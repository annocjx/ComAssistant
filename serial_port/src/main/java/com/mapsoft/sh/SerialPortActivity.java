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

    private RecyclerView mRecyclerView;
    private Spinner mSpSerial;
    private EditText mEdInput;
    private Button mBtSend;
    private SerialPortFinder mSerialPortFinder;
    private AbstractSerialHelper mSerialHelper;
    private Spinner mSpBote;
    private Button mBtOpen, mBtSave, mBtClear;
    private AbstractCommonAdapter mLoglistadapter;
    private List<ComBean> mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        mRecyclerView = findViewById(R.id.recy);
        mSpSerial = findViewById(R.id.sp_serial);
        mEdInput = findViewById(R.id.ed_input);
        mBtSend = findViewById(R.id.bt_send);
        mSpBote = findViewById(R.id.sp_bote);
        mBtOpen = findViewById(R.id.bt_open);
        mBtClear = findViewById(R.id.bt_clear);


        mLogs = new ArrayList<>();
        findViewById(R.id.bt_save).setOnClickListener(v -> {
            save(mLogs);
        });
        mLoglistadapter = new AbstractCommonAdapter<ComBean>(this, R.layout.item_layout, mLogs) {
            @Override
            public void convert(BaseVH holder, ComBean bean, int postion) {
                TextView tv = holder.getView(R.id.textView);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.LEFT);
                tv.setText(bean.sRecTime + ":   " + FuncUtil.ByteArrToHex(bean.bRec));
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLoglistadapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
       


        iniview();

        findViewById(R.id.textView2).setOnClickListener(v -> save(mLogs));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSerialHelper.close();
    }

    private void iniview() {
        mSerialPortFinder = new SerialPortFinder();
        final String[] ports = mSerialPortFinder.getAllDevicesPath();
        SpAdapter spAdapter = new SpAdapter(this);
        spAdapter.setDatas(ports);
        mSpSerial.setAdapter(spAdapter);

        mSerialHelper = new AbstractSerialHelper("",0) {
            @Override
            protected void onDataReceived(ComBean comBean) {
                mLogs.add(comBean);
                Log.i(mSerialHelper.toString() + ":收到串口数据0: ", "" + FuncUtil.ByteArrToHex(comBean.bRec));
                mRecyclerView.post(()->{
                    mRecyclerView.smoothScrollToPosition(mLoglistadapter.getItemCount() - 1);
                    mLoglistadapter.notifyDataSetChanged();
                });
            }
        };
        
        
        mSpSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSerialHelper.close();
                mSerialHelper.setPort(ports[position]);
                mBtOpen.setEnabled(true);
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
        mSpBote.setAdapter(spAdapter2);
        //spBote.setSelection(13);
        mSpBote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSerialHelper.close();
                mSerialHelper.setBaudRate(boteArr[position]);
                mBtOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      
        mBtOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeInputMethod(SerialPortActivity.this, mEdInput);
                    if (TextUtils.isEmpty(mSerialHelper.getPort())|| mSerialHelper.getBaudRate()==0){
                        return;
                    }
                    mSerialHelper.close();
                    mSerialHelper = new AbstractSerialHelper(mSerialHelper.getPort(), mSerialHelper.getBaudRate()) {

                        @Override
                        protected void onDataReceived(ComBean comBean) {
                            mLogs.add(comBean);
                            Log.i(mSerialHelper.toString() + ":收到串口数据: ", "" + FuncUtil.ByteArrToHex(comBean.bRec));
                            mRecyclerView.post(()->{
                                mRecyclerView.smoothScrollToPosition(mLoglistadapter.getItemCount() - 1);
                                mLoglistadapter.notifyDataSetChanged();
                            });
                        }
                    };
                    mSerialHelper.open(false);
                    mBtOpen.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mBtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogs.clear();
                mLoglistadapter.notifyDataSetChanged();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1) {
                    mBtSend.setText("发送Text");
                    if (mEdInput.getText().toString().length() > 0) {
                        if (mSerialHelper.isOpen()) {
                            mSerialHelper.sendTxt(mEdInput.getText().toString());
                        } else {
                            Toast.makeText(getBaseContext(), "串口还没打开", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "填数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (mEdInput.getText().toString().length() > 0) {
                        if (mSerialHelper.isOpen()) {
                            mSerialHelper.sendHex(mEdInput.getText().toString());
                        } else {
                            Toast.makeText(getBaseContext(), "串口还没打开", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "填数据", Toast.LENGTH_SHORT).show();
                    }
                    mBtSend.setText("发送Hex");
                }
            }
        });
        closeInputMethod(this, mBtSend);
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
