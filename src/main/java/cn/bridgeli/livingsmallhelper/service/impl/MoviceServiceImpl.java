package cn.bridgeli.livingsmallhelper.service.impl;

import cn.bridgeli.livingsmallhelper.movie.entity.Movie;
import cn.bridgeli.livingsmallhelper.movie.entity.MovieInfo;
import cn.bridgeli.livingsmallhelper.service.KeyService;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

import com.google.gson.Gson;

public class MoviceServiceImpl implements KeyService {

    @Override
    public String excute(String substring) {
        String requestUrl = "http://api.map.baidu.com/telematics/v3/movie?qt=hot_movie&location=北京&output=json&ak=Nbx4Q9PtGhrRgkGdlvbgLaZm";
        String jsonObject = UrlUtil.httpRequest(requestUrl);
        StringBuffer movies = new StringBuffer();
        Gson gson = new Gson();
        MovieInfo movieInfo = gson.fromJson(jsonObject, MovieInfo.class);
        movieInfo.getDate();
        for (Movie movie : movieInfo.getResult().getMovie()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("影片名称： " + movie.getMovie_name() + "\n");
            stringBuffer.append("影片类型： " + movie.getMovie_type() + "\n");
            stringBuffer.append("影片上映时间： " + movie.getMovie_release_date() + "\n");
            stringBuffer.append("影片所属国家： " + movie.getMovie_nation() + "\n");
            stringBuffer.append("影片演员： " + movie.getMovie_starring() + "\n");
            stringBuffer.append("影片时长： " + movie.getMovie_length() + "\n");
            stringBuffer.append("影片评分： " + movie.getMovie_score() + "\n");
            stringBuffer.append("影片导演： " + movie.getMovie_director() + "\n");
            stringBuffer.append("影片所属类型： " + movie.getMovie_tags() + "\n");
            stringBuffer.append("影片概要信息： " + movie.getMovie_message() + "\n");

            movies.append(stringBuffer);
        }
        return movies.toString();
    }

    @Override
    public String getUsage() {
        return WeiXinUtil.getMoviceUsage();
    }

}
