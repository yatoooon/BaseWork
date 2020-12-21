package ${presenterPackageName}

import android.app.Application

import ${baselibraryName}.integration.AppManager
<#if needActivity && needFragment>
import ${baselibraryName}.di.scope.ActivityScope
<#elseif needActivity>
import ${baselibraryName}.di.scope.ActivityScope
<#elseif needFragment>
import ${baselibraryName}.di.scope.FragmentScope
</#if>
import ${baselibraryName}.mvp.BasePresenter
import ${baselibraryName}.http.imageloader.ImageLoader
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

import ${contractPackageName}.${pageName}Contract

<#import "root://activities/MVPArmsTemplate/globals.xml.ftl" as gb>

<@gb.fileHeader />
<#if needActivity && needFragment>
@ActivityScope
<#elseif needActivity>
@ActivityScope
<#elseif needFragment>
@FragmentScope
</#if>
class ${pageName}Presenter
@Inject
constructor(model: ${pageName}Contract.Model, rootView: ${pageName}Contract.View) :
BasePresenter<${pageName}Contract.Model, ${pageName}Contract.View>(model,rootView) {
    @Inject
    lateinit var mErrorHandler:RxErrorHandler
    @Inject
    lateinit var mApplication:Application
    @Inject
    lateinit var mImageLoader:ImageLoader
    @Inject
    lateinit var mAppManager:AppManager


    override fun onDestroy() {
          super.onDestroy();
    }
}
