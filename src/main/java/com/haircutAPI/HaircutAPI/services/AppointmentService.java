package com.haircutAPI.HaircutAPI.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;
import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.enity.Appointment;
import com.haircutAPI.HaircutAPI.enity.AppointmentDetails;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;
import com.haircutAPI.HaircutAPI.enity.Worker;
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

        appointment.setCustomer(servicesUtils.getCustomerByID(rq.getIdCustomer()));
        appointment.setWorker(servicesUtils.findWorkerById(rq.getIdWorker()));

        appointmentRepository.save(appointment);

        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        AppointmentDetails appointmentDetails = new AppointmentDetails();
        appointmentDetails = appointmentMapper.toAppointmentDetails(rq);

        appointmentDetails.setIdService(servicesUtils.toServiceEntitiesSet(rq.getIdService()));
        appointmentDetails.setIdCombo(servicesUtils.toComboEnitiesSet(rq.getIdCombo()));

        appointmentDetails.setId(appointment.getId());
        appointmentDetailsRepository.save(appointmentDetails);

        rp.setResult(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetails,
                appointment.getCustomer(),
                appointment.getWorker()));
        List<String> userIds = new ArrayList<>();
        userIds.add(appointment.getCustomer().getId());
        userIds.add(appointment.getWorker().getId());
        servicesUtils.sendNotificationsToUsers(userIds, "New Appointmet",
                "There is a new appointment for you at: " + appointment.getDateTime().toString());
        return rp;
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public APIresponse<List<AppointmentResponse>> getAllAppointments(Authentication authentication) {
        if (!servicesUtils.checkAuthoritesHasRole((Collection<GrantedAuthority>) authentication.getAuthorities(),
                "SCOPE_ADMIN")) {
            Worker workerManager = servicesUtils.findWorkerByUsername(authentication.getName());
            return getAppointmentByIdLocation(workerManager.getIdLocation(), authentication);
        }
        List<AppointmentResponse> list = appointmentRepository.GetAllReponses();

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(list);
        result.sort((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime()));
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @SuppressWarnings({ "unchecked"})
    public APIresponse<AppointmentResponse> getAppointment(String id, Authentication authentication) {
        APIresponse<AppointmentResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        AppointmentDetails appointmentDetail = appointmentDetailsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {

            if (!servicesUtils.checkAuthoritesHasRole(authorities,
                    "SCOPE_MANAGER")) {
                if (!servicesUtils.findCustomerIDByUsername(authentication.getName())
                        .equals(appointment.getCustomer().getId())) {
                    throw new AccessDeniedException("Access Denied");
                }
            } else {
                if (!servicesUtils.getWorkerIdLocation(authentication.getName()).equals(appointment.getIdLocation())) {
                    throw new AccessDeniedException("Access Denied");
                }
            }

        }

        AppointmentResponse  appointmentRp = appointmentRepository.getAppointmentResponseById(id);
        appointmentRp = findAppointmentDetailsByAppointment(appointmentRp);

        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(appointmentRp);

        return rp;
    }

    @SuppressWarnings({ "unchecked" })
    public APIresponse<AppointmentResponse> updateAppointment(AppointmentUpdationRequest rq,
            Authentication authentication) {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

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

        // Save entities
        appointmentDetailsRepository.save(appointmentDetails);
        appointmentRepository.save(appointment);

        APIresponse<AppointmentResponse> apIresponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        apIresponse.setResult(appointmentMapper.appointmentResponseGenerator(appointment, appointmentDetails,
                appointment.getCustomer(),
                appointment.getWorker()));

        return apIresponse;
    }

    public APIresponse<String> cancelAppointment(String id, Authentication authentication) {
        APIresponse<String> ou = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        ou.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        List<String> userIds = new ArrayList<>();
        userIds.add(appointment.getWorker().getId());
        userIds.add(appointment.getCustomer().getId());
        servicesUtils.sendNotificationsToUsers(userIds, "Appointmemt cancelled",
                "Your appointmet at " +
                        appointment.getDateTime().toString().replace("T", " ") + " has been cancelled");
        appointmentRepository.save(appointment);
        return ou;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<String> deleteAppointment(String id) {
        if (!appointmentRepository.existsById(id))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        appointment.setDeleted(true);
        appointmentRepository.save(appointment);
        APIresponse<String> apIresponse = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        apIresponse.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return apIresponse;
    }

    @SuppressWarnings({ "unchecked"})
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public APIresponse<List<AppointmentResponse>> getAppointmentByIdLocation(String idLocation,
            Authentication authentication) {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findWorkerByUsername(authentication.getName()).getIdLocation()
                    .equals(idLocation)) {
                System.out.println(authentication.getName());
                throw new AccessDeniedException("Access Denied");
            }

        }

        List<AppointmentResponse> listAppointments = appointmentRepository.GetReponsesByIdLocation(idLocation);

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(listAppointments);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or #username == #currentUsername")
    public APIresponse<List<AppointmentResponse>> getAppointmentByCustomerUsername(String username,
            String currentUsername) {
        // var list = appointmentRepository.getByIdCustomer(servicesUtils.findCustomerIDByUsername(username));
        List<AppointmentResponse> listAppointments = appointmentRepository.GetReponsesByIdCustomer(servicesUtils.findCustomerIDByUsername(username));
        System.out.println("List Appointments: " + listAppointments.size());
        
        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(listAppointments);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    @SuppressWarnings("unchecked")
    public APIresponse<List<AppointmentResponse>> getAppointmentByIdWorker(Authentication authentication,
            String idWorker) {

        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();

        if (!servicesUtils.checkAuthoritesHasRole(authorities,
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findWorkerByUsername(authentication.getName()).getIdLocation()
                    .equals(servicesUtils.findWorkerById(idWorker).getIdLocation())) {
                throw new AccessDeniedException("Access Denied");
            } else if (!servicesUtils.findWorkerByUsername(authentication.getName()).getId()
                    .equals(servicesUtils.findWorkerById(idWorker).getId())) {
                throw new AccessDeniedException("Access Denied");
            }

        }

        List<AppointmentResponse> listAppointments = appointmentRepository.GetReponsesByIdWorker(idWorker);

        List<AppointmentResponse> result = findAppointmentDetailsByListAppointment(listAppointments);
        APIresponse<List<AppointmentResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setResult(result);
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        return rp;
    }

    private List<AppointmentResponse> findAppointmentDetailsByListAppointment(List<AppointmentResponse> listAppointments) {
        List<AppointmentResponse> listAppointmentResponses = new ArrayList<AppointmentResponse>();
        for (AppointmentResponse response : listAppointments) {
            List<ServiceEntity> listServices = appointmentRepository.findServicesByAppointmentId(response.getId());
            List<ComboEntity> listCombos = appointmentRepository.findCombosByAppointmentId(response.getId());
            response.setIdService(listServices);
            response.setIdCombo(listCombos);
            listAppointmentResponses.add(response);
        }
        return listAppointmentResponses;
    }

    private AppointmentResponse findAppointmentDetailsByAppointment(AppointmentResponse appointment) {
        List<ServiceEntity> listServices = appointmentRepository.findServicesByAppointmentId(appointment.getId());
        List<ComboEntity> listCombos = appointmentRepository.findCombosByAppointmentId(appointment.getId());
        appointment.setIdService(listServices);
        appointment.setIdCombo(listCombos);
        return appointment;
    }

    private boolean checkValidInfomationRq(AppointmentCreationRequest rq) {
        if (!rq.getDateTime().isAfter(LocalDateTime.now()))
            return false;

        if (AppointmentStatus.WAITING.compareTo(rq.getStatus()) != 0)
            return false;

        if (!servicesUtils.isCustomerIdExisted(rq.getIdCustomer()))
            return false;

        if (!servicesUtils.isWorkerIdExisted(rq.getIdWorker()))
            return false;

        if (!servicesUtils.isLocationIdExisted(rq.getIdLocation()))
            return false;

        if (!servicesUtils.findWorkerById(rq.getIdWorker()).getIdLocation().equals(rq.getIdLocation()))
            throw new AppException(ErrorCode.WORKER_LOCATION_NOT_MATCHED);

        return true;
    }

    private boolean checkValidInfomationUpdate(AppointmentUpdationRequest rq) {

        if (!rq.getDateTime().isAfter(LocalDateTime.now()))
            return false;

        if (!servicesUtils.isCustomerIdExisted(rq.getIdCustomer()))
            return false;

        if (!servicesUtils.isWorkerIdExisted(rq.getIdWorker()))
            return false;

        if (!servicesUtils.isLocationIdExisted(rq.getIdLocation()))
            return false;

        if (!servicesUtils.findWorkerById(rq.getIdWorker()).getIdLocation().equals(rq.getIdLocation()))
            throw new AppException(ErrorCode.WORKER_LOCATION_NOT_MATCHED);

        return true;
    }

    // Shedule Task

    @Scheduled(fixedDelay = 120000)
    public void checkOverdueAppointment() {
        List<Appointment> list = appointmentRepository.findAll();
        for (Appointment appointment : list) {
            if (appointment.getDateTime().isBefore(LocalDateTime.now())
                    && appointment.getStatus().compareTo(AppointmentStatus.WAITING) == 0) {
                appointment.setStatus(AppointmentStatus.OVERDUE);
                appointmentRepository.save(appointment);
                List<String> userIds = new ArrayList<>();
                userIds.add(appointment.getCustomer().getId());
                userIds.add(appointment.getWorker().getId());
                servicesUtils.sendNotificationsToUsers(userIds, "Overdue Appointmet",
                        "The appointment at " + appointment.getDateTime().toString().replace("T", " ") + " is overdue");
            }
        }
    }

}
