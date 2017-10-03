package cn.coder_felicia.keyworddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：  KeywordDemo
 * 包名：    cn.coder_felicia.keyworddemo
 * 创建者:   涵月felicia
 * 创建时间:  2017/10/2
 * 描述：  自定义View：长文本显示，关键字高亮
 *      需改进：1.连接绘制时位置有问题
 *             2.StaticLayout：拼接易产生大量对象
 */

public class CustomTextView extends AppCompatTextView {

    private int width;//view宽
    private int height;//view高
    private Paint paint;
    private TextPaint defaultPaint;
    private TextPaint highlightPaint;
    private Canvas canvas;

    private StaticLayout defaultLayout ;
    private StaticLayout highlightLayout;

    private String text = "";
    private List<String> highlighted = new ArrayList();

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setTextSize(60);
        highlightPaint.setTextSize(60);
        defaultPaint.setColor(Color.BLACK);//默认
    }

    /**
     * 设置文本默认颜色
     * @param color
     */
    public void setDefaultColor(int color) {
        defaultPaint.setColor(color);
    }

    /**
     * 设置关键词高亮颜色
     * @param color
     */
    public void setHighlight(int color) {
        highlightPaint.setColor(color);
    }

    /**
     * 设置显示文本及关键词列表
     * @param text
     * @param highlighted
     */
    public void setDisplayedText(String text, List<String> highlighted) {
        this.text = text;
        this.highlighted = highlighted;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        width = getWidth();
        height = getHeight();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        canvas.save();
        canvas.translate(width / 2, height / 2);
        RectF f = new RectF(-500, -260, 500, 260);
        canvas.drawRoundRect(f, 30, 30, paint);
        canvas.restore();


        // 绘字
        // StaticLayout：拼接易产生大量对象，需改进
        int drawPosition = 0;
        float drawPositionwidth = 0;
        float drawPositionhigh = 0;
        String drawText;
        String drawedText = "";
        int keywordNoInTextCount = 0;
        int position = 0;
        int sign = 0;

        canvas.save();
        String keyword;
        for( int i = 0 ; i < highlighted.size() ; i++) {
            keyword = highlighted.get(i);
            //未绘制的text中查找关键字
            position = text.indexOf(keyword,drawPosition);
            if(position == -1){
                keywordNoInTextCount++;
                continue;
            }

            //drawText:关键字前的text
            drawText = text.substring(drawPosition,position);
            drawPositionwidth = (int) defaultPaint.measureText(drawedText) % (width - 120);
            drawPositionhigh = (int)defaultPaint.measureText(drawedText)/(width-120);

            defaultLayout = new StaticLayout(drawText, defaultPaint, width-120, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            canvas.translate(drawPositionwidth, drawPositionhigh);
            defaultLayout.draw(canvas);
            sign++;

            drawedText += drawText;
            drawPosition += drawText.length();

            //绘制关键字
            float blankPlace = width-(defaultPaint.measureText(drawedText))%(width-120)-120;
            float keywordSize = highlightPaint.measureText(keyword);

            if(blankPlace < keywordSize) {
                highlightLayout = new StaticLayout("\n" + keyword, highlightPaint, width - 120, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            } else{
                highlightLayout = new StaticLayout(keyword, highlightPaint, width - 120, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            }

            if(sign == 0) {
                drawPositionwidth = (int) defaultPaint.measureText(drawedText) % (width - 120);
            }else {
                drawPositionwidth = ((int) defaultPaint.measureText(drawedText) % (width - 120))/2;
            }
            drawPositionhigh = (int)defaultPaint.measureText(drawedText)/(width-120);
            canvas.translate(drawPositionwidth, drawPositionhigh);
            highlightLayout.draw(canvas);

            drawedText += keyword;
            drawPosition += keyword.length();
        }
        //关键字遍历后剩余字符
        if(drawPosition<text.length()){
            defaultLayout = new StaticLayout(text.substring(drawPosition), defaultPaint, width-120, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            drawPositionwidth = (int)defaultPaint.measureText(drawedText)%(width-120);
            drawPositionhigh = (int)defaultPaint.measureText(drawedText)/(width-120);
            canvas.translate(drawPositionwidth, drawPositionhigh);
            defaultLayout.draw(canvas);
        }
        canvas.restore();


        //用户输入特殊情况处理
        if(highlighted.size() == 0||keywordNoInTextCount == highlighted.size()) {
            defaultLayout = new StaticLayout(text, defaultPaint, width-120, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
            canvas.save();
            canvas.translate(60, 30);
            defaultLayout.draw(canvas);
            canvas.restore();
        }

//        canvas.drawText()方法不能自动换行，手动还需判断非关键词换行并且不能被截断，故没考虑
//        measuredText = defaultPaint.measureText(text);
//        canvas.drawText(text, 50, 200, paint);
//        canvas.drawText(text1, 50 + measuredText, 200, paint1);

        //刷新界面
        // clearAnimation();
        invalidate();


    }


}
