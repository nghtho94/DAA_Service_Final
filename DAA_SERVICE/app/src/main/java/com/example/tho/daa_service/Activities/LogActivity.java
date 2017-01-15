package com.example.tho.daa_service.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tho.daa_service.CheckBoxView.SmoothCheckBox;
import com.example.tho.daa_service.R;

import java.io.Serializable;
import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {
    private ArrayList<Bean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mList.add(new Bean("tho","20","hocsinh"));
        mList.add(new Bean("my","20","hocsinh"));


        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(LogActivity.this, R.layout.item, null);
                    holder.tv = (TextView) convertView.findViewById(R.id.tv);
                    holder.cb = (SmoothCheckBox) convertView.findViewById(R.id.scb);
                    holder.cb.setClickable(false);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                final Bean bean = mList.get(position);
                holder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        bean.isChecked = isChecked;
                    }
                });

                holder.cb.setChecked(true);
                String text = "Item" + position;
                holder.tv.setText(text);


                return convertView;
            }

            class ViewHolder {
                SmoothCheckBox cb;
                TextView tv;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bean bean = (Bean) parent.getAdapter().getItem(position);
                bean.isChecked = !bean.isChecked;
                SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb);
//                checkBox.setChecked(bean.isChecked, true);
                Toast.makeText(LogActivity.this, bean.getTen(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Bean implements Serializable {
        boolean isChecked;
        String ten;
        String tuoi;
        String congviec;

        public Bean(String ten, String tuoi, String congviec){
            this.ten = ten;
            this.tuoi = tuoi;
            this.congviec = congviec;
        }

        public String getCongviec() {
            return congviec;
        }

        public String getTen() {
            return ten;
        }

        public String getTuoi() {
            return tuoi;
        }
    }
}
