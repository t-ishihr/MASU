package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �t�@�C�����g���N�X���Ǘ�����N���X�D
 * 
 * @author y-higo
 * 
 */
public final class FileMetricsInfoManager {

    /**
     * ���̃N���X�̃C���X�^���X��Ԃ��D�V���O���g���p�^�[����p���Ă���D
     * 
     * @return ���̃N���X�̃C���X�^���X
     */
    public static FileMetricsInfoManager getInstance() {
        return FILE_METRICS_MAP;
    }

    /**
     * ���g���N�X���o�^����Ă���N���X�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���o�^����Ă���N���X�̃C�e���[�^
     */
    public Iterator<FileInfo> fileInfoIterator() {
        Set<FileInfo> unmodifiableFileInfoSet = Collections.unmodifiableSet(this.fileMetricsInfos
                .keySet());
        return unmodifiableFileInfoSet.iterator();
    }

    /**
     * ���g���N�X���ꗗ�̃C�e���[�^��Ԃ��D
     * 
     * @return ���g���N�X���̃C�e���[�^
     */
    public Iterator<FileMetricsInfo> fileMetricsInfoIterator() {
        Collection<FileMetricsInfo> unmodifiableFileMetricsInfoCollection = Collections
                .unmodifiableCollection(this.fileMetricsInfos.values());
        return unmodifiableFileMetricsInfoCollection.iterator();
    }

    /**
     * �����Ŏw�肳�ꂽ�t�@�C���̃��g���N�X����Ԃ��D �����Ŏw�肳�ꂽ�t�@�C���̃��g���N�X��񂪑��݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param fileInfo �ق������g���N�X���̃t�@�C��
     * @return ���g���N�X���
     */
    public FileMetricsInfo get(final FileInfo fileInfo) {
        
        if (null == fileInfo) {
            throw new NullPointerException();
        }
        
        return this.fileMetricsInfos.get(fileInfo);
    }

    /**
     * ���g���N�X��o�^����
     * 
     * @param fileInfo ���g���N�X�v���Ώۂ̃t�@�C���I�u�W�F�N�g
     * @param plugin ���g���N�X�̃v���O�C��
     * @param value ���g���N�X�l
     * @throws MetricAlreadyRegisteredException �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���
     */
    public void putMetric(final FileInfo fileInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        FileMetricsInfo fileMetricsInfo = this.fileMetricsInfos.get(fileInfo);

        // �Ώۃt�@�C���� fileMetricsInfo �������ꍇ�́Cnew ���� Map �ɓo�^����
        if (null == fileMetricsInfo) {
            fileMetricsInfo = new FileMetricsInfo();
            this.fileMetricsInfos.put(fileInfo, fileMetricsInfo);
        }

        fileMetricsInfo.putMetric(plugin, value);
    }

    /**
     * �t�@�C�����g���N�X�}�l�[�W���̃I�u�W�F�N�g�𐶐�����D �V���O���g���p�^�[����p���Ă��邽�߁Cprivate �����Ă���D
     * 
     */
    private FileMetricsInfoManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.fileMetricsInfos = Collections
                .synchronizedMap(new TreeMap<FileInfo, FileMetricsInfo>());
    }

    /**
     * ���̃N���X�̃I�u�W�F�N�g���Ǘ����Ă���萔�D�V���O���g���p�^�[����p���Ă���D
     */
    private static final FileMetricsInfoManager FILE_METRICS_MAP = new FileMetricsInfoManager();

    /**
     * �t�@�C�����g���N�X�̃}�b�v��ۑ����邽�߂̕ϐ�
     */
    private final Map<FileInfo, FileMetricsInfo> fileMetricsInfos;
}