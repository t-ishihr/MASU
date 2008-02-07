package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SimpleBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSimpleBlockInfo;

public class SimpleBlockBuilder extends InnerBlockBuilder {

    public SimpleBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new SimpleBlockStateManager();
    }
    
    @Override
    protected UnresolvedSimpleBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedSimpleBlockInfo();
    }

}