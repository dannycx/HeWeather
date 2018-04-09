package com.dcx.citywheel;

import android.content.Context;

import com.dcx.bean.CityBean;
import com.dcx.bean.DistrictBean;
import com.dcx.bean.ProvinceBean;
import com.dcx.utils.utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区解析辅助类
 */
public class CityParseHelper {
    //省份数据
    private ArrayList<ProvinceBean> mProvinceBeanArrayList = new ArrayList<>();

    //城市数据
    private ArrayList<ArrayList<CityBean>> mCityBeanArrayList;

    //地区数据
    private ArrayList<ArrayList<ArrayList<DistrictBean>>> mDistrictBeanArrayList;

    private ProvinceBean[] mProvinceBeenArray;

    private ProvinceBean mProvinceBean;

    private CityBean mCityBean;

    private DistrictBean mDistrictBean;

    //key - 省 value - 市
    private Map<String, CityBean[]> mPro_CityMap = new HashMap<String, CityBean[]>();

    //key - 市 values - 区
    private Map<String, DistrictBean[]> mCity_DisMap = new HashMap<String, DistrictBean[]>();

    //key - 区 values - 邮编
    private Map<String, DistrictBean> mDisMap = new HashMap<String, DistrictBean>();

    //默认加载的城市数据type，只包含基本的省市区名称，不包含code、经纬度、拼音等数据
    private String cityJsonDataType = "simple_cities_pro_city_dis.json";

    public CityParseHelper() {
    }

    public ArrayList<ProvinceBean> getProvinceBeanArrayList() {
        return mProvinceBeanArrayList;
    }

    public ProvinceBean[] getProvinceBeenArray() {
        return mProvinceBeenArray;
    }

    public ProvinceBean getProvinceBean() {
        return mProvinceBean;
    }

    public void setProvinceBean(ProvinceBean provinceBean) {
        mProvinceBean = provinceBean;
    }

    public CityBean getCityBean() {
        return mCityBean;
    }

    public void setCityBean(CityBean cityBean) {
        mCityBean = cityBean;
    }

    public DistrictBean getDistrictBean() {
        return mDistrictBean;
    }

    public void setDistrictBean(DistrictBean districtBean) {
        mDistrictBean = districtBean;
    }

    public Map<String, CityBean[]> getPro_CityMap() {
        return mPro_CityMap;
    }

    public Map<String, DistrictBean[]> getCity_DisMap() {
        return mCity_DisMap;
    }

    public Map<String, DistrictBean> getDisMap() {
        return mDisMap;
    }

    //初始化数据，解析json数据
    public void initData(Context context) {
        cityJsonDataType = "simple_cities_pro_city_dis.json";
        String cityJson = utils.getJson(context, cityJsonDataType);
        Type type = new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType();

        mProvinceBeanArrayList = new Gson().fromJson(cityJson, type);
        if (mProvinceBeanArrayList == null || mProvinceBeanArrayList.isEmpty()) {
            return;
        }
        mCityBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());
        mDistrictBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());

        // 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
        if (mProvinceBeanArrayList != null && !mProvinceBeanArrayList.isEmpty()) {
            mProvinceBean = mProvinceBeanArrayList.get(0);
            List<CityBean> cityList = mProvinceBean.getCityList();
            if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                mCityBean = cityList.get(0);
                List<DistrictBean> districtList = mCityBean.getCityList();
                if (districtList != null && !districtList.isEmpty() && districtList.size() > 0) {
                    mDistrictBean = districtList.get(0);
                }
            }
        }

        //省份数据
        mProvinceBeenArray = new ProvinceBean[mProvinceBeanArrayList.size()];
        for (int p = 0; p < mProvinceBeanArrayList.size(); p++) {
            //遍历每个省份
            ProvinceBean itemProvince = mProvinceBeanArrayList.get(p);
            //每个省份对应下面的市
            ArrayList<CityBean> cityList = itemProvince.getCityList();
            //当前省份下面的所有城市
            CityBean[] cityNames = new CityBean[cityList.size()];
            //遍历当前省份下面城市的所有数据
            for (int j = 0; j < cityList.size(); j++) {
                cityNames[j] = cityList.get(j);
                //当前省份下面每个城市下面再次对应的区或者县
                List<DistrictBean> districtList = cityList.get(j).getCityList();
                if (districtList == null) {
                    break;
                }
                DistrictBean[] distrinctArray = new DistrictBean[districtList.size()];
                for (int k = 0; k < districtList.size(); k++) {
                    // 遍历市下面所有区/县的数据
                    DistrictBean districtModel = districtList.get(k);
                    //存放 省市区 数据
                    mDisMap.put(itemProvince.getName() + cityNames[j].getName() + districtList.get(k).getName(),
                            districtModel);
                    distrinctArray[k] = districtModel;
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mCity_DisMap.put(itemProvince.getName() + cityNames[j].getName(), distrinctArray);
            }

            // 省-市的数据，保存到mCitisDatasMap
            mPro_CityMap.put(itemProvince.getName(), cityNames);
            mCityBeanArrayList.add(cityList);

            //只有显示三级联动，才会执行
            ArrayList<ArrayList<DistrictBean>> array2DistrictLists = new ArrayList<>(cityList.size());
            for (int c = 0; c < cityList.size(); c++) {
                CityBean cityBean = cityList.get(c);
                array2DistrictLists.add(cityBean.getCityList());
            }
            mDistrictBeanArrayList.add(array2DistrictLists);
            //赋值所有省份的名称
            mProvinceBeenArray[p] = itemProvince;
        }
    }
}
