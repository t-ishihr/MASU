package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;


/**
 * PDG上で制御ノードを表すクラス
 * 
 * @author t-miyake
 *
 */
public class PDGControlNode extends PDGNode<CFGControlNode> {

    /**
     * ノードの集合から，制御ノードのみを抽出し，そのSortedSetを返す
     * 
     * @param nodes
     * @return
     */
    public static SortedSet<PDGControlNode> getControlNodes(final Set<? extends PDGNode<?>> nodes) {
        final SortedSet<PDGControlNode> controlNodes = new TreeSet<PDGControlNode>();
        for (final PDGNode<?> node : nodes) {
            if (node instanceof PDGControlNode) {
                controlNodes.add((PDGControlNode) node);
            }
        }
        return Collections.unmodifiableSortedSet(controlNodes);
    }

    /**
     * CFGControlNodeを与えて，初期化
     * 
     * @param controlNode
     */
    protected PDGControlNode(final CFGControlNode controlNode) {
        super(controlNode);
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final SortedSet<VariableInfo<?>> definedVariables = new TreeSet<VariableInfo<?>>();
        definedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getAssignments(this.getCore().getVariableUsages())));
        return definedVariables;
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        final SortedSet<VariableInfo<?>> referencedVariables = new TreeSet<VariableInfo<?>>();
        referencedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getReferencees(this.getCore().getVariableUsages())));
        return referencedVariables;
    }

    /**
     * この制御ノードに制御されるノードを追加
     * @param controlledNode 制御されるノード
     */
    public void addControlDependingNode(final PDGNode<?> controlledNode,
            final boolean trueDependence) {
        if (null == controlledNode) {
            throw new IllegalArgumentException();
        }

        final PDGControlDependenceEdge controlFlow = new PDGControlDependenceEdge(this,
                controlledNode, trueDependence);
        this.addForwardEdge(controlFlow);
        controlledNode.addBackwardEdge(controlFlow);
    }

}
