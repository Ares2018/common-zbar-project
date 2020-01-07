## 本地图片使用方法

    /**
     * 识别本地二维码
     *
     * @param path 本地图片地址
     * @return
     */
     
     
String content = QRUtils.getInstance().decodeQRCode(path);

jni 编译来源 https://github.com/bertsir/zBarLibary