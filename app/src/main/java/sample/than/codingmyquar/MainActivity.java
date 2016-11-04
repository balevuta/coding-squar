package sample.than.codingmyquar;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListItemAdapter mListItemAdapter;

    private List<ImageDownloader> mImageDownloaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponants();
        initMockData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onDownloadEvent(EventClickObject pEventClickObject) {
        String url = pEventClickObject.getUrlClicked();
        int index = pEventClickObject.getIndex();
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        ImageDownloader lImageDownloader = new ImageDownloader(this, url, index);
        lImageDownloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mImageDownloaders.add(lImageDownloader);
    }

    @Subscribe
    public void onResumeDownload(EventResumeDownload pEventResumeDownload) {
        if(mImageDownloaders!=null && mImageDownloaders.size()>0) {
            for (int i = 0; i < mImageDownloaders.size(); i++) {
                if (mImageDownloaders.get(i).getDownloadStatus().equals(DownloadStatus.STATE_DOWNLOADING)) {
                    ImageDownloader lImageDownloader = new ImageDownloader(this, mImageDownloaders.get(i).getFileUrl(), mImageDownloaders.get(i).getFileIndex());
                    lImageDownloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }
    }

    private void initComponants() {
        mImageDownloaders = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycleview);
        mListItemAdapter = new ListItemAdapter();
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListItemAdapter);
    }

    private void initMockData() {
        ItemModel lItemModel1 = new ItemModel("File 1", "http://www.johnharveyphoto.com/HongKong/FishInLargeTankLg.jpg");
        ItemModel lItemModel2 = new ItemModel("File 2", "http://www.johnharveyphoto.com/HongKong/FishInLargeTankLg.jpg");
        ItemModel lItemModel3 = new ItemModel("File 3", "http://www.johnharveyphoto.com/HongKong/FishInLargeTankLg.jpg");

        List<ItemModel> lstLItemModels = new ArrayList<>();
        lstLItemModels.add(lItemModel1);
        lstLItemModels.add(lItemModel2);
        lstLItemModels.add(lItemModel3);
        mListItemAdapter.setList(lstLItemModels);
    }
}
