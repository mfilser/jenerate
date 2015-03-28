package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.UserActionIdentifier;
import org.jenerate.internal.domain.data.CompareToGenerationData;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.impl.CompareToDialog;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CompareToDialogFactory extends AbstractDialogFactory<CompareToDialog, CompareToGenerationData> {

    private JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CompareToDialogFactory(PreferencesManager preferencesManager,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(preferencesManager);
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public CompareToDialog createDialog(Shell parentShell, IType objectClass, Set<IMethod> excludedMethods)
            throws Exception {
        IField[] fields = getObjectClassFields(objectClass);
        boolean disableAppendSuper = getDisableAppendSuper(objectClass);
        return new CompareToDialog(parentShell, "Generate CompareTo Method", objectClass, fields, excludedMethods,
                disableAppendSuper, preferencesManager);
    }

    @Override
    public UserActionIdentifier getUserActionIdentifier() {
        return UserActionIdentifier.COMPARE_TO;
    }

    private boolean getDisableAppendSuper(IType objectClass) throws JavaModelException {
        return !(javaInterfaceCodeAppender.isImplementedInSupertype(objectClass, "Comparable") && isCompareToImplementedInSuperclass(objectClass));
    }

    public boolean isCompareToImplementedInSuperclass(final IType objectClass) throws JavaModelException {
        return isOverriddenInSuperclass(objectClass, "compareTo", new String[] { "QObject;" }, null);
    }
}
