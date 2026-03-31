package com.doritech.ItemService.Validation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.ResponseEntity;
import com.doritech.ItemService.Exception.ExternalServiceException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.FeignClient.ParamFeignClient;
import com.doritech.ItemService.Response.ParamPayloadDTO;
import com.doritech.ItemService.Response.ParamResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

@Service
public class ValidationService {

	private static final Logger logger = LoggerFactory
			.getLogger(ValidationService.class);

	@Autowired
	private ParamFeignClient paramFeignClient;

	@Autowired
	private ObjectMapper objectMapper;

	public List<ParamResponseDTO> validateParamList(String code,
			String serial) {

		try {
			ResponseEntity response = paramFeignClient.getParamValues(code,
					serial);

			if (response == null || response.getPayload() == null) {
				logger.error("Null response received from Param Service");
				throw new ExternalServiceException(
						"Invalid response from Param Service");
			}

			ParamPayloadDTO payload = objectMapper
					.convertValue(response.getPayload(), ParamPayloadDTO.class);

			List<ParamResponseDTO> paramList = payload.getData();

			if (paramList == null || paramList.isEmpty()) {
				logger.error("Param list is empty for code: {} and serial: {}",
						code, serial);
				throw new ResourceNotFoundException(
						"No parameters found for given code and serial");
			}

			logger.info(
					"Fetched {} parameters successfully for code: {} and serial: {}",
					paramList.size(), code, serial);

			return paramList;

		} catch (FeignException.NotFound ex) {
			logger.error(
					"Parameters not found in Param Service for code: {} and serial: {}",
					code, serial, ex);
			throw new ResourceNotFoundException("Parameters not found");

		} catch (FeignException ex) {
			logger.error(
					"Feign error while calling Param Service for code: {} and serial: {}",
					code, serial, ex);
			throw new ExternalServiceException(
					"Unable to fetch parameters from Param Service");

		} catch (Exception ex) {
			logger.error(
					"Unexpected error while calling Param Service for code: {} and serial: {}",
					code, serial, ex);
			throw new ExternalServiceException(
					"Something went wrong while fetching parameters");
		}
	}

}