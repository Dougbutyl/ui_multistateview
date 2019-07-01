# ui_multistateview  
![image](https://github.com/Dougbutyl/ui_multistateview/blob/master/screen/p.gif)
#### 内容页、失败页、空白页、加载页状态 

## Dependency
Add it in your root build.gradle at the end of repositories:
``` Java
 	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 and then add dependency
``` Java
 		dependencies {
	      implementation 'com.github.Dougbutyl:ui_multistateview:1.0'
	}


 ```
 ## Usage
 ### Xml
 ```
 <com.neocean.app.library.MultiStateView
        android:id="@+id/ms_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:msv_errorView="@layout/view_error"
        app:msv_emptyView="@layout/view_empty"
        app:msv_viewState="content">
 </com.neocean.app.library.MultiStateView>
```
```
kotlin
  ms_view.viewState = MultiStateView.VIEW_STATE_ERROR //指定状态页
 java
   ms_view.setViewState(MultiStateView.VIEW_STATE_ERROR)
```
  |Attributes|default value|Description|
|:---|:---|:---|
|app:msv_errorView|@layout/view_error|失败（错误）页|
|app:msv_emptyView|@layout/view_empty|空白页|
|app:msv_loadingView|@layout/view_loading|加载页|
|app:msv_viewState||指定状态页|
