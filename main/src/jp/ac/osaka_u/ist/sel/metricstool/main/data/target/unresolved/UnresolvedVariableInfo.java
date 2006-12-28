package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Resolved;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved�ȕϐ��̋��ʂȐe�N���X.
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * <li>�ʒu���</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public abstract class UnresolvedVariableInfo implements PositionSetting, Unresolved {

    /**
     * �ϐ�����Ԃ�
     * 
     * @return �ϐ���
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �ϐ������Z�b�g����
     * 
     * @param name �ϐ���
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * �ϐ��̌^��Ԃ�
     * 
     * @return �ϐ��̌^
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * �ϐ��̌^���Z�b�g����
     * 
     * @param type �ϐ��̌^
     */
    public final void setType(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public void addModifiar(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * �J�n�s���Z�b�g����
     * 
     * @param fromLine �J�n�s
     */
    public void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * �J�n����Z�b�g����
     * 
     * @param fromColumn �J�n��
     */
    public void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * �I���s���Z�b�g����
     * 
     * @param toLine �I���s
     */
    public void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * �I������Z�b�g����
     * 
     * @param toColumn �I����
     */
    public void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
    }

    /**
     * �J�n�s��Ԃ�
     * 
     * @return �J�n�s
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * �J�n���Ԃ�
     * 
     * @return �J�n��
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * �I���s��Ԃ�
     * 
     * @return �I���s
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * �I�����Ԃ�
     * 
     * @return �I����
     */
    public int getToColumn() {
        return this.toColumn;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    public Resolved getResolvedInfo() {
        return this.resolvedInfo;
    }

    /**
     * ���O�������ꂽ�����Z�b�g����
     * 
     * @param resolvedInfo ���O�������ꂽ���
     */
    public void setResolvedInfo(final Resolved resolvedInfo) {

        if (null == resolvedInfo) {
            throw new NullPointerException();
        }

        if (!(resolvedInfo instanceof VariableInfo)) {
            throw new IllegalArgumentException();
        }

        this.resolvedInfo = resolvedInfo;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �ϐ��̌^
     */
    UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D
     */
    UnresolvedVariableInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.type = null;
        this.modifiers = new HashSet<ModifierInfo>();

        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;

        this.resolvedInfo = null;
    }

    /**
     * �ϐ�����\���ϐ�
     */
    private String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private UnresolvedTypeInfo type;

    /**
     * ���̃t�B�[���h�̏C���q��ۑ����邽�߂̕ϐ�
     */
    private Set<ModifierInfo> modifiers;

    /**
     * �J�n�s��ۑ����邽�߂̕ϐ�
     */
    private int fromLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int fromColumn;

    /**
     * �I���s��ۑ����邽�߂̕ϐ�
     */
    private int toLine;

    /**
     * �J�n���ۑ����邽�߂̕ϐ�
     */
    private int toColumn;

    /**
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    private Resolved resolvedInfo;
}