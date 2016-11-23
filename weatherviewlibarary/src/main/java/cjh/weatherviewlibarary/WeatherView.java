package cjh.weatherviewlibarary;

import android.content.Context;
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

public class WeatherView<T extends BaseWeatherData> extends View {

    private static final String TAG = "WeatherView";

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

    protected int highestDegree = 1;

    protected int lowestDegree = -1;

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

    public void setW(int width) {
        this.width = width;
        this.height = width / 5 * 8;
    }

    public void setDatas(List<T> datas, int highestDegree, int lowestDegree, int currentPosition) {
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.currentPosition = currentPosition;
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
        canvas.drawColor(CANVASCOLOR);

        w = getWidth();
        h = getHeight();
        baseY = dip2px(distanceTxtToBorder + distanceBetweenTxtDot) + sp2px(degreeTextSize);
        int baseHeight = h - 2 * baseY;
        if (highestDegree == lowestDegree) {
            Log.e(TAG, "最高温 和 最低温 居然一样：highestDegree = lowestDegree ！！！！");
            return;
        }
        degreeHeight = baseHeight / (highestDegree - lowestDegree);
        T t = datas.get(currentPosition);

        Point[] tPoints = getTPoints(t);
        canvas.drawCircle(tPoints[0].x, tPoints[0].y, dip2px(weaDotRadiu), dotPaint);
        canvas.drawCircle(tPoints[1].x, tPoints[1].y, dip2px(weaDotRadiu), dotPaint);

        Point[] txtBaseLinePoints = getTxtBaseLinePoint(t, tPoints);
        canvas.drawText(t.highDegree + "", txtBaseLinePoints[0].x, txtBaseLinePoints[0].y, txtPaint);
        canvas.drawText(t.lowDegree + "", txtBaseLinePoints[1].x, txtBaseLinePoints[1].y, txtPaint);

        if (currentPosition > 0) {
            T preT = datas.get(currentPosition - 1);
            Point[] leftPoints = getLeftPoints(tPoints, preT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, leftPoints[0].x, leftPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, leftPoints[1].x, leftPoints[1].y, linePaint);
        }

        if (currentPosition < datas.size() - 1) {
            T nextT = datas.get(currentPosition + 1);
            Point[] rightPoints = getRightPoints(tPoints, nextT);
            canvas.drawLine(tPoints[0].x, tPoints[0].y, rightPoints[0].x, rightPoints[0].y, linePaint);
            canvas.drawLine(tPoints[1].x, tPoints[1].y, rightPoints[1].x, rightPoints[1].y, linePaint);
        }
    }


    private Point[] getTPoints(T t) {
        Point[] points = new Point[2];
        int highY = baseY + dip2px(weaDotRadiu) / 2 + (highestDegree - t.highDegree) * degreeHeight;
        Point highPoint = new Point(w / 2, highY);
        int lowY = highY + (t.highDegree - t.lowDegree) * degreeHeight;
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

    private Point[] getTxtBaseLinePoint(T t, Point[] middlePoints) {
        Point[] baseLinePoints = new Point[2];
        Point middelHighPoint = middlePoints[0];
        int disY = dip2px(weaDotRadiu) / 2 + dip2px(distanceBetweenTxtDot);
        int highBaseLineY = middelHighPoint.y - disY;
        int highBaseLineX = (w - (int) txtPaint.measureText(t.highDegree + "")) / 2;
        Point highBaseLinePoint = new Point(highBaseLineX, highBaseLineY);
        Point middleLowPoint = middlePoints[1];
        int lowBaseLineY = middleLowPoint.y + disY + sp2px(degreeTextSize);
        int lowBaseLineX = (w - (int) txtPaint.measureText(t.lowDegree + "")) / 2;
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
