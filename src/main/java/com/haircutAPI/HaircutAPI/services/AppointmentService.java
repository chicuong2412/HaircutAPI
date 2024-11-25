package com.haircutAPI.HaircutAPI.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    @SuppressWarnings("unchecked")
    public APIresponse<AppointmentResponse> createAppointment(AppointmentCreationRequest rq,
            Authentication authentication) {
        if (!servicesUtils.checkAuthoritesHasRole((Collection<GrantedAuthority>) authentication.getAuthorities(),
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findCustomerIDByUsername(authentication.getName()).equals(rq.getIdCustomer())) {
                throw new AccessDeniedException("Access Denied");
            }
        }

        if (!checkValidInfomationRq(rq))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        Appointment appointment = new Appointment();

        appointment = appointmentMapper.toAppointment(rq);

        appointmentRepository.save(appointment);

        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointmentDetails = appointmentMapper.toAppointmentDetails(rq);

        appointmentDetails.setIdService(servicesUtils.toServiceEntitiesSet(rq.getIdService()));
        appointmentDetails.setIdCombo(servicesUtils.toComboEnitiesSet(rq.getIdCombo()));

        appointmentDetails.setId(appointment.getId());
        appointmentDetailsRepository.save(appointmentDetails);

        rp.setResult(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetails));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<List<AppointmentResponse>> getAllAppointments() {
        var list = appointmentRepository.findAll();

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(list);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @SuppressWarnings("unchecked")
    public APIresponse<AppointmentResponse> getAppointment(String id, Authentication authentication) {
        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetail = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        var authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {

            if (!servicesUtils.checkAuthoritesHasRole(authorities,
                    "SCOPE_MANAGER")) {
                if (!servicesUtils.findCustomerIDByUsername(authentication.getName())
                        .equals(appointment.getIdCustomer())) {
                    throw new AccessDeniedException("Access Denied");
                }
            } else {
                if (!servicesUtils.getWorkerIdLocation(authentication.getName()).equals(appointment.getIdLocation())) {
                    throw new AccessDeniedException("Access Denied");
                }
            }

        }

        var appointmentRp = appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetail);

        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(appointmentRp);

        return rp;
    }

    @SuppressWarnings("unchecked")
    public APIresponse<AppointmentResponse> updateAppointment(AppointmentUpdationRequest rq,
            Authentication authentication) {

        var authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {

            if (!servicesUtils.checkAuthoritesHasRole(authorities,
                    "SCOPE_MANAGER")) {
                if (!servicesUtils.findCustomerIDByUsername(authentication.getName()).equals(rq.getIdCustomer())) {
                    throw new AccessDeniedException("Access Denied");
                }
            } else {
                if (!servicesUtils.getWorkerIdLocation(authentication.getName()).equals(rq.getIdLocation())) {
                    throw new AccessDeniedException("Access Denied");
                }
            }

        }

        if (!checkValidInfomationUpdate(rq))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        String id = rq.getId();
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetails = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        appointmentMapper.updateAppointment(appointment, rq);
        appointmentMapper.updateAppointment(appointmentDetails, rq);

        appointmentDetails.setIdService(servicesUtils.toServiceEntitiesSet(rq.getIdService()));
        appointmentDetails.setIdCombo(servicesUtils.toComboEnitiesSet(rq.getIdCombo()));

        //Save entities
        appointmentDetailsRepository.save(appointmentDetails);
        appointmentRepository.save(appointment);

        APIresponse<AppointmentResponse> apIresponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        apIresponse.setResult(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetails));

        return apIresponse;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<String> deleteAppointment(String id) {
        if (!appointmentRepository.existsById(id))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        appointmentRepository.deleteById(id);
        appointmentDetailsRepository.deleteById(id);

        APIresponse<String> apIresponse = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return apIresponse;
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public APIresponse<List<AppointmentResponse>> getAppointmentByIdLocation(String idLocation,
            Authentication authentication) {

        var authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findWorkerByUsername(authentication.getName()).getIdLocation()
                    .equals(idLocation)) {
                System.out.println(authentication.getName());
                throw new AccessDeniedException("Access Denied");
            }

        }

        var listAppointments = appointmentRepository.findAllByIdLocation(idLocation);

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(listAppointments);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or #username == #currentUsername")
    public APIresponse<List<AppointmentResponse>> getAppointmentByCustomerUsername(String username,
            String currentUsername) {
        var list = appointmentRepository.findByIdCustomer(servicesUtils.findCustomerIDByUsername(username));

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(list);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public APIresponse<List<AppointmentResponse>> getAppointmentByIdWorker(Authentication authentication,
            String idWorker) {

        var authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findWorkerByUsername(authentication.getName()).getIdLocation()
                    .equals(servicesUtils.findWorkerById(idWorker).getIdLocation())) {
                throw new AccessDeniedException("Access Denied");
            }

        }

        var listAppointments = appointmentRepository.findAllByIdWorker(idWorker);

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(listAppointments);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    private List<AppointmentResponse> findAppointmentDetailsByListAppointment(List<Appointment> listAppointments) {
        var listAppointmentResponses = new ArrayList<AppointmentResponse>();
        for (Appointment appointment : listAppointments) {
            AppointmentDetails appointmentDetail = appointmentDetailsRepository.findById(appointment.getId())
                    .orElseThrow();
            listAppointmentResponses
                    .add(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetail));
        }
        return listAppointmentResponses;
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

        if (!servicesUtils.isLocationIdExisted(rq.getIdLocation()))
            return false;

        return true;
    }

    private boolean checkValidInfomationUpdate(AppointmentUpdationRequest rq) {
        if (rq.getDateTime().isBefore(LocalDateTime.now()))
            return false;

        if (!servicesUtils.isCustomerIdExisted(rq.getIdCustomer()))
            return false;

        if (!servicesUtils.isWorkerIdExisted(rq.getIdWorker()))
            return false;

        if (!servicesUtils.isLocationIdExisted(rq.getIdLocation()))
            return false;

        return true;
    }

}
