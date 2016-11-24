package com.shanbay.reader.model.http;



import com.shanbay.reader.model.bean.Word;
import com.shanbay.reader.model.bean.WordInfo;

import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by windfall on 16-11-24.
 */

public interface ShanBayApi {
    @GET("search/")
    Observable<WordInfo> getWordInfo(@Query("word") String word);
}
