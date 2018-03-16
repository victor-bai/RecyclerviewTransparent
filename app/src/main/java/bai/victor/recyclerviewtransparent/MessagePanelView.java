package bai.victor.recyclerviewtransparent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor-Bai on 2018/3/16.
 */

public class MessagePanelView extends RecyclerView {
    public MessagePanelView(Context context) {
        this(context, null);
    }

    public MessagePanelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessagePanelView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private Context mContext;
    LinearLayoutManager mLayoutManager;
    AudienceAdapter mAdapter;
    List<Bean> mList;
    int itemViewWidth;

    private void init(Context context) {
        mContext = context;
        // 每个用户 item 的宽度
        itemViewWidth = MainActivity.dip2px(mContext, 40);
        // 第三个参数控制Recyclerview逆向显示数据(从下到上)
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        setLayoutManager(mLayoutManager);
        // 添加透明度过渡效果
        doTopGradualEffect();

        mList = new ArrayList<>();
        mAdapter = new AudienceAdapter(mList);
        setAdapter(mAdapter);
    }

    public void refreshData(List<Bean> list){
        if(mAdapter!=null){
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    // 实现渐变效果
    Paint mPaint;
    private int layerId;
    private LinearGradient linearGradient;
    public void doTopGradualEffect(){
        mPaint = new Paint();
        // dst_in 模式，实现底层透明度随上层透明度进行同步显示（即上层为透明时，下层就透明，并不是上层覆盖下层)
        final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mPaint.setXfermode(xfermode);
        // 透明位置不变，位于Recyclerview偏上位置
        linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 100.0f, new int[]{0, Color.BLACK}, null, Shader.TileMode.CLAMP);

        addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(canvas, parent, state);

                mPaint.setXfermode(xfermode);
                mPaint.setShader(linearGradient);
                canvas.drawRect(0.0f, 0.0f, parent.getRight(), 200.0f, mPaint);
                mPaint.setXfermode(null);
                canvas.restoreToCount(layerId);
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                // 此处 Paint的参数这里传的null， 在传入 mPaint 时会出现第一次打开黑屏闪现的问题
                // 注意 saveLayer 不能省也不能移动到onDrawOver方法里
                layerId = c.saveLayer(0.0f, 0.0f, (float) parent.getWidth(), (float) parent.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                // 该方法作用自行百度
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
    }

    class AudienceAdapter extends RecyclerView.Adapter<AudienceHolder>{

        List<Bean> list;

        public AudienceAdapter(List<Bean> list){
            this.list = list;
        }

        @Override
        public AudienceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.message_panel_view, parent, false);
            return new AudienceHolder(view);
        }

        @Override
        public void onBindViewHolder(AudienceHolder holder, int position) {
            holder.bindData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<Bean> list){
            this.list = list;
        }
    }

    class AudienceHolder extends ViewHolder{

        public AudienceHolder(View itemView) {
            super(itemView);
        }

        public void bindData(Bean bean){

        }
    }

}
