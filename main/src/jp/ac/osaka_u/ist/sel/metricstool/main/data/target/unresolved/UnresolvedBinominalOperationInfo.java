package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * �������񍀉��Z���i�[���邽�߂̃N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedBinominalOperationInfo implements UnresolvedEntityUsageInfo {

    /**
     * �����Ȃ��R���X�g���N�^
     */
    public UnresolvedBinominalOperationInfo() {
    }

    /**
     * ���Z�q��2�̃I�y�����h��^���ď���������
     * 
     * @param operator ���Z�q
     * @param firstOperand ���i�������j�I�y�����h
     * @param secondOperand ���i�������j�I�y�����h
     */
    public UnresolvedBinominalOperationInfo(final OPERATOR operator,
            final UnresolvedEntityUsageInfo firstOperand,
            final UnresolvedEntityUsageInfo secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.resolvedInfo = null;
    }

    /**
     * ���̖������j�����Z�������ς݂ł��邩�ǂ�����\��
     * 
     * @return �����ς݂ł���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���̖������j�����Z�̉����ς݃G���e�B�e�B��Ԃ�
     * 
     * @return �����ς݃j�����Z
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * �������񍀉��Z���������C���̌^��Ԃ��D
     * 
     * @param usingClass �������񍀉��Z���s���Ă���N���X
     * @param usingMethod �������񍀉��Z���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݓ񍀉��Z�i�܂�C���Z���ʂ̌^�j
     */
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final OPERATOR operator = this.getOperator();
        final UnresolvedEntityUsageInfo unresolvedFirstOperand = this.getFirstOperand();
        final UnresolvedEntityUsageInfo unresolvedSecondOperand = this.getSecondOperand();
        final EntityUsageInfo firstOperand = unresolvedFirstOperand.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final EntityUsageInfo secondOperand = unresolvedSecondOperand.resolveEntityUsage(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo = new BinominalOperationInfo(operator, firstOperand, secondOperand);
        return this.resolvedInfo;
    }

    /**
     * ���Z�q���擾����
     * 
     * @return ���Z�q
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedEntityUsageInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���擾����
     * 
     * @return ���i�������j�I�y�����h
     */
    public UnresolvedEntityUsageInfo getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * ���Z�q���Z�b�g����
     * 
     * @param operator ���Z�q
     */
    public void setOperator(final OPERATOR operator) {

        if (null == operator) {
            throw new NullPointerException();
        }

        this.operator = operator;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param firstOperand ���i�������j�I�y�����h
     */
    public void setFirstOperand(final UnresolvedEntityUsageInfo firstOperand) {

        if (null == firstOperand) {
            throw new NullPointerException();
        }

        this.firstOperand = firstOperand;
    }

    /**
     * ���i�������j�I�y�����h���Z�b�g����
     * 
     * @param secondOperand ���i�������j�I�y�����h
     */
    public void setSecondOperand(final UnresolvedEntityUsageInfo secondOperand) {

        if (null == secondOperand) {
            throw new NullPointerException();
        }

        this.secondOperand = secondOperand;
    }

    private OPERATOR operator;

    private UnresolvedEntityUsageInfo firstOperand;

    private UnresolvedEntityUsageInfo secondOperand;

    private EntityUsageInfo resolvedInfo;
}