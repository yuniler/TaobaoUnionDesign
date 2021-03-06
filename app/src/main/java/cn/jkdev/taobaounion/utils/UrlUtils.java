package cn.jkdev.taobaounion.utils;

public class UrlUtils {
    //分类ID和page  的数据
    public static String createHoemPagerUrl(int materialId,int page){
        return "discovery/" + materialId + "/" + page;
    }

    public static String getCoverPath(String getPict_url) {
        return "https:" + getPict_url;
    }
}
