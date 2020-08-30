package com.example.administrator.hearinghelp;

/**
 * Created by John.Doe on 2020/3/27.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ChartView extends View {
    private float xLength;
    private float yLength;
    private float startPointX;
    private float startPointY;
    private float xScale;
    private float yScale;
    private float coordTextSize;

    private String[] xLabel;
    private String[] yLabel;
    private String[] data;
    private String title;

    private String[] dataLineColor;
    private float[][] dataCoord;

    private Paint scaleLinePaint;
    private Paint dataLinePaint;
    private Paint scaleValuePaint;
    private Paint backgroundPaint;

    private Rect bounds;

    private boolean isClick;
    private int clickIndex;

    private PopupWindow popupWindow;

    public ChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initChart();
    }

    private void initChart() {
        bounds = new Rect();
        dataCoord = new float[8][2];
        clickIndex = -1;

        this.setBackgroundColor(Color.WHITE);

        xLabel = new String[]{"125", "250", "500", "1000", "2000", "4000", "6000", "8000"};
        yLabel = new String[]{"10", "20", "30", "40", "50", "60", "70", "80", "90"};
        dataLineColor = new String[]{"#ff6b02", "#ff6b02", "#ff6b02", "#ff6b02", "#ff6b02", "#ff6b02", "#ff6b02", "#ff6b02"};

        if (title == null)
            title = "听力检测结果";

        if (data == null)
            data = new String[]{"35", "35", "35", "35", "35", "35", "35", "35"};

        dataLinePaint = new Paint();
        scaleLinePaint = new Paint();
        scaleValuePaint = new Paint();
        backgroundPaint = new Paint();

        dataLinePaint.setAntiAlias(true);
        scaleLinePaint.setAntiAlias(true);
        scaleValuePaint.setAntiAlias(true);
        backgroundPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        initParam();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawYAxis_And_XScaleValue(canvas);
        drawXAxis_And_YScaleValue(canvas);
        drawDataLines(canvas);
        drawDataPoint(canvas);
        drawTitle(canvas);
    }

    private void drawBackground(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 1) {
                canvas.drawRect(startPointX + (i - 1) * xScale,
                        startPointY,
                        startPointX + i * xScale,
                        startPointY + yLength,
                        backgroundPaint);
            }
        }
    }

    private void drawYAxis_And_XScaleValue(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            scaleValuePaint.getTextBounds(xLabel[i], 0, xLabel[i].length(), bounds);

            canvas.drawLine(startPointX + i * xScale,
                    startPointY,
                    startPointX + i * xScale,
                    startPointY + yLength,
                    scaleLinePaint);

            if (i == 0) {
                canvas.drawText(xLabel[i],
                        startPointX,
                        startPointY + yLength + bounds.height() + yScale / 15,
                        scaleValuePaint);
            } else {
                canvas.drawText(xLabel[i],
                        startPointX + i * xScale - bounds.width() / 2,
                        startPointY + yLength + bounds.height() + yScale / 15,
                        scaleValuePaint);
            }
        }
    }

    private void drawXAxis_And_YScaleValue(Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            if (i < 9) {
                scaleValuePaint.getTextBounds(yLabel[8 - i], 0, yLabel[8 - i].length(), bounds);

                canvas.drawText(yLabel[8 - i],
                        startPointX + xScale / 15,
                        startPointY + yScale * (i + 0.5f) + bounds.height() / 2,
                        scaleValuePaint);

                canvas.drawLine(startPointX + bounds.width() + 2 * xScale / 15,
                        startPointY + (i + 0.5f) * yScale,
                        startPointX + xLength,
                        startPointY + (i + 0.5f) * yScale,
                        scaleLinePaint);
            } else {
                canvas.drawLine(startPointX,
                        startPointY + (i + 0.5f) * yScale,
                        startPointX + xLength,
                        startPointY + (i + 0.5f) * yScale,
                        scaleLinePaint);
            }
        }
    }

    private void drawDataLines(Canvas canvas) {
        getDataRoord();

        for (int i = 0; i < 7; i++) {
            dataLinePaint.setColor(Color.parseColor(dataLineColor[i]));
            canvas.drawLine(dataCoord[i][0], dataCoord[i][1], dataCoord[i + 1][0], dataCoord[i + 1][1], dataLinePaint);
        }
    }

    private void drawDataPoint(Canvas canvas) {
        if (isClick && clickIndex > -1) {
            dataLinePaint.setColor(Color.parseColor(dataLineColor[clickIndex]));
            canvas.drawCircle(dataCoord[clickIndex][0], dataCoord[clickIndex][1], xScale / 10, dataLinePaint);

            dataLinePaint.setColor(Color.WHITE);
            canvas.drawCircle(dataCoord[clickIndex][0], dataCoord[clickIndex][1], xScale / 20, dataLinePaint);

            dataLinePaint.setColor(Color.parseColor(dataLineColor[clickIndex]));
        }
    }

    private void drawTitle(Canvas canvas) {
        dataLinePaint.getTextBounds(title, 0, title.length(), bounds);

        canvas.drawText(title,
                (getWidth() - bounds.width()) / 2,
                startPointY + yLength + yScale,
                dataLinePaint);

        /* canvas.drawLine(getWidth() - bounds.width() / 2 - xScale / 15,
                startPointY + yLength + yScale - bounds.height() / 2 + coordTextSize / 4,
                (getWidth() - bounds.width()) / 2 - xScale / 2,
                startPointY + yLength + yScale - bounds.height() / 2 + coordTextSize / 4,
                dataLinePaint); */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        for (int i = 0; i < 8; i++) {
            float dataX = dataCoord[i][0];
            float dataY = dataCoord[i][1];

            if (Math.abs(touchX - dataX) < xScale / 2 && Math.abs(touchY - dataY) < yScale / 2) {
                isClick = true;
                clickIndex = i;
                invalidate();
                showDetail(i);
                return true;
            } else {
                hideDetail();
            }
            clickIndex = -1;
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    private void showDetail(int index) {
        if (popupWindow != null)
            popupWindow.dismiss();

        TextView textView = new TextView(getContext());
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.shape_pop_bg);
        textView.setPadding(20, 0, 20, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setText(data[index] + "dB");

        GradientDrawable gradientDrawable = (GradientDrawable) textView.getBackground();
        gradientDrawable.setColor(Color.parseColor(dataLineColor[index]));

        popupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setFocusable(false);

        int xoff = (int) (dataCoord[index][0] - 0.6 * xScale);
        int yoff = (int) (dataCoord[index][1] - yScale - getHeight());

        popupWindow.showAsDropDown(this, xoff, yoff);
        popupWindow.update();
    }

    private void hideDetail() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    private void getDataRoord() {
        float oddPointX = startPointX;
        float oddPointY = startPointY + yLength - yScale;

        for (int i = 0; i < data.length; i++) {
            dataCoord[i][0] = oddPointX + i * xScale;

            float dataY = Float.parseFloat(data[i]);
            float oddY = Float.parseFloat(yLabel[0]);

            dataCoord[i][1] = oddPointY - (yScale *(dataY - oddY) / (Float.parseFloat(yLabel[1]) - Float.parseFloat(yLabel[0])));
        }
    }

    private void initParam() {
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        xScale = width / 8.5f;
        yScale = height / 11.5f;

        startPointX = xScale / 2;
        startPointY = yScale / 2;

        xLength = 7.5f * xScale;
        yLength = 9.5f * yScale;

        float chartLineStrokeWidth = xScale / 50;
        float dataLineStrokeWidth = xScale / 30;
        coordTextSize = xScale / 5;

        backgroundPaint.setColor(0x11DEDE68);

        scaleLinePaint.setStrokeWidth(chartLineStrokeWidth);
        scaleLinePaint.setColor(0xFFDEDCDB);

        scaleValuePaint.setColor(0xFF999999);
        scaleValuePaint.setTextSize(coordTextSize);

        dataLinePaint.setStrokeWidth(dataLineStrokeWidth);
        dataLinePaint.setStrokeCap(Paint.Cap.ROUND);
        dataLinePaint.setTextSize(1.5f * coordTextSize);
    }

    public void setxLabel(String[] xLabel) {
        this.xLabel = xLabel;
    }

    public void setyLabel(String[] yLabel) {
        this.yLabel = yLabel;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void fresh() {
        initChart();
        requestLayout();
        postInvalidate();
    }
}
