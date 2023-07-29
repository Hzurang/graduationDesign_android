package com.example.english.config;

import com.example.english.R;

public class ConstantData {
    // 提示句子集合
    // 数据存放目录（外置存储卡）
    public static final String DIR_TOTAL = "english";
    // 解压后的数据目录
    public static final String DIR_AFTER_FINISH = "json";

    // 书默认ID
    public static final int CET4 = 1;
    public static final int CET6 = 2;
    public static final int TEM4 = 3;
    public static final int TEM8 = 4;
    public static final int KAOYAN = 5;
    public static final int GRE = 6;
    public static final int TOEFL = 7;
    public static final int IELTS = 8;

    // 背景图API
    public static final String IMG_API = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=7&n=1";
    public static final String IMG_API_BEFORE = "https://www.bing.com";

    // 每日一句API
    public static final String DAILY_SENTENCE_API = "https://open.iciba.com/dsapi/";

    // 有道英音发音
    public static final String YOU_DAO_VOICE_EN = "https://dict.youdao.com/dictvoice?type=1&audio=";

    // 有道美音发音
    public static final String YOU_DAO_VOICE_USA = "https://dict.youdao.com/dictvoice?type=0&audio=";

    // raw资源正确提示音
    public static final int RIGHT_SIGN = R.raw.right;
    // raw资源错误提示音
    public static final int WRONG_SIGN = R.raw.wrong;

    // 通知渠道ID
    public static final String channelId = "default";
    public static final String channelId2 = "default2";
    // 通知渠道名称
    public static final String channelName = "默认通知";
    public static final String channelName2 = "默认通知2";

    // 提示句子集合
    public static final String[] phrases = {
            "马行软地易失蹄，人贪安逸易失志",
            "每天告诉自己一次：我真的很不错",
            "没有热忱，世间便无进步",
            "有志者，事竟成，破釜沉舟，百二秦关终属楚",
            "有心人，天不负，卧薪尝胆，三千越甲可吞吴",
            "风尘三尺剑，社稷一戎衣",
            "只要站起来的次数比倒下去的次数多，那就是成功",
            "收拾一下心情，开始下一个新的开始",
            "你配不上自己的野心，也辜负了曾经历的苦难",
            "现实很近又很冷，梦想很远却很温暖",
            "前方无绝路，希望在转角",
            "没有人会让我输，除非我不想赢",
            "追踪着鹿的猎人是看不见山的",
            "有志始知蓬莱近，无为总觉咫尺远",
            "业精于勤而荒于嬉，行成于思而毁于随",
            "没有所谓失败，除非你不再尝试"};

    // 根据书ID获取该书的单词总量
    public static int wordTotalNumberById(int bookId) {
        int num = 0;
        switch (bookId) {
            case CET4:
                num = 5833;
                break;
            case CET6:
                num = 8029;
                break;
            case TEM4:
                num = 8846;
                break;
            case TEM8:
                num = 3978;
                break;
            case KAOYAN:
                num = 5467;
                break;
            case GRE:
                num = 7485;
                break;
            case TOEFL:
                num = 4867;
                break;
            case IELTS:
                num = 4523;
                break;
        }
        return num;
    }

    // 根据书ID获取该书的书名
    public static String bookNameById(int bookId) {
        String name = "";
        switch (bookId) {
            case CET4:
                name = "英语四级词汇";
                break;
            case CET6:
                name = "英语六级词汇";
                break;
            case TEM4:
                name = "英专四级词汇";
                break;
            case TEM8:
                name = "英专八级词汇";
                break;
            case KAOYAN:
                name = "考研英语词汇";
                break;
            case GRE:
                name = "GRE词汇";
                break;
            case TOEFL:
                name = "英语托福词汇";
                break;
            case IELTS:
                name = "英语雅思词汇";
                break;
        }
        return name;
    }

    // 根据书ID获取该书的类型
    public static String typeById(int bookId) {
        String name = "";
        switch (bookId) {
            case CET4:
                name = "四级词汇";
                break;
            case CET6:
                name = "六级词汇";
                break;
            case TEM4:
                name = "英专四级";
                break;
            case TEM8:
                name = "英专八级";
                break;
            case KAOYAN:
                name = "考研词汇";
                break;
            case GRE:
                name = "GRE词汇";
                break;
            case TOEFL:
                name = "托福词汇";
                break;
            case IELTS:
                name = "雅思词汇";
                break;
        }
        return name;
    }

    // 根据书ID获取该书的图片
    public static String bookPicById(int bookId) {
        String picAddress = "";
        switch (bookId) {
            case CET4:
                picAddress = "https://images-cn.ssl-images-amazon.cn/images/I/41qVe2F4UnL.jpg";
                break;
            case CET6:
                picAddress = "https://img14.360buyimg.com/pop/jfs/t1/59150/26/7205/170864/5d512c1cEcb05b1f2/6521e6f2ab96a7db.png";
                break;
            case TEM4:
                picAddress = "https://img2.baidu.com/it/u=1397410865,2925461761&fm=253&fmt=auto&app=138&f=JPEG?w=480&h=480";
                break;
            case TEM8:
                picAddress = "http://t14.baidu.com/it/u=962636177,494785306&fm=224&app=112&f=JPEG?w=500&h=500";
                break;
            case KAOYAN:
                picAddress = "https://img1.baidu.com/it/u=2529312588,3986958872&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
                break;
            case GRE:
                picAddress = "https://img1.baidu.com/it/u=516111037,330425857&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
                break;
            case TOEFL:
                picAddress = "https://5b0988e595225.cdn.sohucs.com/images/20181016/75ed6ff48b3d43699a2635d639da07a2.jpeg";
                break;
            case IELTS:
                picAddress = "https://img0.baidu.com/it/u=3560139109,2255236058&fm=253&fmt=auto&app=138&f=JPEG?w=417&h=500";
                break;
        }
        return picAddress;
    }

//    // 根据书ID获取该书的下载地址
//    public static String bookDownLoadAddressById(int bookId) {
//        String picAddress = "";
//        switch (bookId) {
//            case CET4_WORDBOOK:
//                picAddress = "http://ydschool-online.nos.netease.com/1523620217431_CET4luan_1.zip";
//                break;
//            case CET6_WORDBOOK:
//                picAddress = "http://ydschool-online.nos.netease.com/1521164660466_CET6luan_1.zip";
//                break;
//            case KAOYAN_WORDBOOK:
//                picAddress = "http://ydschool-online.nos.netease.com/1521164661106_KaoYanluan_1.zip";
//                break;
//            case CET6ALL:
//                picAddress = "http://ydschool-online.nos.netease.com/1524052554766_CET6_2.zip";
//                break;
//            case KAOYANALL:
//                picAddress = "http://ydschool-online.nos.netease.com/1521164654696_KaoYan_2.zip";
//                break;
//        }
//        return picAddress;
//    }
//
//    // 根据书ID获取该书的下载后的文件名
//    public static String bookFileNameById(int bookId) {
//        String picAddress = "";
//        switch (bookId) {
//            case CET4_WORDBOOK:
//                picAddress = "CET4luan_1.zip";
//                break;
//            case CET6_WORDBOOK:
//                picAddress = "CET6luan_1.zip";
//                break;
//            case KAOYAN_WORDBOOK:
//                picAddress = "KaoYanluan_1.zip";
//                break;
//            case CET6ALL:
//                picAddress = "CET6_2.zip";
//                break;
//            case KAOYANALL:
//                picAddress = "KaoYan_2.zip";
//                break;
//        }
//        return picAddress;
//    }

}
