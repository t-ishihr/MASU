package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGCaughtExceptionNode;


/**
 * catch節の式の部分を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGCaughtExceptionNode extends PDGNormalNode<CFGCaughtExceptionNode> {

    PDGCaughtExceptionNode(final CFGCaughtExceptionNode node) {
        super(node);
    }
}
