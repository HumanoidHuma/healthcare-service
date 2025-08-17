import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    //* Проверка вывода сообщения во время проверка давления
    @Test
    void test_sendMessage_whenCheckBloodPressure() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(
                        new PatientInfo("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
                );

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalService.checkBloodPressure("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 4ab4bfb3-3d5f-4391-8aec-3651720d8b42, need help", argumentCaptor.getValue());
    }

    //* Проверка вывода сообщения во врем проверки температуры
    @Test
    void test_sendMessage_whenCheckTemperature() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(
                        new PatientInfo("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
                );

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BigDecimal currentTemperature = new BigDecimal("35.0");
        medicalService.checkTemperature("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", currentTemperature);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 4ab4bfb3-3d5f-4391-8aec-3651720d8b42, need help", argumentCaptor.getValue());
    }

    //* Проверка, что сообщения не выводятся, когда показатель давления в норме
    @Test
    void test_NotSendMessage_whenCheckBlood() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(
                        new PatientInfo("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
                );

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", currentPressure);

        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.anyString());
    }

    //* Проверка, что сообщения не выводятся, когда показатель температуры в норме
    @Test
    void test_NotSendMessage_whenCheckTemperature() {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(
                        new PatientInfo("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)))
                );

        SendAlertService sendAlertService = Mockito.mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        BigDecimal currentTemperature = new BigDecimal("37.9");
        medicalService.checkTemperature("4ab4bfb3-3d5f-4391-8aec-3651720d8b42", currentTemperature);

        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.anyString());
    }
}
