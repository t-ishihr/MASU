package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * 実行依存辺を表すクラス
 * 
 * @author higo
 * 
 */
public class PDGExecutionDependenceEdge extends PDGEdge {

	/**
	 * エッジの集合から，実行依存を表すエッジのみを抽出し，そのSetを返す
	 * 
	 * @param edges
	 * @return
	 */
	public static SortedSet<PDGExecutionDependenceEdge> getExecutionDependenceEdge(
			final Set<PDGEdge> edges) {
		final SortedSet<PDGExecutionDependenceEdge> executionDependenceEdges = new TreeSet<PDGExecutionDependenceEdge>();
		for (final PDGEdge edge : edges) {
			if (edge instanceof PDGExecutionDependenceEdge) {
				executionDependenceEdges.add((PDGExecutionDependenceEdge) edge);
			}
		}
		return executionDependenceEdges;
	}

	public PDGExecutionDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode) {
		super(fromNode, toNode);
	}

	@Override
	public String getDependenceString() {
		return "";
	}

	@Override
	public String getDependenceTypeString() {
		return "Execution Dependency";
	}
}
