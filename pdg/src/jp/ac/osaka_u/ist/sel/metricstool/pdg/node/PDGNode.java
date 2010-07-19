package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGCaseEntryNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGCaughtExceptionNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGEmptyNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGExpressionNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGForeachControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGReturnStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGCallDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGReturnDependenceEdge;


/**
 * PDGを構成するノードを表すクラス
 * 
 * @author t-miyake, higo
 * 
 * @param <T>
 *            ノードの核となる情報の型
 */
public abstract class PDGNode<T extends CFGNode<? extends ExecutableElementInfo>> implements
        Comparable<PDGNode<?>> {

    /**
     * CFGノードからPDGノードを生成するメソッド
     * 
     * @param cfgNode
     * @return
     */
    public static PDGNode<?> generate(final CFGNode<?> cfgNode) {

        if (cfgNode instanceof CFGControlNode) {

            if (cfgNode instanceof CFGForeachControlNode) {
                return new PDGForeachControlNode((CFGForeachControlNode) cfgNode);
            }

            else {
                return new PDGControlNode((CFGControlNode) cfgNode);
            }

        }

        else if (cfgNode instanceof CFGNormalNode<?>) {

            if (cfgNode instanceof CFGCaseEntryNode) {
                return new PDGCaseEntryNode((CFGCaseEntryNode) cfgNode);
            }

            else if (cfgNode instanceof CFGCaughtExceptionNode) {
                return new PDGCaughtExceptionNode((CFGCaughtExceptionNode) cfgNode);
            }

            else if (cfgNode instanceof CFGEmptyNode) {
                return new PDGEmptyNode((CFGEmptyNode) cfgNode);
            }

            else if (cfgNode instanceof CFGExpressionNode) {
                return new PDGExpressionNode((CFGExpressionNode) cfgNode);
            }

            else if (cfgNode instanceof CFGStatementNode<?>) {

                if (cfgNode instanceof CFGReturnStatementNode) {
                    return new PDGReturnStatementNode((CFGReturnStatementNode) cfgNode);
                }

                else {
                    return new PDGStatementNode<CFGStatementNode<?>>((CFGStatementNode<?>) cfgNode);
                }
            }

            else {
                throw new IllegalStateException();
            }

        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * フォワードエッジ（このノードからの依存辺）
     */
    private final SortedSet<PDGEdge> forwardEdges;

    /**
     * バックワードエッジ（このノードへの依存辺）
     */
    private final SortedSet<PDGEdge> backwardEdges;

    /**
     * ノードの核となる情報
     */
    protected final T cfgNode;

    protected final String text;

    /**
     * ノードの核となる情報を与えて初期化
     * 
     * @param core
     *            ノードの核となる情報
     */
    protected PDGNode(final T node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        this.cfgNode = node;
        this.text = node.getCore().getText() + " <" + node.getCore().getFromLine() + ">";
        this.forwardEdges = new TreeSet<PDGEdge>();
        this.backwardEdges = new TreeSet<PDGEdge>();
    }

    /**
     * このノードにて，変更または定義される変数のSet
     * 
     * @return
     */
    public abstract SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * このノードにて，参照されている変数のSet
     * 
     * @return
     */
    public abstract SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables();

    /**
     * 引数で与えられた変数がこのノードで定義されているかどうかを返す
     * 
     * @param variable
     * @return
     */
    public final boolean isDefine(final VariableInfo<? extends UnitInfo> variable) {
        return this.getDefinedVariables().contains(variable);
    }

    /**
     * 引数で与えられた変数がこのノードで参照されているかを返す
     * 
     * @param variable
     * @return
     */
    public final boolean isReferenace(final VariableInfo<? extends UnitInfo> variable) {
        return this.getReferencedVariables().contains(variable);
    }

    /**
     * このノードのフォワードエッジを追加
     * 
     * @param forwardEdge
     *            このノードのフォワードエッジ
     */
    public final boolean addForwardEdge(final PDGEdge forwardEdge) {
        if (null == forwardEdge) {
            throw new IllegalArgumentException("forwardNode is null.");
        }

        if (!forwardEdge.getFromNode().equals(this)) {
            throw new IllegalArgumentException();
        }

        return this.forwardEdges.add(forwardEdge);
    }

    /**
     * このノードのバックワードエッジを追加
     * 
     * @param backwardEdge
     */
    public final boolean addBackwardEdge(final PDGEdge backwardEdge) {
        if (null == backwardEdge) {
            throw new IllegalArgumentException("backwardEdge is null.");
        }

        if (!(backwardEdge.getToNode().equals(this))) {
            throw new IllegalArgumentException();
        }

        return this.backwardEdges.add(backwardEdge);
    }

    final public void removeBackwardEdge(final PDGEdge backwardEdge) {
        this.backwardEdges.remove(backwardEdge);
    }

    final public void removeForwardEdge(final PDGEdge forwardEdge) {
        this.forwardEdges.remove(forwardEdge);
    }

    /**
     * このノードからのデータ依存辺を追加
     * 
     * @param dependingNode
     */
    public boolean addDataDependingNode(final PDGNode<?> dependingNode, final VariableInfo<?> data) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGDataDependenceEdge dataEdge = new PDGDataDependenceEdge(this, dependingNode, data);
        boolean added = this.addForwardEdge(dataEdge);
        added &= dependingNode.addBackwardEdge(dataEdge);
        return added;
    }

    public boolean addExecutionDependingNode(final PDGNode<?> dependingNode) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGExecutionDependenceEdge executionEdge = new PDGExecutionDependenceEdge(this,
                dependingNode);
        boolean added = this.addForwardEdge(executionEdge);
        added &= dependingNode.addBackwardEdge(executionEdge);
        return added;
    }

    public boolean addCallDependingNode(final PDGNode<?> dependingNode, final CallInfo<?> call) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGCallDependenceEdge callEdge = new PDGCallDependenceEdge(this, dependingNode, call);
        boolean added = this.addForwardEdge(callEdge);
        added &= dependingNode.addBackwardEdge(callEdge);
        return added;
    }

    public boolean addReturnDependingNode(final PDGNode<?> dependingNode) {

        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        final PDGReturnDependenceEdge returnEdge = new PDGReturnDependenceEdge(this, dependingNode);
        boolean added = this.addForwardEdge(returnEdge);
        added &= dependingNode.addBackwardEdge(returnEdge);
        return added;
    }

    /**
     * このノードのバックワードエッジを取得
     * 
     * @return このノードのバックワードエッジ
     */
    public final SortedSet<PDGEdge> getBackwardEdges() {
        return Collections.unmodifiableSortedSet(this.backwardEdges);
    }

    /**
     * このノードのフォワードエッジを取得
     * 
     * @return このノードのフォワードエッジ
     */
    public final SortedSet<PDGEdge> getForwardEdges() {
        return Collections.unmodifiableSortedSet(this.forwardEdges);
    }

    /**
     * このノード内において呼び出されているメソッドがオブジェクトの内容を変えているかどうかを返す
     * 
     * @return
     */
    public final boolean isStateChanged() {

        for (final CallInfo<?> call : this.getCore().getCalls()) {
            if (call instanceof MethodCallInfo) {
                final MethodCallInfo methodCall = (MethodCallInfo) call;
                if (CFGUtility.stateChange(methodCall.getCallee())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int compareTo(final PDGNode<?> node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        final int methodOrder = this.getCore().getOwnerMethod().compareTo(
                node.getCore().getOwnerMethod());
        if (0 != methodOrder) {
            return methodOrder;
        }

        return this.getCore().compareTo(node.getCore());
    }

    /**
     * このノードの元になったプログラム要素
     * 
     * @return このノードの元になったプログラム要素
     */
    public final ExecutableElementInfo getCore() {
        return this.getCFGNode().getCore();
    }

    /**
     * このPDGノードの元となったCFGノード
     * @return
     */
    public final T getCFGNode() {
        return this.cfgNode;
    }

    public final String getText() {
        return this.text;
    }
}
