package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ꍀ���Z�̓��e��\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedMonominalOperationInfo extends UnresolvedEntityUsageInfo {

    /**
     * ���ƈꍀ���Z�̌��ʂ̌^��^���ď�����
     * 
     * @param term ��
     * @param type �ꍀ���Z�̌��ʂ̌^
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo term,
            final PrimitiveTypeInfo type) {
        this.term = term;
        this.type = type;
    }

    @Override
    boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // �g�p�ʒu���擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedEntityUsageInfo unresolvedTerm = this.getTerm();
        final EntityUsageInfo term = unresolvedTerm.resolveEntityUsage(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final PrimitiveTypeInfo type = this.getResultType();

        this.resolvedInfo = new MonominalOperationInfo(term, type, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * �ꍀ���Z�̍���Ԃ�
     * 
     * @return �ꍀ���Z�̍�
     */
    public UnresolvedEntityUsageInfo getTerm() {
        return this.term;
    }

    /**
     * �ꍀ���Z�̌��ʂ̌^��Ԃ�
     * 
     * @return �ꍀ���Z�̌��ʂ̌^
     */
    public PrimitiveTypeInfo getResultType() {
        return this.type;
    }

    /**
     * �ꍀ���Z�̍�
     */
    private final UnresolvedEntityUsageInfo term;

    /**
     * �ꍀ���Z�̌��ʂ̌^
     */
    private final PrimitiveTypeInfo type;

    private EntityUsageInfo resolvedInfo;
}