package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.ComboResponse;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComboMapper {
    ComboEntity toComboEntity(@MappingTarget ComboEntity comboEntity, ComboCreationRequest rq);

    void updateComboEntity(@MappingTarget ComboEntity comboEntity, ComboUpdationRequest rq);

    ComboResponse toComboResponse(ComboEntity comboEntity);

    List<ComboResponse> toComboResponses(List<ComboEntity> entities);
}
