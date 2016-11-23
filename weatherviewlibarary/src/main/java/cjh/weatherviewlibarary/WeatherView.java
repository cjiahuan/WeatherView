package cjh.weatherviewlibarary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjh on 16-11-15.
 */

public class WeatherView<T extends IBaseWeatherData> extends View {

    protected static final String TAG = "WeatherView";

    protected Context context;

    //分别是 圆点 文字 线 的画笔
    protected Paint dotPaint, txtPaint, linePaint;

    // 默认的高度 dp
    protected static final int DEFAUL_TMINHEIGHT = 160;

    // 默认的宽度 dp
    protected static final int DEFAULT_MINWIDTH = 100;

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
    protected static final int LINEWIDTH = 2;

    // 文字默认大小
    protected static final int TXTSIZE = 12;

    //文字距离顶部 底部的距离
    protected static final int TXTTOBORDER = 8;

    // 文字与圆点之间的距离
    protected static final int TXTTODOT = 5;//

    // 高温字体颜色
    protected static final int HIGHTXTCOLOR = Color.BLACK;

    // 低温字体颜色
    protected static final int LOWTXTCOLOR = Color.BLACK;

    private DisplayMetrics dm;

    protected Paint.FontMetrics fontMetrics;

    protected int highestDegree = 1;

    protected int lowestDegree = -1;

    protected int txtSize = TXTSIZE;

    protected int txtColor = -1;

    protected int highTXTColor = HIGHTXTCOLOR;

    protected int lowTXTColor = LOWTXTCOLOR;

    protected int txtToDot = TXTTODOT;

    protected int txtToBorder = TXTTOBORDER;

    protected int lineStrokeWidth = LINEWIDTH;

    protected int lineColor = -1;

    protected int highLineColor = HIGHLINECOLOR;

    protected int lowLineColor = LOWLINECOLOR;

    protected int dotColor;

    protected int highDotColor = HIGHDOTCOLOR;

    protected int lowDotColor = LOWDOTCOLOR;

    protected int dotRadiu = WEADOTRADIU;

    protected int todayDotRadiu = -1;

    protected int backgroundColor = CANVASCOLOR;

    protected int position;

    protected int height = DEFAUL_TMINHEIGHT;
    protected int width = DEFAULT_MINWIDTH;

    protected List<T> datas = new ArrayList();

    public void setW(int width) {
        this.width = width;
        this.height = width / 5 * 8;
    }

    public void setDatas(List<T> datas, int highestDegree, int lowestDegree, int position) {
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.position = position;
        postInvalidate();
    }

