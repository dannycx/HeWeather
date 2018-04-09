package com.dcx.style.citypickerview.widget.wheel.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 抽象车轮适配器为适配器提供通用功能
 */
public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {

    /**
     * Text view resource. Used as a default view for adapter.
     */
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;
    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;
    /**
     * No resource constant.
     */
    protected static final int NO_RESOURCE = 0;
    // Current context
    protected Context context;
    // Layout inflater
    protected LayoutInflater inflater;
    // Items resources
    protected int itemResourceId;
    protected int itemTextResourceId;
    // Empty items resources
    protected int emptyItemResourceId;
    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textSize = DEFAULT_TEXT_SIZE;
    private int padding = 5;

    protected AbstractWheelTextAdapter(Context context) {
        this(context, TEXT_VIEW_ITEM_RESOURCE);
    }

    protected AbstractWheelTextAdapter(Context context, int itemResource) {
        this(context, itemResource, NO_RESOURCE);
    }

    protected AbstractWheelTextAdapter(Context context, int itemResource, int itemTextResource) {
        this.context = context;
        itemResourceId = itemResource;
        itemTextResourceId = itemTextResource;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Sets text color
     *
     * @param textColor the text color to set
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * item间距
     *
     * @return
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * Sets text size
     *
     * @param textSize the text size to set
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * Sets resource Id for items views
     *
     * @param itemResourceId the resource Id to set
     */
    public void setItemResource(int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }

    /**
     * Sets resource Id for text view in item layout
     *
     * @param itemTextResourceId the item text resource Id to set
     */
    public void setItemTextResource(int itemTextResourceId) {
        this.itemTextResourceId = itemTextResourceId;
    }

    /**
     * Sets resource Id for empty items views
     *
     * @param emptyItemResourceId the empty item resource Id to set
     */
    public void setEmptyItemResource(int emptyItemResourceId) {
        this.emptyItemResourceId = emptyItemResourceId;
    }

    /**
     * Returns text for specified item
     *
     * @param index the item index
     * @return the text of specified items
     */
    protected abstract CharSequence getItemText(int index);

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index >= 0 && index < getItemsCount()) {
            if (convertView == null) {
                convertView = getView(itemResourceId, parent);
            }
            TextView textView = getTextView(convertView, itemTextResourceId);
            if (textView != null) {
                CharSequence text = getItemText(index);
                if (text == null) {
                    text = "";
                }
                textView.setText(text);

                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView);
                }
            }
            return convertView;
        }
        return null;
    }

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(emptyItemResourceId, parent);
        }
        if (emptyItemResourceId == TEXT_VIEW_ITEM_RESOURCE && convertView instanceof TextView) {
            configureTextView((TextView) convertView);
        }

        return convertView;
    }

    /**
     * 配置文本视图
     *
     * @param view the text view to be configured
     */
    protected void configureTextView(TextView view) {
        view.setTextColor(textColor);
        view.setGravity(Gravity.CENTER);
        view.setPadding(0, padding, 0, padding);
        view.setTextSize(textSize);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setLines(1);
        //        view.setCompoundDrawablePadding(20);
        //        view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    }

    /**
     * Loads a text view from view
     *
     * @param view         the text view or layout containing it
     * @param textResource the text resource Id in layout
     * @return the loaded text view
     */
    private TextView getTextView(View view, int textResource) {
        TextView text = null;
        try {
            if (textResource == NO_RESOURCE && view instanceof TextView) {
                text = (TextView) view;
            } else if (textResource != NO_RESOURCE) {
                text = (TextView) view.findViewById(textResource);
            }
        } catch (ClassCastException e) {
            Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", e);
        }

        return text;
    }

    /**
     * Loads view from resources
     *
     * @param resource the resource Id
     * @return the loaded view or null if resource is not set
     */
    private View getView(int resource, ViewGroup parent) {
        switch (resource) {
            case NO_RESOURCE:
                return null;
            case TEXT_VIEW_ITEM_RESOURCE:
                return new TextView(context);
            default:
                return inflater.inflate(resource, parent, false);
        }
    }
}
