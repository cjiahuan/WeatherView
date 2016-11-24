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
    protected static final int DEFAULT_TMINHEIGHT = 100;

    // 默认的宽度 dp
    protected static final int DEFAULT_MINWIDTH = 60;

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

    protected int txtSize = -1;

    protected int txtColor = -1;

    protected int highTXTColor = HIGHTXTCOLOR;

    protected int lowTXTColor = LOWTXTCOLOR;

    protected int txtToDot = -1;

    protected int txtToBorder = -1;

    protected int lineStrokeWidth = LINEWIDTH;

    protected int lineColor = -1;

    protected int highLineColor = HIGHLINECOLOR;

    protected int lowLineColor = LOWLINECOLOR;

    protected int leftHighLineColor = -1;

    protected int rightHighLineColor = -1;

    protected int leftLowLineColor = -1;

    protected int rightLowLineColor = -1;

    protected int dotColor;

    protected int highDotColor = HIGHDOTCOLOR;

    protected int lowDotColor = LOWDOTCOLOR;

    protected int todayHighDotColor = -1;

    protected int todayLowDotColor = -1;

    protected int dotRadiu = -1;

    protected int todayDotRadiu = -1;

    protected int backgroundColor = CANVASCOLOR;

    protected int position;

    protected int height = -1;
    protected int width = -1;

    protected boolean haveMiddleLine;

    protected List<T> datas = new ArrayList();


    /**
     * 设置数据
     * issue: 1.必须设置最低和最高的温度值，以及要展示的数据的position
     * 2.如果你需要在展示数据的时候，需要设置其它属性，那么这个方法必须在最后才能调用，否则控件不会重绘
     * 3.如果，确实需要动态改变属性，可以继承WeatherView，然后重写属性方法, 在调用父类方法之后，在调用  postInvalidate() 或者 invalidate()
     *
     * @param datas
     * @param highestDegree
     * @param lowestDegree
     * @param position
     */
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
        setUpAttr();
        initPaints();
    }

    /**
     * 根据属性的优先级，设置冲突的属性
     */
    private void setUpAttr() {
        if (txtSize == -1)
            txtSize = sp2px(TXTSIZE);
        if (dotRadiu == -1)
            dotRadiu = dip2px(WEADOTRADIU);
        if (todayDotRadiu != -1)
            todayDotRadiu = dotRadiu;
        if (lineStrokeWidth == -1)
            lineStrokeWidth = dip2px(LINEWIDTH);
        if (txtToDot == -1)
            txtToDot = dip2px(TXTTODOT);
        if (txtToBorder == -1)
            txtToBorder = dip2px(txtToBorder);
        if (width == -1)
            width = dip2px(DEFAULT_MINWIDTH);
        if (height == -1)
            height = dip2px(DEFAULT_TMINHEIGHT);
        if (dotColor != -1)
            highDotColor = lowDotColor = dotColor;
        if (todayHighDotColor == -1 || todayLowDotColor == -1)
            todayHighDotColor = todayLowDotColor = HIGHDOTCOLOR;
        if (txtColor != -1)
            highTXTColor = lowTXTColor = txtColor;
        if (lineColor != -1)
            highLineColor = lowLineColor = lineColor;
        if (leftHighLineColor == -1 || rightHighLineColor == -1)
            leftHighLineColor = rightHighLineColor = highLineColor;
        if (leftLowLineColor == -1 || rightLowLineColor == -1)
            leftLowLineColor = rightLowLineColor = lowLineColor;
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.WeatherView);
        int count = types.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = types.getIndex(i);
            if (attr == R.styleable.WeatherView_txtSize)
                txtSize = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_dotRadiu)
                dotRadiu = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_todayDotRadiu)
                todayDotRadiu = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_dotColor)
                dotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_highDotColor)
                highDotColor = types.getColor(attr, HIGHDOTCOLOR);
            else if (attr == R.styleable.WeatherView_lowDotColor)
                lowDotColor = types.getColor(attr, LOWDOTCOLOR);
            else if (attr == R.styleable.WeatherView_todayHighDotColor)
                todayHighDotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_todayLowDotColor)
                todayLowDotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_lineStrokWidth)
                lineStrokeWidth = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_txtToBorder)
                txtToBorder = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_txtToDot)
                txtToDot = types.getDimensionPixelSize(attr, -1);
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
            else if (attr == R.styleable.WeatherView_leftHighLineColor)
                leftHighLineColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_rightHighLineColor)
                rightHighLineColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_leftLowLineColor)
                leftLowLineColor = types.getColor(attr, leftLowLineColor);
            else if (attr == R.styleable.WeatherView_rightLowLineColor)
                rightLowLineColor = types.getColor(attr, -1);
            else if (attr == R.styleable.WeatherView_haveMiddleLine)
                haveMiddleLine = types.getBoolean(attr, false);
            else if (attr == R.styleable.WeatherView_width)
                width = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_height)
                height = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.WeatherView_backgroundColor)
                backgroundColor = types.getColor(attr, CANVASCOLOR);
        }
        types.recycle();
    }

    /**
     * 初始化所要用的三支画笔
     */
    private void initPaints() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(HIGHDOTCOLOR);

        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setTextSize(txtSize);
        fontMetrics = txtPaint.getFontMetrics();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(LINEWIDTH);
    }

    /**
     * 设置WeatherView宽高
     *
     * @param width
     * @param height
     */
    public void setWH(int width, int height) {
        this.width = width;
        this.height = height;
    }


    /**
     * 设置画布背景色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        setUpAttr();
    }

    public void setBackgroundColor(String color) {
        setBackgroundColor(Color.parseColor(color));
    }

    public void setTxtParams(int txtSize, int txtColor, int txtToBorder, int txtToDot) {
        setTxtParams(txtSize, txtColor, txtColor, txtToBorder, txtToDot);
    }

    /**
     * 设置和文本相关的参数, px is default
     *
     * @param txtSize      字体大小
     * @param highTXTColor 高度的文字颜色
     * @param lowTXTColor  低度的文字颜色
     * @param txtToBorder  文字距离顶部/底部的距离
     * @param txtToDot     文字距离 dot 距离
     */
    public void setTxtParams(int txtSize, int highTXTColor, int lowTXTColor, int txtToBorder, int txtToDot) {
        this.txtSize = txtSize;
        this.highTXTColor = highTXTColor;
        this.lowTXTColor = lowTXTColor;
        this.txtToBorder = txtToBorder;
        this.txtToDot = txtToDot;
        setUpAttr();
    }

    public void setDotParams(int dotRadiu, int dotColor) {
        setDotParams(dotRadiu, dotRadiu, dotColor, dotColor);
    }

    /**
     * 设置 dot 相关参数， px is default
     *
     * @param dotRadiu      圆半径
     * @param todayDotRadiu 当天圆半径
     * @param highDotColor
     * @param lowDotColor
     */
    public void setDotParams(int dotRadiu, int todayDotRadiu, int highDotColor, int lowDotColor) {

    }

    public void setDotParams(){

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

        //温度数据的异常判断，如果最高温最低温一样....
        if (highestDegree == lowestDegree) {
            Log.e(TAG, " n 天内 最高温 和 最低温 居然一样：highestDegree = lowestDegree ！！！！ I can't believe it!!!");
            return;
        }

        //------------------------在绘制控件之前的必要数据 base data, 整个控件的绘制都是基于这几个数据---------------------------

        //宽高
        w = getWidth();
        h = getHeight();

        //baseY：代表的是 文字距离顶部/底部的距离 + 文字距离dot的距离 + 文字本身的高度
        baseY = txtToBorder + txtToDot + (int) (fontMetrics.descent - fontMetrics.ascent);

        //最重要的一个数据 baseHeight: 可以看到 h - 2*baseY 其实就是在计算真实可用的温度线的高度区间
        int baseHeight = h - 2 * baseY;

        //在计算出温度线所在的区间后，计算每一度的高度
        degreeHeight = baseHeight / (highestDegree - lowestDegree);

        //---------------------------------------------------------------------------------------------------------------

        T t = datas.get(position);
        Point[] tPoints = getTPoints(t);

        //dot
        canvas.drawCircle(tPoints[0].x, tPoints[0].y, dotRadiu, dotPaint);
        canvas.drawCircle(tPoints[1].x, tPoints[1].y, dotRadiu, dotPaint);

        //txt
        Point[] txtBaseLinePoints = getTxtBaseLinePoint(t, tPoints);
        canvas.drawText(t.getHighDegree() + "", txtBaseLinePoints[0].x, txtBaseLinePoints[0].y, txtPaint);
        canvas.drawText(t.getLowDegree() + "", txtBaseLinePoints[1].x, txtBaseLinePoints[1].y, txtPaint);

        //只要 position >0 都需要画左边的连线
        if (position > 0) {
            T preT = datas.get(position - 1);
            Point[] leftPoints = getLeftPoints(tPoints, preT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, leftPoints[0].x, leftPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, leftPoints[1].x, leftPoints[1].y, linePaint);
        }

        //只要 position 不是最后一个 都需要画右边的连线
        if (position < datas.size() - 1) {
            T nextT = datas.get(position + 1);
            Point[] rightPoints = getRightPoints(tPoints, nextT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, rightPoints[0].x, rightPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, rightPoints[1].x, rightPoints[1].y, linePaint);
        }
    }

    /**
     * 计算当天温度 dot 圆心的坐标
     *
     * @param t
     * @return
     */
    private Point[] getTPoints(T t) {
        Point[] points = new Point[2];
        int highY = baseY + dotRadiu / 2 + (highestDegree - t.getHighDegree()) * degreeHeight;
        Point highPoint = new Point(w / 2, highY);
        int lowY = highY + (t.getHighDegree() - t.getLowDegree()) * degreeHeight;
        Point lowPoint = new Point(w / 2, lowY);
        points[0] = highPoint;
        points[1] = lowPoint;
        return points;
    }

    /**
     * 计算当天line左侧 endX endY 的坐标
     *
     * @param tPoints
     * @param preT
     * @return
     */
    private Point[] getLeftPoints(Point[] tPoints, T preT) {
        Point[] points = new Point[2];
        Point[] preTPoints = getTPoints(preT);
        points[0] = new Point(0, (tPoints[0].y + preTPoints[0].y) / 2);
        points[1] = new Point(0, (tPoints[1].y + preTPoints[1].y) / 2);
        return points;
    }

    /**
     * 计算当天line右侧 endX endY 的坐标
     *
     * @param tPoints
     * @param nextT
     * @return
     */
    private Point[] getRightPoints(Point[] tPoints, T nextT) {
        Point[] points = new Point[2];
        Point[] nextTPoints = getTPoints(nextT);
        points[0] = new Point(w, (tPoints[0].y + nextTPoints[0].y) / 2);
        points[1] = new Point(w, (tPoints[1].y + nextTPoints[1].y) / 2);
        return points;
    }

    /**
     * 计算温度文字的基线的起始点
     *
     * @param t
     * @param tPoints
     * @return
     */
    private Point[] getTxtBaseLinePoint(T t, Point[] tPoints) {
        Point[] baseLinePoints = new Point[2];
        Point middelHighPoint = tPoints[0];
        int disY = dotRadiu / 2 + txtToDot;
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
