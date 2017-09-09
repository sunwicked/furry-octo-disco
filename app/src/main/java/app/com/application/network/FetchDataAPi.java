package app.com.application.network;

import java.util.ArrayList;
import java.util.List;

import app.com.application.main.DataModel;
import app.com.application.storage.Storage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 09/09/17.
 */

public class FetchDataAPi extends DataApi {

    public static void getList(final NetworkCallback networkCallback, final int requestType) {

        final Call<List<DataModel>> data = DataApi.getApi().getData();

        data.enqueue(new Callback<List<DataModel>>() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                final List<DataModel> body = response.body();
                if (null != body) {
                    networkCallback.onResponse(body, requestType);

                } else {
                    networkCallback.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                networkCallback.onFailure(t);
            }
        });


    }
}
