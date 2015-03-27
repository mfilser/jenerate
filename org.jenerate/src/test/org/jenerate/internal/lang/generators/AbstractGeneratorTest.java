package org.jenerate.internal.lang.generators;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.generate.method.util.JavaCodeFormatter;
import org.jenerate.internal.generate.method.util.JavaUiCodeAppender;
import org.jenerate.internal.manage.PreferencesManager;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Abstract test for the different method generators
 * 
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractGeneratorTest {

    public static final String FORMATTED_CODE_1 = "formatted_code_1";
    public static final String FORMATTED_CODE_2 = "formatted_code_2";

    @Mock
    protected PreferencesManager preferencesManager;
    @Mock
    protected JavaUiCodeAppender javaUiCodeAppender;
    @Mock
    protected JavaCodeFormatter jeneratePluginCodeFormatter;
    @Mock
    protected GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate;

    @Mock
    protected Shell parentShell;
    @Mock
    protected ICompilationUnit compilationUnit;
    @Mock
    protected IType objectClass;

    @Mock
    protected IMethod createdMethod1;
    @Mock
    protected IJavaElement elementPosition;
    @Mock
    private IField field1;
    @Mock
    private IField field2;

    protected IField[] fields = new IField[] { field1, field2 };

    @Before
    public void SetUp() throws Exception {
        mockCommonPreferencesManager();
        mockCommonObjectClass();
        mockCommonGeneratorsCommonMethodsDelegate();
        mockJeneratePluginCodeFormatter();
        callbackAfterSetUp();
    }

    protected void mockCommonPreferencesManager() {
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.ADD_OVERRIDE_ANNOTATION))
                .thenReturn(false);
        when(preferencesManager.getCurrentPreferenceValue(JeneratePreference.USE_COMMONS_LANG3)).thenReturn(false);
    }

    private void mockCommonObjectClass() throws CoreException {
        when(field1.getElementName()).thenReturn("field1");
        when(field2.getElementName()).thenReturn("field2");
        when(field1.getTypeSignature()).thenReturn(Signature.SIG_BOOLEAN);
        fields = new IField[] { field1, field2 };
        when(objectClass.getFields()).thenReturn(fields);
        when(objectClass.getCompilationUnit()).thenReturn(compilationUnit);
    }

    private void mockCommonGeneratorsCommonMethodsDelegate() throws JavaModelException {
        when(generatorsCommonMethodsDelegate.getObjectClassFields(objectClass, preferencesManager)).thenReturn(fields);
        when(generatorsCommonMethodsDelegate.areAllFinalFields(fields)).thenReturn(false);
        when(generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)).thenReturn(false);
    }

    private void mockJeneratePluginCodeFormatter() throws Exception {
        when(jeneratePluginCodeFormatter.formatCode(any(IType.class), anyString())).thenReturn(FORMATTED_CODE_1,
                FORMATTED_CODE_2);
    }

    /**
     * Callback after the Junit Before annotated setUp method
     */
    public abstract void callbackAfterSetUp() throws Exception;

}
