package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGVariableDeclarationStatementNode;


/**
 * 変数宣言文を表すノード
 * 
 * @author higo
 *
 */
public class PDGVariableDeclarationStatementNode extends
        PDGStatementNode<CFGVariableDeclarationStatementNode> {

    PDGVariableDeclarationStatementNode(final CFGVariableDeclarationStatementNode node) {
        super(node);
    }
}
