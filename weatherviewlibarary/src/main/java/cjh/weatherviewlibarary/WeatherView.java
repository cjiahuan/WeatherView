package cjh.weatherviewlibarary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by cjh on 16-11-15.
 */

public class WeatherView extends View {

    protected Context context;

    protected static final int DEFAULTMINHEIGHT = 80;

    protected static final int DEFAULTMINWIDTH = 50;

    protected static final int CANVASCOLOR = Color.YELLOW;

    protected static final int HIGHLINECOLOR = Color.BLUE;

    protected static final int LOWLINECOLOR = Color.BLACK;

    protected static final int TEMPERATURETEXTSIZE = 16;

    protected static final int TEMPERATURETEXTCOLOR = Color.BLACK;

    protected static final int WEATHERTEXTSIZE = 14;

    protected static final int WEATHERHIGHTEXTCOLOR = Color.BLUE;

    protected static final int WEATHERLOWTEXTCOLOR = Color.RED;

    private DisplayMetrics dm;

    public WeatherView(Context context) {
        super(context);
        init(context);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        dm = getResources().getDisplayMetrics();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthResult = getSize(width, widthMode, 0);
        int heightResult = getSize(height, heightMode, 1);
        setMeasuredDimension(widthResult, heightResult);
    }

    private int getSize(int size, int sizeMode, int type) {
        int result = -1;
        if (sizeMode == MeasureSpec.EXACTLY)
            result = size;
        else {
            if (type == 0)
                result = dip2px(DEFAULTMINWIDTH) + getPaddingLeft() + getPaddingRight();
            else
                result = dip2px(DEFAULTMINHEIGHT) + getPaddingBottom() + getPaddingTop();

            if (sizeMode == MeasureSpec.AT_MOST)
                result = Math.min(size, result);

        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    private int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

}
