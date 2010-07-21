package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;


/**
 * throw����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode<ThrowStatementInfo> {

    /**
     * �m�[�h�𐶐�����throw����^���ď�����
     * 
     * @param throwStatement
     */
    CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
        super(throwStatement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ThrowStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getThrownExpression();

        // assert�̔��蕔�����ϐ��g�p�̎��łȂ��ꍇ�͕������s��
        if (!CFGUtility.isDissolved(expression)) {
            return null;
        }

        // �Â��m�[�h���폜
        nodeFactory.removeNode(statement);
        this.remove();

        // ����O�̕�����K�v�ȏ����擾
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        this.makeDissolvedNode(expression, nodeFactory, dissolvedNodeList,
                dissolvedVariableUsageList);
        final ThrowStatementInfo newThrowStatement = this.makeNewElement(ownerSpace,
                dissolvedVariableUsageList.getFirst());
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newThrowStatement);
        dissolvedNodeList.add(newNode);

        // ���������m�[�h���G�b�W�łȂ�
        this.makeEdges(dissolvedNodeList);

        // ownerSpace�Ƃ̒���
        this.replace(dissolvedNodeList);

        // ���������m�[�h�Q����CFG���\�z
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        final ThrowStatementInfo statement = this.getCore();
        return statement.getThrownExpression();
    }

    @Override
    ThrowStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final ThrowStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final ThrowStatementInfo newStatement = new ThrowStatementInfo(ownerSpace,
                requiredExpression[0], fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}