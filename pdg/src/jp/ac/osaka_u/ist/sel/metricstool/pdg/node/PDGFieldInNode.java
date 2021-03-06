package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGFieldInNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;


public class PDGFieldInNode extends PDGDataNode<CFGFieldInNode> implements PDGDataInNode {

    /**
     * 引数で与えられたfieldからPDGFieldInNodeを作成して返す
     * 
     * @param field
     * @param unit
     * @return
     */
    public static PDGFieldInNode getInstance(final FieldInfo field, final CallableUnitInfo unit) {

        if (null == field || null == unit) {
            throw new IllegalArgumentException();
        }

        final CFGFieldInNode cfgNode = CFGFieldInNode.getInstance(field, unit);
        return new PDGFieldInNode(cfgNode);
    }

    private PDGFieldInNode(final CFGFieldInNode node) {
        super(node);
    }
}
