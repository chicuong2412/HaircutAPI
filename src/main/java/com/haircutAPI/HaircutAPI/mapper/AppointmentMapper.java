package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.enity.Appointment;
import com.haircutAPI.HaircutAPI.enity.AppointmentDetails;

@Mapper
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentCreationRequest rq);

    AppointmentResponse toAppointmentResponse(Appointment appointment);

    AppointmentDetails toAppointmentDetails(AppointmentCreationRequest rq);

    List<AppointmentResponse> toAppointmentResponses(List<Appointment> appointments);

    List<AppointmentResponse> toAppointmentResponse(List<AppointmentDetails> appointmentDetails);


    
    void updateAppointment(@MappingTarget Appointment appointment, AppointmentUpdationRequest rq);
    void updateAppointment(@MappingTarget AppointmentDetails appointmentDetails, AppointmentUpdationRequest rq);

    default void updateAppointmetResponses(@MappingTarget List<AppointmentResponse> rps,
            List<AppointmentDetails> appointmentResponses) {
        for (int i = 0; i < rps.size(); i++) {
            rps.get(i).setIdCombo(appointmentResponses.get(i).getIdCombo());
            rps.get(i).setIdService(appointmentResponses.get(i).getIdService());
        }
    }

    default void updateAppointmetResponse(AppointmentResponse rp, AppointmentDetails appointmentDetails) {
        rp.setIdCombo(appointmentDetails.getIdCombo());
        rp.setIdService(appointmentDetails.getIdService());
    }
}
