package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.enity.Appointment;
import com.haircutAPI.HaircutAPI.enity.AppointmentDetails;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.enity.Worker;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapper {

    Appointment toAppointment(AppointmentCreationRequest rq);

    AppointmentResponse toAppointmentResponse(Appointment appointment);

    @Mapping(target = "idService", ignore = true)
    @Mapping(target = "idCombo", ignore = true)
    AppointmentDetails toAppointmentDetails(AppointmentCreationRequest rq);

    List<AppointmentResponse> toAppointmentResponses(List<Appointment> appointments);

    List<AppointmentResponse> toAppointmentResponse(List<AppointmentDetails> appointmentDetails);

    void updateAppointment(@MappingTarget Appointment appointment, AppointmentUpdationRequest rq);

    @Mapping(target = "idService", ignore = true)
    @Mapping(target = "idCombo", ignore = true)
    void updateAppointment(@MappingTarget AppointmentDetails appointmentDetails, AppointmentUpdationRequest rq);

    default AppointmentResponse appointmentResponseGenerator(Appointment appointment,
            AppointmentDetails appointmentDetail, Customer customer, Worker worker) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .idCombo(appointmentDetail.getIdCombo())
                .customer(customer)
                .idLocation(appointment.getIdLocation())
                .worker(worker)
                .status(appointment.getStatus())
                .idService(appointmentDetail.getIdService())
                .price(appointmentDetail.getPrice())
                .build();
    }
}
