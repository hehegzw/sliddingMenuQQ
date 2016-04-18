package cn.gzw.sliddingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by gzw on 2015/12/11.
 */
public class SliddingMenuView extends HorizontalScrollView {
    private int screenWidth;//屏幕宽度
    private int menuWidth;//菜单宽度
    private int mainWidth;//主界面宽度
    private boolean isFirst;//是否是第一次启动
    private ViewGroup menu;//菜单view
    private ViewGroup main;//主界面view
    private float startMoveX;//开始滑动坐标
    private float endMoveX;//停止滑动坐标
    private int reservedWidth;//显示菜单时，右边预留空间
    private boolean isScal;//是否缩放
    private boolean isAlpha;//是否透明度变化
    private boolean isSlid;//是否处于侧滑状态
    public SliddingMenuView(Context context) {
        this(context,null);
    }

    public SliddingMenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SliddingMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHorizontalScrollBarEnabled(false);
        initSize(context);
    }
    public void openMenu(){
        smoothScrollTo(-menuWidth,0);
        isSlid = true;
    }
    public void closeMenu(){
        smoothScrollTo(menuWidth,0);
        isSlid = false;
    }
    /**
     * 显示菜单时，右边预留空间
     * @param reservedWidth 预留空间
     */
    public void setReservedWidth(int reservedWidth) {
        this.reservedWidth = reservedWidth;
        menuWidth = screenWidth -reservedWidth;
    }

    public void setScal(boolean scal) {
        isScal = scal;
    }

    public void setAlpha(boolean alpha) {
        isAlpha = alpha;
    }

    private void initSize(Context context){
        reservedWidth = 300;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        menuWidth = screenWidth -reservedWidth;
        mainWidth = screenWidth;
        isFirst = true;
        startMoveX = endMoveX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isFirst){
            LinearLayout ll = (LinearLayout) getChildAt(0);
            menu = (ViewGroup) ll.getChildAt(0);
            main = (ViewGroup) ll.getChildAt(1);
            menu.getLayoutParams().width = menuWidth;
            main.getLayoutParams().width = mainWidth;
            isFirst = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            scrollTo(menuWidth,0);//启动时先滚动到菜单消失
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startMoveX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                endMoveX = ev.getX();
                float dis = endMoveX - startMoveX;
                Log.d("scrollDis",dis+"");
                if(dis >= screenWidth/2){
                    smoothScrollTo(-menuWidth,0);
                    isSlid = true;
                }else {
                    float temp = Math.abs(dis);
                    if(isSlid){//如果menu为显示状态
                        if(temp<5){//此时默认是点击
                            if(startMoveX>menuWidth){
                                smoothScrollTo(menuWidth, 0);
                                isSlid = false;
                            }else{
                                return true;
                            }
                        }else{
                            if(temp > screenWidth/2){
                                smoothScrollTo(menuWidth, 0);
                                isSlid = false;
                            }else{
                                smoothScrollTo(-menuWidth,0);
                            }
                        }
                    }else{
                        smoothScrollTo(menuWidth,0);
                        isSlid = false;
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }
    //l为x轴移动的距离，当l==oldl时说明滑动停止，否则正在滑动，t则代表y
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.d("onScrollChanged","l->"+l+"-----"+"oldl->"+oldl+"t->"+t+"oldt->"+oldt);
        anima(l);
    }
    private void anima(int l){
        float scale = l * 1.0f / menuWidth;
        float menuScale = (float) (1 - 0.3*scale);
        float mainScale = (float) (0.7+ 0.3*scale);
        if(isAlpha){
            ViewHelper.setAlpha(menu, menuScale);
        }
        if(isScal){
            ViewHelper.setScaleX(menu,menuScale);
            ViewHelper.setScaleY(menu, menuScale);
            ViewHelper.setScaleX(main, mainScale);
            ViewHelper.setScaleY(main, mainScale);
        }
        ViewHelper.setTranslationX(menu,menuWidth*scale);
    }
}
