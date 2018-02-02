package com.demoproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.demoproject.database.DB_Handler;
import com.demoproject.pojo.Category;
import com.demoproject.pojo.Product;
import com.demoproject.pojo.ProductRank;
import com.demoproject.pojo.Ranking;
import com.demoproject.pojo.ResponseJSON;
import com.demoproject.pojo.Variant;
import com.demoproject.utils.Util;
import com.demoproject.webservice.RetrofitBuilder;
import com.demoproject.webservice.RetrofitInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDBService extends IntentService {
    DB_Handler db_handler;
    Intent intent;

    public SyncDBService(String name) {
        super(name);
    }

    public SyncDBService() {
        super("SyncDBService");
    }

    protected void onHandleIntent(@Nullable Intent intent) {
        this.db_handler = new DB_Handler(this);
        fetchData();
        this.intent = intent;
    }

    private void fetchData() {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder(this);
        ((RetrofitInterface) retrofitBuilder.retrofitBuilder(retrofitBuilder.setClient()).create(RetrofitInterface.class)).fetchData().enqueue(new Callback<ResponseJSON>() {
            public void onResponse(Call<ResponseJSON> call, Response<ResponseJSON> response) {
                try {
                    if (response.body() != null) {
                        SyncDBService.this.processData((ResponseJSON) response.body());
                    }
                } catch (Exception e) {
                    SyncDBService.this.reply(SyncDBService.this.getResources().getString(R.string.Error500));
                }
            }

            public void onFailure(Call<ResponseJSON> call, Throwable t) {
                SyncDBService.this.reply(Util.getErrorMessage(t, SyncDBService.this));
            }
        });
    }

    private void processData(ResponseJSON responseJSON) {
        try {
            int i;
            int j;
            List<Category> categoryList = responseJSON.getCategories();
            for (i = 0; i < categoryList.size(); i++) {
                int CategoryID = ((Category) responseJSON.getCategories().get(i)).getId().intValue();
                this.db_handler.insertCategories(CategoryID, ((Category) responseJSON.getCategories().get(i)).getName());
                List<Product> productList = ((Category) responseJSON.getCategories().get(i)).getProducts();
                for (j = 0; j < productList.size(); j++) {
                    int ProductID = ((Product) productList.get(j)).getId().intValue();
                    this.db_handler.insertProducts(ProductID, CategoryID, ((Product) productList.get(j)).getName(), ((Product) productList.get(j)).getDateAdded(), ((Product) productList.get(j)).getTax().getName(), ((Product) productList.get(j)).getTax().getValue());
                    List<Variant> variantList = ((Product) productList.get(j)).getVariants();
                    for (int p = 0; p < variantList.size(); p++) {
                        int VariantID = ((Variant) variantList.get(p)).getId().intValue();
                        String Size = null;
                        String Color = ((Variant) variantList.get(p)).getColor();
                        String Price = String.valueOf(((Variant) variantList.get(p)).getPrice());
                        try {
                            Size = ((Variant) variantList.get(p)).getSize().toString();
                        } catch (NullPointerException e) {
                        }
                        this.db_handler.insertVariants(VariantID, Size, Color, Price, ProductID);
                    }
                }
                List<Integer> childCategories = ((Category) categoryList.get(i)).getChildCategories();
                for (int k = 0; k < childCategories.size(); k++) {
                    this.db_handler.insertChildCategoryMapping(CategoryID, ((Integer) childCategories.get(k)).intValue());
                }
            }
            List<Ranking> rankingList = responseJSON.getRankings();
            for (i = 0; i < rankingList.size(); i++) {
                List<ProductRank> productRankList = ((Ranking) rankingList.get(i)).getProducts();
                for (j = 0; j < productRankList.size(); j++) {
                    try {
                        int id = ((ProductRank) productRankList.get(j)).getId().intValue();
                        switch (i) {
                            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_gravity /*0*/:
                                this.db_handler.updateCounts(DB_Handler.VIEW_COUNT, ((ProductRank) productRankList.get(j)).getViewCount().intValue(), id);
                                break;
                            case org.apmem.tools.layouts.R.styleable.FlowLayout_android_orientation /*1*/:
                                this.db_handler.updateCounts(DB_Handler.ORDER_COUNT, ((ProductRank) productRankList.get(j)).getOrderCount().intValue(), id);
                                break;
                            case org.apmem.tools.layouts.R.styleable.FlowLayout_debugDraw /*2*/:
                                this.db_handler.updateCounts(DB_Handler.SHARE_COUNT, ((ProductRank) productRankList.get(j)).getShares().intValue(), id);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            reply("success");
            Log.i("DB Sync", "success");
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void reply(String value) {
        Bundle bundle = this.intent.getExtras();
        bundle.putString("message", value);
        if (bundle != null) {
            Messenger messenger = (Messenger) bundle.get("messenger");
            Message msg = Message.obtain();
            msg.setData(bundle);
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                Log.i("error", "error");
            }
        }
    }
}
