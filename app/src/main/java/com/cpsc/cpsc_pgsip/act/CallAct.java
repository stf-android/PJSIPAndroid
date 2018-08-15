package com.cpsc.cpsc_pgsip.act;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.Enums.FromEnums;
import com.cpsc.cpsc_pgsip.R;
import com.cpsc.cpsc_pgsip.Transformation.CircleTransform;
import com.cpsc.cpsc_pgsip.base.BaseAct;
import com.cpsc.cpsc_pgsip.bean.CallInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p>
 * <p>
 * 拨打电话界面
 *
 * @author allens
 * @date 2018/1/25
 */

public class CallAct extends BaseAct {
    @BindView(R.id.act_call_rv)
    RecyclerView actCallRv;
    @BindView(R.id.act_call_tv)
    TextView actCallTv;
    @BindView(R.id.act_call_img)
    ImageView actCallImg;

    private StringBuffer buffer;

    public static final String NUMBER = "0X00";


    @Override
    protected void onCreate() {
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);
        buffer = new StringBuffer();
        actCallImg.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        buffer = null;
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void initListener() {
        init_adapter();
    }

    private void init_adapter() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        actCallRv.setLayoutManager(mLayoutManager);

        actCallRv.setAdapter(new CommonAdapter<CallInfo>(this, R.layout.item_call_number, getRvDatas()) {
            @Override
            protected void convert(ViewHolder holder, CallInfo fgMeRvInfo, final int position) {
                init_callSurface(holder, fgMeRvInfo, position);
                init_callNumber(holder, fgMeRvInfo, position);
                init_call(holder);
            }
        });

    }

    private void init_callNumber(ViewHolder holder, CallInfo fgMeRvInfo, final int position) {
        holder.setOnClickListener(R.id.item_call_number_view, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        actCallImg.setVisibility(View.VISIBLE);
                        buffer.append("" + (position + 1));
                        actCallTv.setText(buffer.toString());
                        break;
                    case 9:
                        actCallImg.setVisibility(View.VISIBLE);
                        buffer.append("*");
                        actCallTv.setText(buffer.toString());
                        break;
                    case 10:
                        actCallImg.setVisibility(View.VISIBLE);
                        buffer.append("0");
                        actCallTv.setText(buffer.toString());
                        break;
                    case 11:
                        actCallImg.setVisibility(View.VISIBLE);
                        buffer.append("#");
                        actCallTv.setText(buffer.toString());
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 打电话
     *
     * @param holder
     */
    private void init_call(ViewHolder holder) {
        holder.setOnClickListener(R.id.item_call_number_img, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallAct.this, CallPhoneAct.class);
                intent.putExtra(NUMBER, buffer.toString());
                intent.putExtra(Config.FROMTYPE, FromEnums.CallAct.getState());
                startActivity(intent);
                finish();
            }
        });

    }

    /***
     * call  界面
     * @param holder
     * @param fgMeRvInfo
     * @param position
     */
    private void init_callSurface(ViewHolder holder, CallInfo fgMeRvInfo, int position) {
        holder.setText(R.id.item_call_number_number, fgMeRvInfo.getNumber());
        holder.setText(R.id.item_call_number_letter, fgMeRvInfo.getLetter());
        if (position == 9 || position == 11) {
            holder.setVisible(R.id.item_call_number_letter, true);
            TextView textView = holder.getView(R.id.item_call_number_number);
            textView.setPadding(0, 0, 0, 0);
        }

        if (position == 12 || position == 14) {
            holder.setVisible(R.id.item_call_number_number, false);
            holder.setVisible(R.id.item_call_number_letter, false);
            holder.setVisible(R.id.item_call_number_view, false);
        }

        if (position == 13) {
            holder.setVisible(R.id.item_call_number_number, false);
            holder.setVisible(R.id.item_call_number_letter, false);
            holder.setVisible(R.id.item_call_number_view, false);
            holder.setVisible(R.id.item_call_number_img, true);

            RequestOptions options = new RequestOptions()
                    .transforms(new CircleTransform(CallAct.this, 0, getResources().getColor(R.color.green)));
            Glide.with(CallAct.this)
                    .load(R.mipmap.call_phone)
                    .apply(options)
                    .into((ImageView) holder.getView(R.id.item_call_number_img));
        }
    }

    private List<CallInfo> getRvDatas() {
        ArrayList<CallInfo> list = new ArrayList<>();
        String[] arr = new String[]{"", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"};
        for (int i = 0; i < 9; i++) {
            list.add(new CallInfo.Builder()
                    .number(i + 1 + "")
                    .letter(arr[i])
                    .build());
        }

        String[] arr3 = new String[]{"*", "0", "#"};
        String[] arr4 = new String[]{"", "+", ""};
        for (int i = 0; i < 3; i++) {
            list.add(new CallInfo.Builder()
                    .number(arr3[i])
                    .letter(arr4[i])
                    .build());
        }

        for (int i = 0; i < 3; i++) {
            list.add(new CallInfo.Builder()
                    .number(arr3[i])
                    .letter(arr4[i])
                    .build());
        }
        return list;
    }

    @OnClick(R.id.act_call_img)
    public void onViewClicked() {
        if (buffer.length() > 1) {
            buffer.deleteCharAt(buffer.length() - 1);
            actCallTv.setText(buffer.toString());
        } else if (buffer.length() == 1) {
            buffer.deleteCharAt(buffer.length() - 1);
            actCallImg.setVisibility(View.INVISIBLE);
            actCallTv.setText(buffer.toString());
        }
    }
}
