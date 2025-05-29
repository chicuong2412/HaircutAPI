package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.enity.Appointment;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {

   @Query("SELECT a FROM Appointment a WHERE a.idCustomer = :idCustomer")
   List<Appointment> getByIdCustomer(@Param("idCustomer") String idCustomer);

   List<Appointment> findAllByIdLocation(String idLocation);

   @Query("SELECT a FROM Appointment a WHERE a.idWorker = :idWorker")
   List<Appointment> findAllByIdWorker(@Param("idWorker") String idWorker);

   @Query("SELECT new com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse(" +
         "a.id, " +
         "c, " +
         "w, " +
         "a.idLocation, " +
         "a.status, " +
         "a.dateTime, " +
         "ad.price, " +
         "a.isDeleted" +
         ") " +
         "FROM Appointment a " +
         "INNER JOIN AppointmentDetails ad ON a.id = ad.id " +
         "INNER JOIN Customer c ON a.idCustomer = c.id " +
         "INNER JOIN Worker w ON a.idWorker = w.id " +
         "WHERE a.idCustomer = :idCustomer")
   List<AppointmentResponse> GetReponsesByIdCustomer(@Param("idCustomer") String idCustomer);

   @Query("SELECT new com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse(" +
         "a.id, " +
         "c, " +
         "w, " +
         "a.idLocation, " +
         "a.status, " +
         "a.dateTime, " +
         "ad.price, " +
         "a.isDeleted" +
         ") " +
         "FROM Appointment a " +
         "INNER JOIN AppointmentDetails ad ON a.id = ad.id " +
         "INNER JOIN Customer c ON a.idCustomer = c.id " +
         "INNER JOIN Worker w ON a.idWorker = w.id " +
         "WHERE a.idWorker = :idWorker")
   List<AppointmentResponse> GetReponsesByIdWorker(@Param("idWorker") String idWorker);

   @Query("SELECT new com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse(" +
         "a.id, " +
         "c, " +
         "w, " +
         "a.idLocation, " +
         "a.status, " +
         "a.dateTime, " +
         "ad.price, " +
         "a.isDeleted" +
         ") " +
         "FROM Appointment a " +
         "INNER JOIN AppointmentDetails ad ON a.id = ad.id " +
         "INNER JOIN Customer c ON a.idCustomer = c.id " +
         "INNER JOIN Worker w ON a.idWorker = w.id" +
         " WHERE a.id = :id")
   AppointmentResponse getAppointmentResponseById(@Param("id") String id);

   @Query("SELECT s FROM AppointmentDetails ad JOIN ad.idService s WHERE ad.id = :id")
   List<ServiceEntity> findServicesByAppointmentId(@Param("id") String id);

   @Query("SELECT c FROM AppointmentDetails ad JOIN ad.idCombo c WHERE ad.id = :id")
   List<ComboEntity> findCombosByAppointmentId(@Param("id") String id);

   @Query("SELECT new com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse(" +
         "a.id, " +
         "c, " +
         "w, " +
         "a.idLocation, " +
         "a.status, " +
         "a.dateTime, " +
         "ad.price, " +
         "a.isDeleted" +
         ") " +
         "FROM Appointment a " +
         "INNER JOIN AppointmentDetails ad ON a.id = ad.id " +
         "INNER JOIN Customer c ON a.idCustomer = c.id " +
         "INNER JOIN Worker w ON a.idWorker = w.id " +
         "WHERE a.idLocation = :idLocation")
   List<AppointmentResponse> GetReponsesByIdLocation(@Param("idLocation") String idLocation);

   @Query("SELECT new com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse(" +
         "a.id, " +
         "c, " +
         "w, " +
         "a.idLocation, " +
         "a.status, " +
         "a.dateTime, " +
         "ad.price, " +
         "a.isDeleted" +
         ") " +
         "FROM Appointment a " +
         "INNER JOIN AppointmentDetails ad ON a.id = ad.id " +
         "INNER JOIN Customer c ON a.idCustomer = c.id " +
         "INNER JOIN Worker w ON a.idWorker = w.id ")
   List<AppointmentResponse> GetAllReponses();

}
