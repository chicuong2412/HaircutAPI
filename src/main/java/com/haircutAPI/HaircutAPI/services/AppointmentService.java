package com.haircutAPI.HaircutAPI.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;
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
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AppointmentDetailsRepository appointmentDetailsRepository;
    @Autowired
    AppointmentMapper appointmentMapper;
    @Autowired
    ServicesUtils servicesUtils;

    public APIresponse<AppointmentResponse> createAppointment(AppointmentCreationRequest rq) {
        if (!checkValidInfomationRq(rq))
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

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(list);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    public APIresponse<AppointmentResponse> getAppointment(String id) {
        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetail = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));

        var appointmentRp = appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetail);

        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(appointmentRp);

        return rp;
    }

    public APIresponse<String> updateAppointment(AppointmentUpdationRequest rq) {

        if (!checkValidInfomationUpdate(rq))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

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

    public APIresponse<String> deleteAppointment(String id) {
        if (!appointmentRepository.existsById(id))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        appointmentRepository.deleteById(id);
        appointmentDetailsRepository.deleteById(id);

        APIresponse<String> apIresponse = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return apIresponse;

    }

    public APIresponse<List<AppointmentResponse>> getAppointmentByCustomerID(String id) {
        var list = appointmentRepository.findByIdCustomer(id);

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(list);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    private List<AppointmentResponse> findAppointmentDetailsByListAppointment(List<Appointment> listAppointments) {
        var list = new ArrayList<AppointmentResponse>();
        for (Appointment appointment : listAppointments) {
            AppointmentDetails appointmentDetail = appointmentDetailsRepository.findById(appointment.getId())
                    .orElseThrow();
            list.add(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetail));
        }
        return list;
    }

    private boolean checkValidInfomationRq(AppointmentCreationRequest rq) {
        if (rq.getDateTime().isBefore(LocalDateTime.now()))
            return false;

        if (AppointmentStatus.WAITING.compareTo(rq.getStatus()) != 0)
            return false;

        if (!servicesUtils.isCustomerIdExisted(rq.getIdCustomer()))
            return false;

        if (!servicesUtils.isWorkerIdExisted(rq.getIdWorker()))
            return false;

        return true;
    }

    private boolean checkValidInfomationUpdate(AppointmentUpdationRequest rq) {
        if (rq.getDateTime().isBefore(LocalDateTime.now()))
            return false;

        if (AppointmentStatus.WAITING.compareTo(rq.getStatus()) != 0)
            return false;

        if (!servicesUtils.isCustomerIdExisted(rq.getIdCustomer()))
            return false;

        if (!servicesUtils.isWorkerIdExisted(rq.getIdWorker()))
            return false;

        return true;
    }

}
