package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


/**
 * @author kou-tngt, t-miyake
 *
 */
public class FieldOrMethodElement extends IdentifierElement {

    public FieldOrMethodElement(UnresolvedExpressionInfo<? extends ExpressionInfo> ownerUsage,
            String name, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(name, fromLine, fromColumn, toLine, toColumn);

        this.ownerUsage = ownerUsage;
    }

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return null;
    }

    @Override
    public String[] getQualifiedName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> resolveAsVariable(
            final BuildDataManager buildDataManager, final boolean reference,
            final boolean assignment) {
        UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(buildDataManager
                .getAllAvaliableNames(), this.ownerUsage, this.name, reference, assignment,
                this.fromLine, this.fromColumn, this.toLine, this.toColumn);
        buildDataManager.addVariableUsage(fieldUsage);

        this.usage = fieldUsage;

        return fieldUsage;
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        return this;
    }

    @Override
    public UnresolvedExpressionInfo<ExpressionInfo> resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager) {
        throw new UnsupportedOperationException();
    }

}
