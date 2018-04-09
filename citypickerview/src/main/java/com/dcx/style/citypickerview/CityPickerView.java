package com.dcx.style.citypickerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dcx.Interface.OnCityItemClickListener;
import com.dcx.bean.CityBean;
import com.dcx.bean.DistrictBean;
import com.dcx.bean.ProvinceBean;
import com.dcx.citywheel.CityConfig;
import com.dcx.citywheel.CityParseHelper;
import com.dcx.style.citypickerview.widget.CanShow;
import com.dcx.style.citypickerview.widget.wheel.OnWheelChangedListener;
import com.dcx.style.citypickerview.widget.wheel.WheelView;
import com.dcx.style.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;
import com.dcx.utils.utils;
import com.lljjcoder.style.citypickerview.R;

/**
 * 省市区三级选择
 */
public class CityPickerView implements CanShow, OnWheelChangedListener {
    private volatile static CityPickerView instance;
    private PopupWindow popwindow;
    private View popview;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private RelativeLayout mRelativeTitleBg;
    private Button mOK;
    private TextView mTvTitle;
    private Button mCancel;
    private OnCityItemClickListener mBaseListener;
    private CityParseHelper parseHelper;
    private CityConfig config;

    private CityPickerView() {
    }

    // 单例模式
    public static CityPickerView getInstance() {
        if (instance == null) {
            synchronized (CityPickerView.class) {
                if (instance == null) {
                    instance = new CityPickerView();
                }
            }
        }
        return instance;
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        mBaseListener = listener;
    }

    // 设置属性
    public void setConfig(final CityConfig config) {
        this.config = config;
    }

    //初始化，默认解析城市数据，提交加载速度
    public void init(Context context) {
        parseHelper = new CityParseHelper();
        //解析初始数据
        if (parseHelper == null || parseHelper.getProvinceBeanArrayList().isEmpty()) {
            parseHelper.initData(context);
        }
    }

    // 初始化popWindow
    private void initCityPickerPopwindow(Context context) {
        if (config == null) {
            throw new IllegalArgumentException("please set config first...");
        }
        LayoutInflater layoutInflater = LayoutInflater.from(config.getContext());
        if (2 == context.getResources().getConfiguration().orientation) {//横屏
            popview = layoutInflater.inflate(R.layout.pop_citypicker_land, null);
        } else {
            popview = layoutInflater.inflate(R.layout.pop_citypicker, null);
        }

        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) popview.findViewById(R.id.rl_title);
        mOK = (Button) popview.findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        mCancel = (Button) popview.findViewById(R.id.tv_cancel);

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        popwindow.setOutsideTouchable(true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());

