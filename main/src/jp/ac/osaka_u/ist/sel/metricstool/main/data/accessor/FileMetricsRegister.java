package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;


/**
 * ���̃C���^�[�t�F�[�X�́C�t�@�C�����g���N�X��o�^���邽�߂̃��\�b�h�Q��񋟂���D
 * 
 * @author y-higo
 *
 */
public interface FileMetricsRegister {

    /**
     * �������̃t�@�C���̃��g���N�X�l�i�������j��o�^����
     * 
     * @param fileInfo ���g���N�X�̌v���Ώۃt�@�C��
     * @param value ���g���N�X�l
     * @throws �o�^���悤�Ƃ��Ă��郁�g���N�X�����ɓo�^����Ă���ꍇ�ɃX���[�����
     */
    void registMetric(FileInfo fileInfo, int value) throws MetricAlreadyRegisteredException;
}