package com.haircutAPI.HaircutAPI.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.enity.Appointment;
import com.haircutAPI.HaircutAPI.enity.AppointmentDetails;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.AppointmentMapper;
import com.haircutAPI.HaircutAPI.repositories.AppointmentDetailsRepository;
import com.haircutAPI.HaircutAPI.repositories.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AppointmentDetailsRepository appointmentDetailsRepository;
    @Autowired
    AppointmentMapper appointmentMapper;

    public APIresponse<AppointmentResponse> createAppointment(AppointmentCreationRequest rq) {

        if (!rq.getDateTime().isAfter(LocalDateTime.now()))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        Appointment appointment = new Appointment();

        appointment = appointmentMapper.toAppointment(rq);

        appointmentRepository.save(appointment);

        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        rp.setResult(appointmentMapper.toAppointmentResponse(appointment));

        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointmentDetails = appointmentMapper.toAppointmentDetails(rq);

        appointmentDetails.setId(appointment.getId());
        appointmentDetailsRepository.save(appointmentDetails);

        rp.getResult().setIdService(appointmentDetails.getIdService());
        rp.getResult().setIdCombo(appointmentDetails.getIdCombo());

        return rp;
    }

    public APIresponse<List<AppointmentResponse>> getAllAppointments() {
        var list = appointmentRepository.findAll();
        var listDetails = appointmentDetailsRepository.findAll();

        List<AppointmentResponse> result = appointmentMapper.toAppointmentResponses(list);
        appointmentMapper.updateAppointmetResponses(result, listDetails);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    public APIresponse<AppointmentResponse> getAppointment(String id) {
        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));

        var appointmentRp = appointmentMapper.toAppointmentResponse(appointment);

        appointmentMapper.updateAppointmetResponse(appointmentRp, appointmentDetails);

        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(appointmentRp);


        return rp;
    }

    public APIresponse<String> updateAppointment(AppointmentUpdationRequest rq) {
        
        String id = rq.getId();
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        appointmentMapper.updateAppointment(appointment, rq);
        appointmentMapper.updateAppointment(appointmentDetails, rq);
        appointmentDetailsRepository.save(appointmentDetails);
        appointmentRepository.save(appointment);

        APIresponse<String> apIresponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());

        return apIresponse;
    }

    public void deleteAppointment(String id) {
        if (!appointmentRepository.existsById(id))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        appointmentRepository.deleteById(id);
        appointmentDetailsRepository.deleteById(id);

    }

    public boolean checkVerifiedAppointmentUpdationRequest(AppointmentUpdationRequest rq) {
        if (rq.getId() == null) return false;
        if (rq.getDateTime() == null) return false;
        if (rq.getIdCombo() == null) return false;
        if (rq.getIdCustomer() == null) return false;

        return true;
    }

}
