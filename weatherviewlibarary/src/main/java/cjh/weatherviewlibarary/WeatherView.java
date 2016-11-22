package cjh.weatherviewlibarary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjh on 16-11-15.
 */

public class WeatherView<T extends BaseWeatherData> extends View {

    protected Context context;

    //分别是 圆点 文字 线 的画笔
    private Paint dotPaint, txtPaint, linePaint;

    // 默认的高度 dp
    protected static final int DEFAULTMINHEIGHT = 160;

    // 默认的宽度 dp
    protected static final int DEFAULTMINWIDTH = 100;

    // 圆点画笔 配 置
    protected static final int HIGHDOTCOLOR = Color.BLACK;

    protected static final int LOWDOTCOLOR = Color.BLACK;

    protected static final int WEADOTRADIU = 2;

    // 默认画布的背景色
    protected static final int CANVASCOLOR = Color.YELLOW;

    // 高温线画笔颜色
    protected static final int HIGHLINECOLOR = Color.BLUE;

    // 低温线画笔颜色
    protected static final int LOWLINECOLOR = Color.BLACK;

    // 温度线的默认宽度
    protected static final int LINEWIDTH = 1;

    // 文字默认大小
    protected static final int DEGREETEXTSIZE = 12;

    // 文字与圆点之间的距离
    protected static final int DISTANCEBETWEENTXTDOT = 5;//

    //文字距离顶部 底部的距离
    protected static final int DISTANCETXTTOBORDER = 8;

    // 高温字体颜色
    protected static final int HIGHTEXTCOLOR = Color.BLACK;

    // 低温字体颜色
    protected static final int LOWTEXTCOLOR = Color.BLACK;

    private DisplayMetrics dm;

    protected Paint.FontMetrics fontMetrics;

    protected int highestDegree, lowestDegree;

    protected int degreeTextSize = DEGREETEXTSIZE;

    protected int distanceBetweenTxtDot = DISTANCEBETWEENTXTDOT;

    protected int distanceTxtToBorder = DISTANCETXTTOBORDER;

    protected int lineWidth = LINEWIDTH;

    protected int highLineColor = HIGHLINECOLOR;

    private int lowLineColor = LOWLINECOLOR;

    protected int highDotColor = HIGHDOTCOLOR;

    protected int lowDotColor = LOWDOTCOLOR;

    protected int weaDotRadiu = WEADOTRADIU;

    private int currentPosition;

    private int height = DEFAULTMINHEIGHT;
    private int width = DEFAULTMINWIDTH;

    private List<T> datas = new ArrayList();

    private void setDatas(List<T> datas, int highestDegree, int lowestDegree) {
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        postInvalidate();
    }

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
        initPaints();
    }

    private void initPaints() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(HIGHDOTCOLOR);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setTextSize(sp2px(degreeTextSize));
        fontMetrics = txtPaint.getFontMetrics();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(LINEWIDTH);
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
                result = dip2px(width) + getPaddingLeft() + getPaddingRight();
            else
                result = dip2px(height) + getPaddingBottom() + getPaddingTop();

            if (sizeMode == MeasureSpec.AT_MOST)
                result = Math.min(size, result);

        }
        return result;
    }

    private int baseY, degreeHeight, pxDegreeTextSize;
    private int w, h;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(CANVASCOLOR);

        w = getWidth();
        h = getHeight();
        baseY = dip2px(distanceTxtToBorder + distanceBetweenTxtDot) + sp2px(degreeTextSize);
        int baseHeight = w - 2 * baseY;
        degreeHeight = baseHeight / (highestDegree - lowestDegree);
        T t = datas.get(currentPosition);
        if (currentPosition == 0 && currentPosition < datas.size() - 1) {
            T nextT = datas.get(currentPosition + 1);

        } else if (currentPosition == datas.size() - 1) {
            if (datas.size() > 1) {
                T preT = datas.get(currentPosition - 1);

            } else {

            }
        } else {

        }
    }


    private Point[] getMiddlePoints(T t) {
        Point[] points = new Point[2];
        int highY = baseY + dip2px(weaDotRadiu) / 2 + (highestDegree - t.highDegree) * degreeHeight;
        Point highPoint = new Point(w / 2, highY);
        int lowY = highY + (t.highDegree - t.lowDegree) * degreeHeight;
        Point lowPoint = new Point(w / 2, lowY);
        points[0] = highPoint;
        points[1] = lowPoint;
        return points;
    }

    private Point[] getLeftPoints(T t, T preT) {
        Point[] points = new Point[2];
        int leftHighDegree = (t.highDegree + preT.highDegree) / 2;
        int leftLowDegree = (t.lowDegree + preT.lowDegree) / 2;
        int leftHighY = baseY + dip2px(weaDotRadiu) / 2 + (highestDegree - leftHighDegree) * degreeHeight;
        Point leftHighPoint = new Point(0, leftHighY);
        int leftLowY = leftHighY + (leftHighDegree - leftLowDegree) * degreeHeight;
        Point leftLowPoint = new Point(0, leftLowY);
        points[0] = leftHighPoint;
        points[1] = leftLowPoint;
        return points;
    }

    private Point[] getRightPoints(T t, T nextT) {
        Point[] points = new Point[2];
        int rightHighDegree = (t.highDegree + nextT.highDegree) / 2;
        int rightLowDegree = (t.lowDegree + nextT.lowDegree) / 2;
        int rightHighY = baseY + dip2px(weaDotRadiu) / 2 + (highestDegree - rightHighDegree) * degreeHeight;
        Point rightHighPoint = new Point(w, rightHighY);
        int rightLowY = rightHighY + (rightHighDegree - rightLowDegree) * degreeHeight;
        Point rightLowPoint = new Point(w, rightLowY);
        points[0] = rightHighPoint;
        points[1] = rightLowPoint;
        return points;
    }

    private Point[] getTxtBaseLinePoint(Point[] points) {
        
    }


    private int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    private int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

}
