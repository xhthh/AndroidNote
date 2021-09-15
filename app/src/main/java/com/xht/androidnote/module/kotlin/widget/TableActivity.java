package com.xht.androidnote.module.kotlin.widget;

import android.view.View;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.xht.androidnote.R;
import com.xht.androidnote.base.BaseActivity;

public class TableActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_table_test;
    }

    @Override
    protected void initEventAndData() {

        SmartTable table = findViewById(R.id.table);

        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return 0;
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return super.getTextColor(cellInfo);
            }
        });

    }
}
