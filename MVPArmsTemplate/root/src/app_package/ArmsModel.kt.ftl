package ${modelPackageName}

import android.app.Application
import com.google.gson.Gson
import ${baselibraryName}.integration.IRepositoryManager
import ${baselibraryName}.mvp.BaseModel

<#if needActivity && needFragment>
import ${baselibraryName}.di.scope.ActivityScope
<#elseif needActivity>
import ${baselibraryName}.di.scope.ActivityScope
<#elseif needFragment>
import ${baselibraryName}.di.scope.FragmentScope
</#if>
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
class ${pageName}Model
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), ${pageName}Contract.Model{
    @Inject
    lateinit var mGson:Gson;
    @Inject
    lateinit var mApplication:Application;

    override fun onDestroy() {
          super.onDestroy();
    }
}
