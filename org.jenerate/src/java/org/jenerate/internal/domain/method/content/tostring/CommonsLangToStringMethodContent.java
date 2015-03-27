package org.jenerate.internal.domain.method.content.tostring;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.domain.method.content.MethodContentLibraries;
import org.jenerate.internal.domain.method.skeleton.impl.ToStringMethodSkeleton;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.manage.PreferencesManager;

public class CommonsLangToStringMethodContent extends AbstractMethodContent<ToStringMethodSkeleton, ToStringGenerationData> {

    public CommonsLangToStringMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
    }

    @Override
    public String getMethodContent(IType objectClass, ToStringGenerationData data) throws JavaModelException {
        boolean cacheToString = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.CACHE_TOSTRING)).booleanValue();
        boolean isCacheable = cacheToString
                && generatorsCommonMethodsDelegate.areAllFinalFields(data.getCheckedFields());
        String cachingField = "";
        if (isCacheable) {
            cachingField = (String) preferencesManager
                    .getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD);
        }
        IJavaElement currentPosition = data.getElementPosition();
        IField field = objectClass.getField(cachingField);
        if (field.exists()) {
            field.delete(true, null);
        }
        if (isCacheable) {
            String fieldSrc = "private transient String " + cachingField + ";\n\n";
            objectClass.createField(fieldSrc, currentPosition, true, null);
        }
        return MethodGenerations.generateToStringMethodContent(data, cachingField);
    }

    @Override
    public Set<String> getLibrariesToImport(ToStringGenerationData data) {
        boolean useCommonsLang3 = false;
        if (MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        Set<String> libraries = new HashSet<String>();
        String toStringBuilderLibrary = MethodContentLibraries.getToStringBuilderLibrary(useCommonsLang3);
        libraries.add(toStringBuilderLibrary);
        if (!ToStringStyle.NO_STYLE.equals(data.getToStringStyle())) {
            String styleLibrary = ToStringStyle.getToStringStyleLibrary(useCommonsLang3);
            libraries.add(styleLibrary);
        }
        return libraries;
    }

    @Override
    public Class<ToStringMethodSkeleton> getRelatedMethodSkeletonClass() {
        return ToStringMethodSkeleton.class;
    }
}