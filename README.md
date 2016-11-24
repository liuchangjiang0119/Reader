# Reader
	ShanBay Reader for NEC4

## 已实现
* 用python进行数据格式转换，txt转为json
* 按Unit进行文章列表分页
* 加入FloatActionsMenu，做到单词高亮、取消高亮、显示答案和显示单词表
* 文章内容的显示及单词按等级高亮显示
* 文章中的单词单词点击高亮
* 用Rxjava+Retrofit调用扇贝api，进行高亮单词的中文翻译
* 加入页面切换、转场、弹窗动画

## ScreenShot
![开始页面](http://p1.bpimg.com/567571/0538352cae31be73.jpg)
![页面选择](http://p1.bpimg.com/567571/2170ac231d9fb8ec.jpg)
![主界面](http://p1.bpimg.com/567571/8cd0d5e3ae014e81.jpg)
![文章页面](http://p1.bpimg.com/567571/d30a877e2af39888.jpg)
![菜单](http://p1.bqimg.com/567571/f4dbfd9280226d75.jpg)
![单词等级高亮](http://p1.bqimg.com/567571/0d427f65ede392b3.jpg)
![点击高亮](http://p1.bqimg.com/567571/686943e02a0638ca.jpg)
![单词表](http://p1.bqimg.com/567571/8f66f757ba8e8399.jpg)

## Problem
  可以实现单词按等级高亮和单词点击高亮，也可以通过重写TextView做到文章两端对齐，但当两者结合的时候文字可以对齐，但是做不到文字高亮。自己思考了一下，因为文字高亮用的是SpannableString是按照字符索引位置进行setSpan设置样式，自定义TextView实现两端对齐改变了字符的索引位置，所以暂时还做不到两者结合。

![文字对齐](http://p1.bpimg.com/567571/c9b7e4500a379d04.jpg)
