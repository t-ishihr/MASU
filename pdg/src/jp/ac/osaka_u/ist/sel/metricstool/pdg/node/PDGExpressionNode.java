package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGExpressionNode;


/**
 * ExpressionInfoまたはConditionInfoを表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGExpressionNode extends PDGNormalNode<CFGExpressionNode> {

    PDGExpressionNode(final CFGExpressionNode node) {
        super(node);
    }
}
