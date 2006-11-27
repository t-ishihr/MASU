package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �N���X���g���N�X���Ǘ�����N���X�D
 * 
 * @author y-higo
 * 
 */
public final class ClassMetricsInfoManager implements Iterable<ClassMetricsInfo> {

    /**
     * ���̃N���X�̃C���X�^���X��Ԃ��D�V���O���g���p�^�[����p���Ă���D
     * 
     * @return ���̃N���X�̃C���X�^���X
     */
    public static ClassMetricsInfoManager getInstance() {
        return CLASS_METRICS_MAP;
    }

    /**
     * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���̃C�e���[�^
     */
    public Iterator<ClassMetricsInfo> iterator() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        Collection<ClassMetricsInfo> unmodifiableClassMetricsInfoCollection = Collections
                .unmodifiableCollection(this.classMetricsInfos.values());
        return unmodifiableClassMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ�N���X�̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ�N���X�̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param classInfo �ق������g���N�X���̃N���X
     * @return ���g���N�X���
     */
    public ClassMetricsInfo get(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        return this.classMetricsInfos.get(classInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param classInfo ���g���N�X�v���Ώۂ̃N���X�I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final ClassInfo classInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        ClassMetricsInfo classMetricsInfo = this.classMetricsInfos.get(classInfo);

        // �ΏۃN���X�� classMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == classMetricsInfo) {
            classMetricsInfo = new ClassMetricsInfo(classInfo);
            this.classMetricsInfos.put(classInfo, classMetricsInfo);
        }

        classMetricsInfo.putMetric(plugin, value);
    }

    /**
     * �N���X���g���N�X�ɓo�^�R�ꂪ�Ȃ������`�F�b�N����
     * 
     * @throws MetricNotRegisteredException �o�^�R�ꂪ�������ꍇ�ɃX���[�����
     */
    public void checkMetrics() throws MetricNotRegisteredException {

        MetricsToolSecurityManager.getInstance().checkAccess();
        
        for (ClassInfo classInfo : ClassInfoManager.getInstance()) {

            ClassMetricsInfo classMetricsInfo = this.get(classInfo);
            if (null == classMetricsInfo) {
                throw new MetricNotRegisteredException("Class \"" + classInfo.getName()
                        + "\" metrics are not registered!");
            }
            classMetricsInfo.checkMetrics();
        }
    }

    /**
     * �N���X���g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D �V���O���g���p�^�[����p���Ă��邽�߁Cprivate �����Ă���D
     * 
     */
    private ClassMetricsInfoManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.classMetricsInfos = Collections
                .synchronizedSortedMap(new TreeMap<ClassInfo, ClassMetricsInfo>());
    }

    /**
     * ���̃N���X�̃I�u�W�F�N�g���Ǘ����Ă���萔�D�V���O���g���p�^�[����p���Ă���D
     */
    private static final ClassMetricsInfoManager CLASS_METRICS_MAP = new ClassMetricsInfoManager();

    /**
     * �N���X���g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
     */
    private final SortedMap<ClassInfo, ClassMetricsInfo> classMetricsInfos;
}