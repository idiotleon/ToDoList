package com.leontheprofessional.todolist.customview.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.leontheprofessional.todolist.R;

public class ToDoItemDeadlineCustomView extends TextView {

    private Paint linePaint;
    private int paperColor;

    public ToDoItemDeadlineCustomView(Context context) {
        super(context);
        init();
    }

    public ToDoItemDeadlineCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToDoItemDeadlineCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.notepad_lines));

        paperColor = getResources().getColor(R.color.notepad_paper);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(paperColor);

        canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        canvas.save();

        super.onDraw(canvas);
        canvas.restore();
    }
}
