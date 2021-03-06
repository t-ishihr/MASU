package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGExpressionStatementNode;


/**
 * ExpressionStatementInfoを表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGExpressionStatementNode extends PDGStatementNode<CFGExpressionStatementNode> {

    PDGExpressionStatementNode(final CFGExpressionStatementNode node) {
        super(node);
    }
}
