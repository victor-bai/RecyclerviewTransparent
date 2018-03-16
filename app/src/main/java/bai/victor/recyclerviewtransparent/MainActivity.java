package bai.victor.recyclerviewtransparent;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    // 观众列表（横向Recyclerview)
    private AudienceListView audience;
    private List<Bean> audienceBeanList = new ArrayList<>();
    // 聊天面板（竖向Recyclerview)
    private MessagePanelView messagePanel;
    private List<Bean> messagePanelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        audience = (AudienceListView) findViewById(R.id.audience);
        messagePanel = (MessagePanelView) findViewById(R.id.message_panel);
        getAudiences();
        audience.refreshData(audienceBeanList);
        messagePanel.refreshData(messagePanelList);
    }

    private void getAudiences(){
        for (int i=0; i< 3; i++){
            Bean bean = new Bean();
            audienceBeanList.add(bean);
            messagePanelList.add(bean);
        }
    }

    @Override
    public void onBackPressed() {
        getAudiences();
        audience.refreshData(audienceBeanList);
        messagePanel.refreshData(messagePanelList);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
