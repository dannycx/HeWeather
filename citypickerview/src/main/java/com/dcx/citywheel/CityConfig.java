package com.dcx.citywheel;

import android.content.Context;

/**
 * 城市选择器样式配置
 */
public class CityConfig {
    public static final Integer NONE = -1111;
    // 定义默认显示省市区三级联动的滚轮选择器
    public WheelType mWheelType = WheelType.PRO_CITY_DIS;
    private Context mContext;
    //滚轮显示的item个数
    private int visibleItems = 3;
    //CANCEL按钮 颜色、文字、大小
    private String cancelTextColorStr = "#ffffff";
    private String cancelText = "CANCEL";
    private int cancelTextSize = 16;
    //OK按钮 颜色、文字、大小
    private String confirmTextColorStr = "#ffffff";
    private String confirmText = "OK";
    private int confirmTextSize = 16;
    //弹出框标题、背景颜色、标题颜色、标题字体大小
    private String mTitle = "添加城市";
    private String titleBackgroundColorStr = "#C7C7C7";
    private String titleTextColorStr = "#0e73ba";
    private int titleTextSize = 30;
    //默认的显示省份,城市
    private String defaultProvinceName = "广东";
    private String defaultCityName = "深圳";
    private String defaultDistrict = "罗湖区";
    // 自定义的item布局,自定义的item txt id
    private Integer customItemLayout;
    private Integer customItemTextViewId;
    // 是否显示半透明的背景
    private boolean isShowBackground = true;

    public CityConfig(Builder builder) {
        this.mContext = builder.mContext;

        //标题栏背景颜色
        this.titleBackgroundColorStr = builder.titleBackgroundColorStr;
        //标题文字、大小、颜色
        this.mTitle = builder.mTitle;
        this.titleTextColorStr = builder.titleTextColorStr;
        this.titleTextSize = builder.titleTextSize;
        //取消字体的颜色、大小、内容
        this.cancelTextColorStr = builder.cancelTextColorStr;
        this.cancelText = builder.cancelText;
        this.cancelTextSize = builder.cancelTextSize;
        //确认字体的颜色、大小、内容
        this.confirmTextColorStr = builder.confirmTextColorStr;
        this.confirmText = builder.confirmText;
        this.confirmTextSize = builder.confirmTextSize;

        //item显示的个数
        this.visibleItems = builder.visibleItems;
        // 默认的省市区地址
        this.defaultDistrict = builder.defaultDistrict;
        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;
        // 是否显示城市和地区
        this.mWheelType = builder.mWheelType;
        // 是否显示半透明
        this.isShowBackground = builder.isShowBackground;
        // 自定义item的布局，必须制定Layout和id
        this.customItemLayout = builder.customItemLayout;
        this.customItemTextViewId = builder.customItemTextViewId;
    }

    public CityConfig.WheelType getWheelType() {
        return mWheelType;
    }

    public boolean isShowBackground() {
        return isShowBackground;
    }

    public Context getContext() {
        return mContext;
    }

    public String getCancelTextColorStr() {
        return cancelTextColorStr == null ? "" : cancelTextColorStr;
    }

    public String getCancelText() {
        return cancelText == null ? "" : cancelText;
    }

    public int getCancelTextSize() {
        return cancelTextSize;
    }

    public String getConfirmTextColorStr() {
        return confirmTextColorStr == null ? "" : confirmTextColorStr;
    }

    public String getConfirmText() {
        return confirmText == null ? "" : confirmText;
    }

    public int getConfirmTextSize() {
        return confirmTextSize;
    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public String getTitleBackgroundColorStr() {
        return titleBackgroundColorStr == null ? "" : titleBackgroundColorStr;
    }

    public String getTitleTextColorStr() {
        return titleTextColorStr == null ? "" : titleTextColorStr;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public String getDefaultProvinceName() {
        return defaultProvinceName == null ? "" : defaultProvinceName;
    }

    public String getDefaultCityName() {
        return defaultCityName == null ? "" : defaultCityName;
    }

    public String getDefaultDistrict() {
        return defaultDistrict == null ? "" : defaultDistrict;
    }

    public Integer getCustomItemLayout() {
        return customItemLayout == null ? NONE : customItemLayout;
    }

    public Integer getCustomItemTextViewId() {
        return customItemTextViewId == null ? NONE : customItemTextViewId;
    }

    /**
     * 定义显示省市区三种滚轮的显示状态
     * PRO:只显示省份的一级选择器
     * PRO_CITY:显示省份和城市二级联动的选择器
     * PRO_CITY_DIS:显示省份和城市和县区三级联动的选择器
     */
    public enum WheelType {
        PRO, PRO_CITY, PRO_CITY_DIS
    }

    public static class Builder {
        private Context mContext;

        //滚轮显示的item个数
        private int visibleItems = 3;

        private String cancelTextColorStr = "#ffffff";
        private String cancelText = "CANCEL";
        private int cancelTextSize = 16;

        private String confirmTextColorStr = "#ffffff";
        private String confirmText = "OK";
        private int confirmTextSize = 16;

        private String mTitle = "添加城市";
        private String titleBackgroundColorStr = "#C7C7C7";
        private String titleTextColorStr = "#0e73ba";
        private int titleTextSize = 30;

        // 第一次默认的显示省份
        private String defaultProvinceName = "广东";
        private String defaultCityName = "深圳";
        private String defaultDistrict = "罗湖区";
        // 定义默认显示省市区三级联动的滚轮选择器
        private CityConfig.WheelType mWheelType = CityConfig.WheelType.PRO_CITY_DIS;
        // 是否显示半透明的背景
        private boolean isShowBackground = true;
        // 自定义的item布局
        private Integer customItemLayout;
        // 自定义的item txt id
        private Integer customItemTextViewId;

        public Builder(Context context) {
            this.mContext = context;
        }

        // 设置标题背景颜色
        public Builder titleBackgroundColor(String colorBg) {
            this.titleBackgroundColorStr = colorBg;
            return this;
        }

        //设置标题字体颜色
        public Builder titleTextColor(String titleTextColorStr) {
            this.titleTextColorStr = titleTextColorStr;
            return this;
        }

        // 设置标题字体大小
        public Builder titleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        // 设置标题
        public Builder title(String mtitle) {
            this.mTitle = mtitle;
            return this;
        }

        // 确认按钮文字
        public Builder confirmTextSize(int confirmTextSize) {
            this.confirmTextSize = confirmTextSize;
            return this;
        }

        // 确认按钮文字
        public Builder confirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        // 确认按钮文字颜色
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }

        // 取消按钮文字颜色
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }

        // 取消按钮文字大小
        public Builder cancelTextSize(int cancelTextSize) {
            this.cancelTextSize = cancelTextSize;
            return this;
        }

        // 取消按钮文字
        public Builder cancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        // 滚轮显示的item个数
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        // 显示省市区三级联动的显示状态
        public Builder setCityWheelType(CityConfig.WheelType wheelType) {
            this.mWheelType = wheelType;
            return this;
        }

        //第一次默认的显示省份，一般配合定位，使用
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        //第一次默认得显示城市，一般配合定位，使用
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }

        //第一次默认地区显示，一般配合定位，使用
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }

        // 是否显示半透明的背景
        public Builder showBackground(boolean isShowBackground) {
            this.isShowBackground = isShowBackground;
            return this;
        }

        public CityConfig build() {
            CityConfig config = new CityConfig(this);
            return config;
        }
    }
}
