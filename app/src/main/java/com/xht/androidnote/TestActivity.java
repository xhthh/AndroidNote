package com.xht.androidnote;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xht.androidnote.base.BaseActivity;
import com.xht.androidnote.module.eventbus.EventBusHelper;
import com.xht.androidnote.module.eventbus.TestEvent;
import com.xht.androidnote.module.pickerview.TimePickerView;
import com.xht.androidnote.utils.TimeUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xht on 2018/9/10.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private EditText editText;
    private int digits = 1;
    private TextView mTvTime1;
    private TextView mTvTime2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initEventAndData() {
        Button btnCommit = findViewById(R.id.btn_commit);

        Button btnTime1 = findViewById(R.id.btn_time_picker1);
        Button btnTime2 = findViewById(R.id.btn_time_picker2);

        btnTime1.setOnClickListener(this);
        btnTime2.setOnClickListener(this);

        mTvTime1 = findViewById(R.id.tv_time1);
        mTvTime2 = findViewById(R.id.tv_time2);


        editText = findViewById(R.id.edittext);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + digits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }

                if (!TextUtils.isEmpty(s) && !".".equals(s.toString()) && Double.valueOf(s.toString()) < 0.1) {
                    if ("0.".equals(s.toString())) {
                        return;
                    }
                    Toast.makeText(mContext, "商品价格必须在0.1至999999之内", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                Log.i("xht", "content = " + content);


            }
        });


        initData();
    }

    private void initData() {
        figureSaleTime();
    }

    /**
     * EventBus测试
     */
    private void eventBusTest() {
        EventBusHelper.getInstance().register(this);
        EventBusHelper.getInstance().post(new TestEvent("卡米哈米哈"));
    }

    /**
     * 如果不写接收的方法会报错，可以想办法封装到基类当中
     * Caused by: org.greenrobot.eventbus.EventBusException: Subscriber class com.xht.androidnote.TestActivity and its super classes have no public methods with the @Subscribe annotation
     * 接收的方法必须为public
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TestEvent event) {
    }

    @Override
    protected void onDestroy() {
        EventBusHelper.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_time_picker1:
                showDateDialog(mTvTime1, true);
                break;
            case R.id.btn_time_picker2:
                showDateDialog(mTvTime2, false);
                break;
        }
    }


    private String saleStartTime;//销售开始时间
    private String saleEndTime;//销售结束时间

    private String consumeStartTime;//消费码开始时间
    private String consumeEndTime;//消费码结束时间

    /**
     * 时间选择弹窗
     *
     * @param textView
     */
    private void showDateDialog(final TextView textView, final boolean isSaleTime) {
        Calendar startDate = null;
        Calendar endDate = null;

        Calendar current = Calendar.getInstance();

        if (isSaleTime) {
            startDate = Calendar.getInstance();
            int currentYear = startDate.get(Calendar.YEAR);
            int currentMonth = startDate.get(Calendar.MONTH);
            int currentDay = startDate.get(Calendar.DAY_OF_MONTH);

            startDate.set(currentYear, currentMonth, currentDay);

            endDate = Calendar.getInstance();
            Date laterDate = TimeUtils.convertStrToDate(TimeUtils.getBeforeOrLaterDate(330));
            endDate.setTime(laterDate);

            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH);
            int day = endDate.get(Calendar.DAY_OF_MONTH);

            endDate.set(year, month, day);


            current.setTimeInMillis(System.currentTimeMillis());
        } else {
            startDate = Calendar.getInstance();

            Date date = TimeUtils.convertStrToDate(saleEndTime);

            String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String[] strNow = dateFormat.split("-");
            Integer currentYear = Integer.parseInt(strNow[0]);
            Integer currentMonth = Integer.parseInt(strNow[1]) - 1;
            Integer currentDay = Integer.parseInt(strNow[2]);

            startDate.set(currentYear, currentMonth, currentDay);

            endDate = Calendar.getInstance();
            Date laterDate = TimeUtils.convertStrToDate(TimeUtils.getBeforeOrLaterDate(saleEndTime, 330));
            endDate.setTime(laterDate);

            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH);
            int day = endDate.get(Calendar.DAY_OF_MONTH);

            endDate.set(year, month, day);

            current.setTimeInMillis(date.getTime());
        }

        new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                if (isSaleTime) {
                    saleEndTime = TimeUtils.getSimpleTime(date);

                    if (!TextUtils.isEmpty(saleEndTime) && !TextUtils.isEmpty(consumeEndTime) &&
                            TimeUtils.compareDate(saleEndTime, consumeEndTime)) {
                        Toast.makeText(mContext, "消费码有效期不得早于商品在售结束时间", Toast.LENGTH_SHORT).show();
                    } else {
                        textView.setText("自上线之日起 至 " + saleEndTime);
                    }
                } else {
                    //消费码有效期不得早于商品在售结束时间
                    consumeEndTime = TimeUtils.getSimpleTime(date);
                    if (TimeUtils.compareDate(saleEndTime, consumeEndTime)) {
                        Toast.makeText(mContext, "消费码有效期不得早于商品在售结束时间", Toast.LENGTH_SHORT).show();

                    } else {
                        textView.setText("自上线之日起 至 " + consumeEndTime);
                    }
                }
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .isCenterLabel(false)
                .setDate(current)
                .setRangDate(startDate, endDate)
                .build()
                .show();
    }

    /**
     * 计算在售时间
     * 默认显示当前编辑时间+330天，且该时间为可编辑的在售时间的最大值，大于其年-月-日 均不可选
     */
    private void figureSaleTime() {
        Calendar endDateCalendar = Calendar.getInstance();
        String laterDateStr = TimeUtils.getBeforeOrLaterDate(330);
        Date laterDate = TimeUtils.convertStrToDate(laterDateStr);
        endDateCalendar.setTime(laterDate);

        int year = endDateCalendar.get(Calendar.YEAR);
        int month = endDateCalendar.get(Calendar.MONTH);
        int day = endDateCalendar.get(Calendar.DAY_OF_MONTH);

        endDateCalendar.set(year, month, day);

        saleEndTime = laterDateStr;
        consumeEndTime = laterDateStr;

        //在售时间
        mTvTime1.setText("自上线之日起 至 " + saleEndTime);
        //消费码有效期：初始值显示于在售期结束日期一致
        mTvTime2.setText("自上线之日起 至 " + consumeEndTime);
    }
}
