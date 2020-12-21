package ${moudlePackageName};

<#if needActivity && needFragment>
import ${baselibraryName}.di.scope.ActivityScope;
<#elseif needActivity>
import ${baselibraryName}.di.scope.ActivityScope;
<#elseif needFragment>
import ${baselibraryName}.di.scope.FragmentScope;
</#if>

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import ${contractPackageName}.${pageName}Contract;
import ${modelPackageName}.${pageName}Model;

<#import "root://activities/MVPArmsTemplate/globals.xml.ftl" as gb>

<@gb.fileHeader />
@Module
public abstract class ${pageName}Module {

    @Binds
    abstract ${pageName}Contract.Model bind${pageName}Model(${pageName}Model model);
}