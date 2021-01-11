package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

class MedicalServiceTest {
    // given:
    SendAlertService sendAlertServiceMock = Mockito.mock(SendAlertServiceImpl.class);
    PatientInfoRepository patientInfoRepositoryMock = Mockito.mock(PatientInfoRepository.class);
    MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, sendAlertServiceMock);
    PatientInfo patientInfo = new PatientInfo("id","Test", "Testov", LocalDate.of(1111, 1, 1),
            new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78)));

    @Test
    void check_blood_pressure_test(){
        // given:
        BloodPressure currentBloodPressure = new BloodPressure(150, 100);

        // when:
        Mockito.when(patientInfoRepositoryMock.getById("id"))
                .thenReturn(patientInfo);
        medicalService.checkBloodPressure("id", currentBloodPressure);
        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        // expect:
        Mockito.verify(sendAlertServiceMock, Mockito.times(1))
                .send(message);
    }

    @Test
    void check_temperature_test(){
        // given:
        BigDecimal currentTemperature = new BigDecimal("34");

        // when:
        Mockito.when(patientInfoRepositoryMock.getById("id"))
                .thenReturn(patientInfo);
        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        medicalService.checkTemperature("id", currentTemperature);

        // expect:
        Mockito.verify(sendAlertServiceMock, Mockito.times(1))
                .send(message);

    }

    @Test
    void healthy_patient_test(){
        // given:
        BigDecimal currentTemperature = new BigDecimal("36");
        BloodPressure currentBloodPressure = new BloodPressure(125, 78);

        // when:
        Mockito.when(patientInfoRepositoryMock.getById("id"))
                .thenReturn(patientInfo);
        medicalService.checkTemperature("id", currentTemperature);
        medicalService.checkBloodPressure("id", currentBloodPressure);

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());
        Mockito.verify(sendAlertServiceMock, Mockito.times(0))
                .send(message);
    }
}