        popwindow.showAtLocation(popview, Gravity.CENTER, 0, 0);

//        popwindow.setBackgroundDrawable(new ColorDrawable());
//        popwindow.setTouchable(true);
//        popwindow.setOutsideTouchable(false);
//        popwindow.setFocusable(true);

        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (config.isShowBackground()) {
                    utils.setBackgroundAlpha(config.getContext(), 1.0f);
                }
            }
        });

        // 设置标题背景颜色
        if (!TextUtils.isEmpty(config.getTitleBackgroundColorStr())) {
            if (config.getTitleBackgroundColorStr().startsWith("#")) {
                mRelativeTitleBg.setBackgroundColor(Color.parseColor(config.getTitleBackgroundColorStr()));
            } else {
                mRelativeTitleBg.setBackgroundColor(Color.parseColor("#" + config.getTitleBackgroundColorStr()));
            }
        }

        //标题
        if (!TextUtils.isEmpty(config.getTitle())) {
            mTvTitle.setText(config.getTitle());
        }

        //标题文字大小
        if (config.getTitleTextSize() > 0) {
            mTvTitle.setTextSize(config.getTitleTextSize());
        }

        //标题文字颜色
        if (!TextUtils.isEmpty(config.getTitleTextColorStr())) {
            if (config.getTitleTextColorStr().startsWith("#")) {
                mTvTitle.setTextColor(Color.parseColor(config.getTitleTextColorStr()));
            } else {
                mTvTitle.setTextColor(Color.parseColor("#" + config.getTitleTextColorStr()));
            }
        }

        //设置确认按钮文字大小颜色
        if (!TextUtils.isEmpty(config.getConfirmTextColorStr())) {
            if (config.getConfirmTextColorStr().startsWith("#")) {
                mOK.setTextColor(Color.parseColor(config.getConfirmTextColorStr()));
            } else {
                mOK.setTextColor(Color.parseColor("#" + config.getConfirmTextColorStr()));
            }
        }

        if (!TextUtils.isEmpty(config.getConfirmText())) {
            mOK.setText(config.getConfirmText());
        }

        if ((config.getConfirmTextSize() > 0)) {
            mOK.setTextSize(config.getConfirmTextSize());
        }

        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(config.getCancelTextColorStr())) {
            if (config.getCancelTextColorStr().startsWith("#")) {
                mCancel.setTextColor(Color.parseColor(config.getCancelTextColorStr()));
            } else {
                mCancel.setTextColor(Color.parseColor("#" + config.getCancelTextColorStr()));
            }
        }

        if (!TextUtils.isEmpty(config.getCancelText())) {
            mCancel.setText(config.getCancelText());
        }

        if ((config.getCancelTextSize() > 0)) {
            mCancel.setTextSize(config.getCancelTextSize());
        }

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseListener.onCancel();
                hide();
            }
        });

        //确认选择
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parseHelper != null) {
                    if (config.getWheelType() == CityConfig.WheelType.PRO) {
                        mBaseListener.onSelected(parseHelper.getProvinceBean(), new CityBean(), new DistrictBean());
                    } else if (config.getWheelType() == CityConfig.WheelType.PRO_CITY) {
                        mBaseListener.onSelected(parseHelper.getProvinceBean(),
                                parseHelper.getCityBean(),
                                new DistrictBean());
                    } else {
                        mBaseListener.onSelected(parseHelper.getProvinceBean(),
                                parseHelper.getCityBean(),
                                parseHelper.getDistrictBean());
                    }
                } else {
                    mBaseListener.onSelected(new ProvinceBean(), new CityBean(), new DistrictBean());
                }
                hide();
            }
        });
        //显示省市区数据
        setUpData();
        //背景半透明
        if (config != null && config.isShowBackground()) {
            utils.setBackgroundAlpha(context, 0.5f);
        }
    }

    // 加载数据
    private void setUpData() {
        if (parseHelper == null || config == null) {
            return;
        }
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(config.getDefaultProvinceName()) && parseHelper.getProvinceBeenArray().length > 0) {
            for (int i = 0; i < parseHelper.getProvinceBeenArray().length; i++) {
                if (parseHelper.getProvinceBeenArray()[i].getName().contains(config.getDefaultProvinceName())) {
                    provinceDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<ProvinceBean>(config.getContext(),
                parseHelper.getProvinceBeenArray());
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //自定义item
        if (config.getCustomItemLayout() != CityConfig.NONE && config.getCustomItemTextViewId() != CityConfig.NONE) {
            arrayWheelAdapter.setItemResource(config.getCustomItemLayout());
            arrayWheelAdapter.setItemTextResource(config.getCustomItemTextViewId());
        } else {
            arrayWheelAdapter.setItemResource(R.layout.item_city);
            arrayWheelAdapter.setItemTextResource(R.id.item_city_name_tv);
        }
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
//        mViewProvince.setVisibleItems(config.getVisibleItems());
        mViewProvince.setVisibleItems(3);
        mViewCity.setVisibleItems(3);
        mViewDistrict.setVisibleItems(3);
        updateCities();
        updateAreas();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        if (parseHelper == null || config == null) {
            return;
        }
        //省份滚轮滑动的当前位置
        int pCurrent = mViewProvince.getCurrentItem();
        //省份选中的名称
        ProvinceBean mProvinceBean = parseHelper.getProvinceBeenArray()[pCurrent];
        parseHelper.setProvinceBean(mProvinceBean);
        if (parseHelper.getPro_CityMap() == null) {
            return;
        }
        CityBean[] cities = parseHelper.getPro_CityMap().get(mProvinceBean.getName());
        if (cities == null) {
            return;
        }
        //设置最初的默认城市
        int cityDefault = -1;
        if (!TextUtils.isEmpty(config.getDefaultCityName()) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                if (config.getDefaultCityName().contains(cities[i].getName())) {
                    cityDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<>(config.getContext(), cities);
        //自定义item
        if (config.getCustomItemLayout() != CityConfig.NONE && config.getCustomItemTextViewId() != CityConfig.NONE) {
            cityWheel.setItemResource(config.getCustomItemLayout());
            cityWheel.setItemTextResource(config.getCustomItemTextViewId());
        } else {
            cityWheel.setItemResource(R.layout.item_city);
            cityWheel.setItemTextResource(R.id.item_city_name_tv);
        }
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mViewCity.setCurrentItem(0);
        }
        updateAreas();
    }

    // 根据当前的市，更新区WheelView的信息
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        if (parseHelper.getPro_CityMap() == null || parseHelper.getCity_DisMap() == null) {
            return;
        }
        if (config.getWheelType() == CityConfig.WheelType.PRO_CITY
                || config.getWheelType() == CityConfig.WheelType.PRO_CITY_DIS) {
            CityBean mCityBean = parseHelper.getPro_CityMap().get(parseHelper.getProvinceBean().getName())[pCurrent];
            parseHelper.setCityBean(mCityBean);
            if (config.getWheelType() == CityConfig.WheelType.PRO_CITY_DIS) {
                DistrictBean[] areas = parseHelper.getCity_DisMap()
                        .get(parseHelper.getProvinceBean().getName() + mCityBean.getName());
                if (areas == null) {
                    return;
                }
                int districtDefault = -1;
                if (!TextUtils.isEmpty(config.getDefaultDistrict()) && areas.length > 0) {
                    for (int i = 0; i < areas.length; i++) {
                        if (config.getDefaultDistrict().contains(areas[i].getName())) {
                            districtDefault = i;
                            break;
                        }
                    }
                }
                ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<DistrictBean>(config.getContext(), areas);

                //自定义item
                if (config.getCustomItemLayout() != CityConfig.NONE
                        && config.getCustomItemTextViewId() != CityConfig.NONE) {
                    districtWheel.setItemResource(config.getCustomItemLayout());
                    districtWheel.setItemTextResource(config.getCustomItemTextViewId());
                } else {
                    districtWheel.setItemResource(R.layout.item_city);
                    districtWheel.setItemTextResource(R.id.item_city_name_tv);
                }
                mViewDistrict.setViewAdapter(districtWheel);

                DistrictBean mDistrictBean = null;
                if (parseHelper.getDisMap() == null) {
                    return;
                }

                if (-1 != districtDefault) {
                    mViewDistrict.setCurrentItem(districtDefault);
                    //获取第一个区名称
                    mDistrictBean = parseHelper.getDisMap().get(parseHelper.getProvinceBean().getName()
                            + mCityBean.getName() + config.getDefaultDistrict());
                } else {
                    mViewDistrict.setCurrentItem(0);
                    if (areas.length > 0) {
                        mDistrictBean = areas[0];
                    }
                }
                parseHelper.setDistrictBean(mDistrictBean);
            }
        }
    }

    public void showCityPicker(Context context) {
        initCityPickerPopwindow(context);
        if (!isShow()) {
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            if (parseHelper != null && parseHelper.getCity_DisMap() != null) {
                DistrictBean mDistrictBean = parseHelper.getCity_DisMap()
                        .get(parseHelper.getProvinceBean().getName() + parseHelper.getCityBean().getName())[newValue];
                parseHelper.setDistrictBean(mDistrictBean);
            }
        }
    }
}
