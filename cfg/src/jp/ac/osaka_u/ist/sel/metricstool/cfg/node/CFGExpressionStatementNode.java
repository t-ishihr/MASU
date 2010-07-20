package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGStatementNode<ExpressionStatementInfo> {

    /**
     * 生成するノードに対応する文を与えて初期化
     * 
     * @param statement
     *            生成するノードに対応する文
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        final ExpressionStatementInfo statement = this.getCore();
        return statement.getExpression();
    }

    @Override
    ExpressionStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }

        final ExpressionStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(ownerSpace,
                requiredExpression[0], fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}
