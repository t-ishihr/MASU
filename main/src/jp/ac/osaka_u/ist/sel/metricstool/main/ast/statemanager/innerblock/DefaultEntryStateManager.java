package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class DefaultEntryStateManager extends InnerBlockStateManager {

    @Override
    protected boolean isDefinitionToken(AstToken token) {
        return token.isDefault();
    }

}