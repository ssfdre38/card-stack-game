package com.cardstack.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CardView extends View {
    private Card card;
    private Paint cardPaint;
    private Paint borderPaint;
    private Paint textPaint;
    private Paint iconPaint;
    private Paint whitePaint;
    private boolean isSmall = false;

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        cardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
        borderPaint.setColor(Color.WHITE);
        
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(72);
        textPaint.setFakeBoldText(true);
        
        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconPaint.setColor(Color.WHITE);
        iconPaint.setStyle(Paint.Style.FILL);
        
        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);
    }

    public void setCard(Card card) {
        this.card = card;
        invalidate();
    }

    public void setSmall(boolean small) {
        this.isSmall = small;
        if (small) {
            textPaint.setTextSize(36);
            borderPaint.setStrokeWidth(4);
        } else {
            textPaint.setTextSize(72);
            borderPaint.setStrokeWidth(8);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (card == null) return;
        
        int width = getWidth();
        int height = getHeight();
        float cornerRadius = width * 0.15f;
        
        // Draw card background with rounded corners
        cardPaint.setColor(card.getColorResource());
        RectF rect = new RectF(10, 10, width - 10, height - 10);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, cardPaint);
        
        // Draw white border
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint);
        
        // Draw white oval in center
        float ovalPadding = width * 0.15f;
        RectF ovalRect = new RectF(
            ovalPadding, 
            height * 0.25f, 
            width - ovalPadding, 
            height * 0.75f
        );
        canvas.drawOval(ovalRect, whitePaint);
        
        // Draw icon and text in center
        float centerX = width / 2f;
        float centerY = height / 2f;
        
        if (card.getType() == Card.Type.NUMBER) {
            // Draw number
            Paint numberPaint = new Paint(textPaint);
            numberPaint.setColor(card.getColorResource());
            numberPaint.setTextSize(isSmall ? 48 : 96);
            canvas.drawText(String.valueOf(card.getNumber()), centerX, centerY + (isSmall ? 18 : 36), numberPaint);
            
            // Draw small number in corners
            numberPaint.setTextSize(isSmall ? 20 : 32);
            canvas.drawText(String.valueOf(card.getNumber()), width * 0.2f, height * 0.15f, textPaint);
            canvas.drawText(String.valueOf(card.getNumber()), width * 0.8f, height * 0.9f, textPaint);
            
        } else {
            // Draw icon for special cards
            drawCardIcon(canvas, centerX, centerY, card.getType());
            
            // Draw small icons in corners
            float cornerSize = isSmall ? 0.5f : 1f;
            canvas.save();
            canvas.translate(width * 0.2f, height * 0.15f);
            canvas.scale(cornerSize * 0.4f, cornerSize * 0.4f);
            drawCardIcon(canvas, 0, 0, card.getType());
            canvas.restore();
            
            canvas.save();
            canvas.translate(width * 0.8f, height * 0.9f);
            canvas.scale(cornerSize * 0.4f, cornerSize * 0.4f);
            canvas.rotate(180);
            drawCardIcon(canvas, 0, 0, card.getType());
            canvas.restore();
        }
    }

    private void drawCardIcon(Canvas canvas, float cx, float cy, Card.Type type) {
        Paint iconColorPaint = new Paint(iconPaint);
        iconColorPaint.setColor(card.getColorResource());
        float size = isSmall ? 30 : 60;
        
        switch (type) {
            case SKIP:
                drawSkipIcon(canvas, cx, cy, size, iconColorPaint);
                break;
            case REVERSE:
                drawReverseIcon(canvas, cx, cy, size, iconColorPaint);
                break;
            case DRAW_TWO:
                drawDrawTwoIcon(canvas, cx, cy, size, iconColorPaint);
                break;
            case WILD:
                drawWildIcon(canvas, cx, cy, size);
                break;
            case WILD_DRAW_FOUR:
                drawWildDrawFourIcon(canvas, cx, cy, size);
                break;
        }
    }

    private void drawSkipIcon(Canvas canvas, float cx, float cy, float size, Paint paint) {
        // Draw circle with slash (prohibition sign)
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(size / 8);
        canvas.drawCircle(cx, cy, size * 0.8f, paint);
        
        // Draw diagonal slash
        paint.setStrokeWidth(size / 6);
        canvas.drawLine(cx - size * 0.5f, cy - size * 0.5f, cx + size * 0.5f, cy + size * 0.5f, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawReverseIcon(Canvas canvas, float cx, float cy, float size, Paint paint) {
        // Draw two curved arrows in opposite directions
        Path path = new Path();
        
        // Left arrow (counterclockwise)
        path.moveTo(cx - size * 0.6f, cy - size * 0.2f);
        path.lineTo(cx - size * 0.4f, cy - size * 0.5f);
        path.lineTo(cx - size * 0.2f, cy - size * 0.2f);
        
        // Right arrow (clockwise)
        path.moveTo(cx + size * 0.6f, cy + size * 0.2f);
        path.lineTo(cx + size * 0.4f, cy + size * 0.5f);
        path.lineTo(cx + size * 0.2f, cy + size * 0.2f);
        
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(size / 8);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        canvas.drawPath(path, paint);
        
        // Draw curved lines
        Path curve1 = new Path();
        curve1.moveTo(cx - size * 0.4f, cy - size * 0.3f);
        curve1.quadTo(cx - size * 0.5f, cy, cx - size * 0.4f, cy + size * 0.3f);
        canvas.drawPath(curve1, paint);
        
        Path curve2 = new Path();
        curve2.moveTo(cx + size * 0.4f, cy + size * 0.3f);
        curve2.quadTo(cx + size * 0.5f, cy, cx + size * 0.4f, cy - size * 0.3f);
        canvas.drawPath(curve2, paint);
        
        paint.setStyle(Paint.Style.FILL);
    }

    private void drawDrawTwoIcon(Canvas canvas, float cx, float cy, float size, Paint paint) {
        // Draw "+2" text
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(size * 1.2f);
        paint.setFakeBoldText(true);
        canvas.drawText("+2", cx, cy + size * 0.4f, paint);
    }

    private void drawWildIcon(Canvas canvas, float cx, float cy, float size) {
        // Draw four colored quadrants
        Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        // Red quadrant (top-left)
        colorPaint.setColor(Color.rgb(220, 20, 20));
        Path path1 = new Path();
        path1.moveTo(cx, cy);
        path1.lineTo(cx - size * 0.7f, cy - size * 0.7f);
        path1.lineTo(cx, cy - size * 0.7f);
        path1.close();
        canvas.drawPath(path1, colorPaint);
        
        // Blue quadrant (top-right)
        colorPaint.setColor(Color.rgb(20, 100, 220));
        Path path2 = new Path();
        path2.moveTo(cx, cy);
        path2.lineTo(cx, cy - size * 0.7f);
        path2.lineTo(cx + size * 0.7f, cy - size * 0.7f);
        path2.close();
        canvas.drawPath(path2, colorPaint);
        
        // Green quadrant (bottom-left)
        colorPaint.setColor(Color.rgb(20, 180, 20));
        Path path3 = new Path();
        path3.moveTo(cx, cy);
        path3.lineTo(cx - size * 0.7f, cy + size * 0.7f);
        path3.lineTo(cx - size * 0.7f, cy);
        path3.close();
        canvas.drawPath(path3, colorPaint);
        
        // Yellow quadrant (bottom-right)
        colorPaint.setColor(Color.rgb(255, 200, 0));
        Path path4 = new Path();
        path4.moveTo(cx, cy);
        path4.lineTo(cx + size * 0.7f, cy);
        path4.lineTo(cx + size * 0.7f, cy + size * 0.7f);
        path4.close();
        canvas.drawPath(path4, colorPaint);
        
        // Draw white circle in center
        whitePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, size * 0.3f, whitePaint);
    }

    private void drawWildDrawFourIcon(Canvas canvas, float cx, float cy, float size) {
        // Draw wild icon (smaller)
        canvas.save();
        canvas.translate(0, -size * 0.3f);
        drawWildIcon(canvas, cx, cy, size * 0.6f);
        canvas.restore();
        
        // Draw "+4" below
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.rgb(50, 50, 50));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(size * 0.8f);
        textPaint.setFakeBoldText(true);
        canvas.drawText("+4", cx, cy + size * 0.7f, textPaint);
    }
}
