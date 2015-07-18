车牌离线识别 sdk 说明
==================


准备工作
-------

* jdk 1.6+
* 将 `callso-min.jar` 进入项目


接口调用
-------

* 先通过 `Detector.getInstance(android.context.Context ctx, android.view.WindowManager manager, String encoding)` 获得 `Detector` 实例
* 调用 `Detector` 实例的 `String detect(String path)` 方法传入识别图片路径进行识别，识别结果以字符串的形式返回，如 `闽D07Y73,蓝色`

> 可通过 `ImageDisposer.savePicToSdcard(android.graphics.Bitmap bitmap, String path, String fileName)` 将图片保存至 sd 卡


调用样例
-------

```
String sdPath = Environment.getExternalStorageDirectory().getPath();
String testFile = ImageDisposer.savePicToSdcard(BitmapFactory.decodeResource(getResources(), R.raw.test),
                                                sdPath + "/test", 
                                                "test2.jpg");
String result = Detector.getInstance(this, this.getWindowManager(), "gbk")
                        .detect(testFile);
Log.d("Demo", "Detector result is: " + result);
```


拍摄图片要求
----------

要求车牌的像素宽度为 `70~300` 之间，识别率比较理想。故针对手机抓拍的大于 `200W` 的图片，最好给放缩到 `200W` 像素左右，再去识别；或者在应用的时候，在图像上显示一个宽度约为 `200` 左右的框，拍摄的时候，使车牌宽度在框的附近或者之内即可。倾斜角度大约支持正负 `15°`左右，车牌越水平，识别率越高

> `Detector` 会对传入的图像做预处理，但因实际情况的复杂性，还是建议先按照上述要求自行处理图片