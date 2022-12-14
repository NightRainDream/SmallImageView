[![](https://jitpack.io/v/NightRainDream/SmallImageView.svg)](https://jitpack.io/#NightRainDream/SmallImageView)
# SmallImageView

自由角度的ImageView，四个角都可定义不同的角度。

## 1. 配置要求

SmallImageView最低支持Android5.0(API level 21)

## 2. 下载

在`settings.gradle`文件中做如下配置

```
repositories {
	...
	maven { url 'https://jitpack.io' }
}
```

在`app`目录下的`build.gradle`文件中做如下配置

```
dependencies {
	implementation 'com.github.NightRainDream:SmallImageView:Latest_VERSION'
}
```

## 3. 使用

在layout中引入如下View:

```
<com.night.image.SmallImageView
   android:layout_width="100dp"
   android:layout_height="100dp"
   app:customRadius="10dp" />
```

### 3.1 属性

| 属性                    | 说明             | 默认值   |
| ----------------------- |----------------|-------|
| customRadiusTopLeft     | 左上角圆角直径        | 0F    |
| customRadiusTopRight    | 右上角圆角直径        | 0F    |
| customRadiusBottomRight | 右下角圆角直径        | 0F    |
| customRadiusBottomLeft  | 左下角圆角直径        | 0F    |
| customRadius            | 四个角圆角直径(优先级最高) | 0F    |
| customSquare            | 是否强制View高和宽相等  | false |

### 3.2 方法

#### 3.2.1 设置圆角直径(四个角相同)

```
/**
 * 设置圆角直径
 *
 * @param radius 圆角直径
 */
public void setRadius(final float radius)
```

#### 3.2.2 设置圆角直径(分别设置四个角)

```
/**
 * 设置圆角直径
 *
 * @param topLeft     左上角圆角直径
 * @param topRight    右上角圆角直径
 * @param bottomLeft  左下角圆角直径
 * @param bottomRight 右下角圆角直径
 */
public void setRadius(final float topLeft, final float topRight, final float bottomLeft, final float bottomRight)
```
## 4.示例
![示例图片](https://github.com/NightRainDream/SmallImageView/blob/master/example/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87.jpg)

