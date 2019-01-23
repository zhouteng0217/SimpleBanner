# SimpleBanner
本项目分别使用了RecyclerViev和HorizontalScrollView来实现自动轮播的banner, 用于学习和项目使用

#效果展示
![](https://raw.githubusercontent.com/zhouteng0217/SimpleBanner/master/gif_banner.gif)


#使用方式
直接将源码引入到项目中使用，具体参考demo的实现

# 原理说明

本项目中用了两种方式来实现banner, 分别是RecyclerView和HorizontalScrollView

RecyclerView的方式，有着复用的机制，在数据量较大时，性能上比较有优势。

至于用HorizontalScrollView也来实现了一套同样的功能，是因为一些不需要复用机制的项目场景的需求。

simplebanner模块下:

* ```LoopRecyclerViewAdapter``` 实现了对一个普通的RecyclerView.Adapter的封装，将普通的RecyclerView.Adapter的数据源列表，头尾添加2个数据，新适配器数据源的第0个数据，就是原数据源的最后一个数据；新适配器数据源最后一个数据，就是原数据源的第0个数据。

* ```LoopRecyclerViewPager``` 实现对普通RecyclerView的自定义, 重写触摸事件，当banner的最后一张向右滑动时，切换到第一张；当banner的第一张像左滑动时，切换到最后一张，从而实现循环切换

* ```RecyclerBannerAdapter``` 封装的RecyclerView的adapter基类，具体实现每个banner的界面时，可以继承该adapter来实现

* ```RecyclerViewPointView```  banner下面的指示器，可以修改该类来自定义符合需求的指示器

* ```AbsScrollBannerView``` 使用HorizontalScrollView来实现循环切换的banner基类，通过继承该类可以实现自定义的banner视图

* ```ScrollBannerPointView``` 基于 HorizontalScrollView实现的循环切换的banner类的指示器，可以修复来实现自定义指示器

demo中:

* ```RecyclerBannerDemoAdpater```  RecyclerBanner的具体实现适配器，这里面具体实现banner的界面

* ```RecyclerBannerView``` 将RecylerView实现的banner和指示器封装在一个view中，参考使用

* ```ScrollBannerView``` HorizontalScrollView实现的banner的，具体界面实现类

  
具体使用请查看源码

















