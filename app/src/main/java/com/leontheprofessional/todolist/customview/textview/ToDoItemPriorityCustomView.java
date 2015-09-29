package com.leontheprofessional.todolist.customview.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.leontheprofessional.todolist.R;

public class ToDoItemPriorityCustomView extends TextView {

    private static String LOG_TAG = ToDoItemPriorityCustomView.class.getSimpleName();

    private Paint linePaint;
    private Paint circlePaint;
    private int paperColor;

    private int priority = 1;

    public ToDoItemPriorityCustomView(Context context) {
        super(context);
        Log.v(LOG_TAG, "ToDoItemPriorityCustomView(Context context) executed.");

        init();
    }

//    public ToDoItemPriorityCustomView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        Log.v(LOG_TAG, "ToDoItemPriorityCustomView(Context context, AttributeSet attrs) executed.");
//        init();
//    }

    public ToDoItemPriorityCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v(LOG_TAG, "ToDoItemPriorityCustomView(Context context, AttributeSet attrs, int defStyleAttr) executed.");
        priority = defStyleAttr;
        Log.v(LOG_TAG, "defStyleAttr: " + defStyleAttr);
        Log.v(LOG_TAG, "priority, ToDoItemPriorityCustomView: " + priority);
        init();
    }

    private void init() {
        Log.v(LOG_TAG, "priority, init(), ToDoItemPriorityCustomView: " + priority);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(getResources().getColor(R.color.notepad_lines));
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paperColor = getResources().getColor(R.color.notepad_paper);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        switch (priority) {
            case 1:
                circlePaint.setColor(getResources().getColor(R.color.notepad_margin));
                Log.v(LOG_TAG, "case 1 selected");
                break;
            case 2:
                circlePaint.setColor(getResources().getColor(R.color.notepad_lines));
                Log.v(LOG_TAG, "case 2 selected");
                break;
        }

        canvas.drawColor(paperColor);

        // Draw ruled lines
        canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        // Draw a circle
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2, circlePaint);

        canvas.save();

        super.onDraw(canvas);
        canvas.restore();
    }
}
