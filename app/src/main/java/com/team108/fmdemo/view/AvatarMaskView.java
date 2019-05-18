package com.team108.fmdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by shuhuan on 2019/2/21.
 */

public class AvatarMaskView extends RelativeLayout {
    private Context mContext;
    private Path mPath;
    public AvatarMaskView(Context context) {
        this(context, null);
    }

    public AvatarMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setWillNotDraw(false);
        initView();
    }

    private void initView() {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath = new Path();
        RectF rect = new RectF(0, 0, w, h);
        mPath.addRoundRect(rect, h * 0.5f, h * 0.5f, Path.Direction.CW);
        mPath.close();
    }
}