    public WeatherView(Context context) {
        super(context);
        init(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null)
            initAttrs(attrs);
        dm = getResources().getDisplayMetrics();
        initPaints();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.WeatherView);
        int count = types.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = types.getIndex(i);
            if (attr == R.styleable.WeatherView_txtSize)
                txtSize = types.getDimensionPixelSize(attr, TXTSIZE);
            else if (attr == R.styleable.WeatherView_dotRadiu)
                dotRadiu = types.getDimensionPixelSize(attr, WEADOTRADIU);
            else if (attr == R.styleable.WeatherView_todayDotRadiu)
                todayDotRadiu = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_dotColor)
                dotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_highDotColor)
                highDotColor = types.getColor(attr, HIGHDOTCOLOR);
            else if (attr == R.styleable.WeatherView_lowDotColor)
                lowDotColor = types.getColor(attr, LOWDOTCOLOR);
            else if (attr == R.styleable.WeatherView_lineStrokWidth)
                lineStrokeWidth = types.getDimensionPixelSize(attr, LINEWIDTH);
            else if (attr == R.styleable.WeatherView_txtToBorder)
                txtToBorder = types.getDimensionPixelSize(attr, TXTTOBORDER);
            else if (attr == R.styleable.WeatherView_txtToDot)
                txtToDot = types.getDimensionPixelSize(attr, TXTTODOT);
            else if (attr == R.styleable.WeatherView_txtColor)
                txtColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_highTxtColor)
                highTXTColor = types.getColor(attr, HIGHTXTCOLOR);
            else if (attr == R.styleable.WeatherView_lowTxtColor)
                lowTXTColor = types.getColor(attr, LOWTXTCOLOR);
            else if (attr == R.styleable.WeatherView_lineColor)
                lineColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_highLineColor)
                highLineColor = types.getColor(attr, HIGHLINECOLOR);
            else if (attr == R.styleable.WeatherView_lowLineColor)
                lowLineColor = types.getColor(attr, LOWLINECOLOR);
            else if (attr == R.styleable.WeatherView_width)
                width = types.getDimensionPixelSize(attr, DEFAULT_MINWIDTH);
            else if (attr == R.styleable.WeatherView_height)
                height = types.getDimensionPixelSize(attr, DEFAUL_TMINHEIGHT);
            else if (attr == R.styleable.WeatherView_backgroundColor)
                backgroundColor = types.getColor(attr, CANVASCOLOR);
        }
        types.recycle();
    }

    private void initPaints() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(HIGHDOTCOLOR);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setTextSize(sp2px(txtSize));
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
                result = width + getPaddingLeft() + getPaddingRight();
            else
                result = height + getPaddingBottom() + getPaddingTop();

            if (sizeMode == MeasureSpec.AT_MOST)
                result = Math.min(size, result);

        }
        return result;
    }

    private int baseY, degreeHeight;
    private int w, h;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);

        w = getWidth();
        h = getHeight();
        baseY = dip2px(txtToBorder + txtToDot) + (int) (fontMetrics.descent - fontMetrics.ascent);
        int baseHeight = h - 2 * baseY;
        if (highestDegree == lowestDegree) {
            Log.e(TAG, " n 天内 最高温 和 最低温 居然一样：highestDegree = lowestDegree ！！！！ I can't believe it!!!");
            return;
        }
        degreeHeight = baseHeight / (highestDegree - lowestDegree);
        T t = datas.get(position);

        Point[] tPoints = getTPoints(t);
        canvas.drawCircle(tPoints[0].x, tPoints[0].y, dip2px(dotRadiu), dotPaint);
        canvas.drawCircle(tPoints[1].x, tPoints[1].y, dip2px(dotRadiu), dotPaint);

        Point[] txtBaseLinePoints = getTxtBaseLinePoint(t, tPoints);
        canvas.drawText(t.getHighDegree() + "", txtBaseLinePoints[0].x, txtBaseLinePoints[0].y, txtPaint);
        canvas.drawText(t.getLowDegree() + "", txtBaseLinePoints[1].x, txtBaseLinePoints[1].y, txtPaint);

        if (position > 0) {
            T preT = datas.get(position - 1);
            Point[] leftPoints = getLeftPoints(tPoints, preT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, leftPoints[0].x, leftPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, leftPoints[1].x, leftPoints[1].y, linePaint);
        }

        if (position < datas.size() - 1) {
            T nextT = datas.get(position + 1);
            Point[] rightPoints = getRightPoints(tPoints, nextT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, rightPoints[0].x, rightPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, rightPoints[1].x, rightPoints[1].y, linePaint);
        }
    }


    private Point[] getTPoints(T t) {
        Point[] points = new Point[2];
        int highY = baseY + dip2px(dotRadiu) / 2 + (highestDegree - t.getHighDegree()) * degreeHeight;
        Point highPoint = new Point(w / 2, highY);
        int lowY = highY + (t.getHighDegree() - t.getLowDegree()) * degreeHeight;
        Point lowPoint = new Point(w / 2, lowY);
        points[0] = highPoint;
        points[1] = lowPoint;
        return points;
    }

    private Point[] getLeftPoints(Point[] tPoints, T preT) {
        Point[] points = new Point[2];
        Point[] preTPoints = getTPoints(preT);
        points[0] = new Point(0, (tPoints[0].y + preTPoints[0].y) / 2);
        points[1] = new Point(0, (tPoints[1].y + preTPoints[1].y) / 2);
        return points;
    }

    private Point[] getRightPoints(Point[] tPoints, T nextT) {
        Point[] points = new Point[2];
        Point[] nextTPoints = getTPoints(nextT);
        points[0] = new Point(w, (tPoints[0].y + nextTPoints[0].y) / 2);
        points[1] = new Point(w, (tPoints[1].y + nextTPoints[1].y) / 2);
        return points;
    }

    private Point[] getTxtBaseLinePoint(T t, Point[] tPoints) {
        Point[] baseLinePoints = new Point[2];
        Point middelHighPoint = tPoints[0];
        int disY = dip2px(dotRadiu) / 2 + dip2px(txtToDot);
        int highBaseLineY = middelHighPoint.y - disY;
        int highBaseLineX = (w - (int) txtPaint.measureText(t.getHighDegree() + "")) / 2;
        Point highBaseLinePoint = new Point(highBaseLineX, highBaseLineY);
        Point middleLowPoint = tPoints[1];
        int lowBaseLineY = middleLowPoint.y + disY + Math.abs((int) (fontMetrics.descent + fontMetrics.ascent));
        int lowBaseLineX = (w - (int) txtPaint.measureText(t.getLowDegree() + "")) / 2;
        Point lowBaseLinePoint = new Point(lowBaseLineX, lowBaseLineY);
        baseLinePoints[0] = highBaseLinePoint;
        baseLinePoints[1] = lowBaseLinePoint;
        return baseLinePoints;
    }


    private int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    private int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

}
